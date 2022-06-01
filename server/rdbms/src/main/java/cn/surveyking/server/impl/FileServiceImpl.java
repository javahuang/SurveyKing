package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.uitls.HTTPUtils;
import cn.surveyking.server.core.uitls.BarcodeReader;
import cn.surveyking.server.domain.dto.FileQuery;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.dto.UploadFileRequest;
import cn.surveyking.server.domain.mapper.FileViewMapper;
import cn.surveyking.server.domain.model.File;
import cn.surveyking.server.mapper.FileMapper;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.storage.StorageService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

	private AtomicLong seq = new AtomicLong(new Date().getTime());

	@Override
	public void deleteFile(String id) {
		removeById(id);
	}

	@Override
	@SneakyThrows
	public FileView upload(UploadFileRequest request) {
		MultipartFile uploadFile = request.getFile();
		String filePath = seq.incrementAndGet() + "_" + uploadFile.getOriginalFilename();
		File file = new File();
		file.setStorageType(request.getFileType());
		file.setOriginalName(uploadFile.getOriginalFilename());
		file.setFileName(filePath);
		if (request.getBasePath() != null) {
			filePath = request.getBasePath() + java.io.File.separator + filePath;
		}
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
		FileView fileView = fileViewMapper.toFileView(file);

		if (AppConsts.FileType.BARCODE == request.getFileType()) {
			fileView.setContent(BarcodeReader.readBarcode(uploadFile.getInputStream()));
		}
		return fileView;
	}

	@Override
	public List<FileView> listFiles(FileQuery query) {
		return fileViewMapper.toFileView(
				list(Wrappers.<File>lambdaQuery().eq(query.getType() != null, File::getStorageType, query.getType())
						.in(query.getIds() != null && query.getIds().size() > 0, File::getId, query.getIds())));
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
			return null;
		}
		MediaType mediaType;
		try {
			mediaType = MediaType.parseMediaType(Files.probeContentType(Paths.get(file.getFilePath())));
		}
		catch (Exception e) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}
		return ResponseEntity.ok().contentType(mediaType).headers(query.getHeaders())
				.header(HttpHeaders.CONTENT_DISPOSITION,
						query.getDispositionType() == AppConsts.DispositionTypeEnum.inline
								? AppConsts.DispositionTypeEnum.inline.name()
								: HTTPUtils.getContentDispositionValue(file.getOriginalName()))
				.body(new ByteArrayResource(storageService
						.download(query.getId().contains("@") ? file.getThumbFilePath() : file.getFilePath())));
	}

}
