package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.constant.ErrorCode;
import cn.surveyking.server.core.constant.LocalStorageNameStrategyEnum;
import cn.surveyking.server.core.constant.LocalStoragePathStrategyEnum;
import cn.surveyking.server.core.exception.ErrorCodeException;
import cn.surveyking.server.core.uitls.*;
import cn.surveyking.server.domain.dto.*;
import cn.surveyking.server.domain.mapper.FileViewMapper;
import cn.surveyking.server.domain.model.File;
import cn.surveyking.server.mapper.FileMapper;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.service.ProjectService;
import cn.surveyking.server.service.SurveyService;
import cn.surveyking.server.storage.StorageProperties;
import cn.surveyking.server.storage.StorageService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static cn.surveyking.server.core.constant.ErrorCode.FileUploadError;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author javahuang
 * @date 2021/9/10
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private final StorageService storageService;

    private final FileViewMapper fileViewMapper;

    private final StorageProperties storageProperties;

    private final AtomicLong seq = new AtomicLong(System.currentTimeMillis());

    @Autowired
    @Lazy
    private ProjectService projectService;

    @Autowired
    @Lazy
    private SurveyService surveyService;

    @Override
    public void deleteFile(String id) {
        removeById(id);
    }

    @Override
    @SneakyThrows(IOException.class)
    public FileView upload(UploadFileRequest request) {
        MultipartFile uploadFile = request.getFile();
        String filePath = getFileNameByStrategy(Objects.requireNonNull(uploadFile.getOriginalFilename()));
        String extName = StringUtils.substringAfterLast(uploadFile.getOriginalFilename(), ".");

        if (StringUtils.isEmpty(request.getProjectId()) || StringUtils.isEmpty(request.getQuestionId())) {
            throw new ErrorCodeException(FileUploadError);
        }
        ProjectView projectView = projectService.getProject(request.getProjectId());
        // 校验对应项目状态
        surveyService.validateProject(projectView);

        //  校验文件拓展名
        SurveySchema uploadSchema = SchemaHelper.flatSurveySchema(projectView.getSurvey()).stream()
                .filter(x -> x.getId().equals(request.getQuestionId())).findFirst()
                .orElseThrow(() -> new ErrorCodeException(FileUploadError));
        String fileAccept = uploadSchema.getChildren().get(0).getAttribute().getFileAccept();
        if (StringUtils.isNotEmpty(fileAccept)) {
            // 只允许上传指定格式的文件
            boolean notAllowExtension = Arrays.stream(fileAccept.split(","))
                    .map(type -> type.trim().replaceFirst("\\.", "")).noneMatch(type -> type.equalsIgnoreCase(extName));
            if (notAllowExtension) {
                throw new ErrorCodeException(FileUploadError);
            }
        } else {
            // 默认文件类型白名单限制
            boolean notAllowExtension = Arrays.stream(FileUtils.ALLOWED_EXTENSIONS)
                    .noneMatch(ext -> ext.equalsIgnoreCase(extName));
            if (notAllowExtension) {
                throw new ErrorCodeException(FileUploadError);
            }
        }

        File file = new File();
        file.setId(NanoIdUtils.randomNanoId());
        file.setStorageType(request.getFileType());
        file.setOriginalName(uploadFile.getOriginalFilename());
        file.setFileName(filePath);
        filePath = getFilePathByStrategy(request, filePath);
        file.setFilePath(filePath);

        if (isSupportImage(uploadFile.getOriginalFilename())) {
            String thumbImagePath = storageService.getThumbImageFilePath(filePath);
            try (InputStream inputStream = uploadFile.getInputStream()) {
                storageService.uploadFile(storageService.generateThumbImage(inputStream), thumbImagePath);
                file.setThumbFilePath(thumbImagePath);
            }
        }
        storageService.uploadFile(uploadFile.getInputStream(), filePath);

        save(file);
        FileView fileView = fileViewMapper.toView(file);

        if (AppConsts.FileType.BARCODE == request.getFileType()) {
            fileView.setContent(BarcodeReader.readBarcode(uploadFile.getInputStream()));
        }
        return fileView;
    }

    /**
     * 根据存储配置，按照指定规则生成相应的文件名
     *
     * @param originalName 原文件名
     * @return 文件名
     */
    private String getFileNameByStrategy(String originalName) {
        String strategy = storageProperties.getLocal().getNameStrategy();
        int idx = originalName.lastIndexOf('.');
        String fileType = originalName.substring(idx);
        if (LocalStorageNameStrategyEnum.SEQ_ADN_ORIGINAL_NAME.getStrategy().equals(strategy)) {
            return seq.incrementAndGet() + "_" + originalName;
        } else if (LocalStorageNameStrategyEnum.ORIGINAL_NAME_AND_SEQ.getStrategy().equals(strategy)) {
            return originalName.substring(0, idx) + "_" + seq.incrementAndGet() + fileType;
        } else if (LocalStorageNameStrategyEnum.SEQ.getStrategy().equals(strategy)) {
            return seq.incrementAndGet() + fileType;
        } else {
            return UUID.randomUUID().toString().replace("-", "") + fileType;
        }
    }

    /**
     * 根据本地存储配置，生成不同方式的文件保存路径
     *
     * @param request  文件上传请求
     * @param fileName 文件名
     * @return 文件保存路径
     */
    private String getFilePathByStrategy(UploadFileRequest request, String fileName) {
        String strategy = storageProperties.getLocal().getPathStrategy();
        if (LocalStoragePathStrategyEnum.BY_ID.getStrategy().equals(strategy) && request.getBasePath() != null) {
            return request.getBasePath() + java.io.File.separator + fileName;
        } else if (LocalStoragePathStrategyEnum.BY_DATE.getStrategy().equals(strategy)) {
            String dateFormat = storageProperties.getLocal().getDateFormat();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate now = LocalDate.now();
            dateFormat = now.format(dateTimeFormatter).replace("/", java.io.File.separator).replace("\\",
                    java.io.File.separator);
            return dateFormat + java.io.File.separator + fileName;
        } else {
            return fileName;
        }
    }

    @Override
    public List<FileView> listFiles(FileQuery query) {
        return fileViewMapper.toView(
                list(Wrappers.<File>lambdaQuery().eq(query.getType() != null, File::getStorageType, query.getType())
                        // 默认只能查询自己的
                        .eq(CollectionUtils.isEmpty(query.getIds()), File::getCreateBy,
                                SecurityContextUtils.getUserId())
                        // 可以根据 ids 查询已知文件
                        .in(!CollectionUtils.isEmpty(query.getIds()), File::getId, query.getIds())));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Resource> loadFile(FileQuery query) {
        String fileId = query.getId();
        if (fileId.contains("@")) {
            fileId = fileId.substring(0, fileId.lastIndexOf("@"));
        }
        File file = getById(fileId);
        if (file == null) {
            log.error("未找到对应的文件 {}", fileId);
            throw new ErrorCodeException(ErrorCode.FileNotExists);
        }
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(file.getOriginalName());
        // FIXME: 在线预览 mp4 文件会报错，但不影响使用
        return ResponseEntity.ok().contentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM))
                .headers(query.getHeaders())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        query.getDispositionType() == AppConsts.DispositionTypeEnum.inline
                                ? AppConsts.DispositionTypeEnum.inline.name()
                                : HTTPUtils.getContentDispositionValue(file.getOriginalName()))
                .body(new ByteArrayResource(storageService
                        .download(query.getId().contains("@") ? file.getThumbFilePath() : file.getFilePath())));
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate(String name) {
        String templateName = name + ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, HTTPUtils.getContentDispositionValue(templateName))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new ClassPathResource("template/" + templateName));
    }

    /**
     * 生成带文件后缀的id，由于配置了 STATIC_RESOURCES ，会导致单jar部署请求 /preview/xxx.xlsx 的时候报 404
     *
     * @param originalName
     * @return
     */
    private String getFileId(String originalName) {
        String suffix = StringUtils.substringAfterLast(originalName, ".");
        if (isEmpty(suffix)) {
            return NanoIdUtils.randomNanoId();
        }
        return NanoIdUtils.randomNanoId() + "." + suffix;
    }

}
