package cn.surveyking.server.impl;

import cn.surveyking.server.core.constant.AppConsts;
import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.core.uitls.SecurityContextUtils;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.mapper.FileViewMapper;
import cn.surveyking.server.domain.model.File;
import cn.surveyking.server.mapper.FileMapper;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.storage.StorageService;
import cn.surveyking.server.storage.StorePath;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/10
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileMapper fileMapper;

	private final StorageService storageService;

	@Override
	public void deleteImage(String id) {
		fileMapper.deleteById(id);
	}

	@Override
	public FileView upload(MultipartFile uploadFile, AppConsts.StorageType storageType) {
		StorePath storePath;
		if (isSupportImage(uploadFile.getOriginalFilename())) {
			storePath = storageService.uploadImage(uploadFile);
		}
		else {
			storePath = storageService.uploadFile(uploadFile);
		}
		File file = FileViewMapper.INSTANCE.toFile(storePath, uploadFile.getOriginalFilename(), storageType.getType());
		fileMapper.insert(file);
		FileView fileView = FileViewMapper.INSTANCE.toFileView(file);
		return fileView;
	}

	@Override
	public Resource loadAsResource(String attachmentId) {
		String fileId = attachmentId;
		if (fileId.contains("@")) {
			fileId = attachmentId.substring(0, attachmentId.lastIndexOf("@"));
		}
		File file = fileMapper.selectById(fileId);

		if (file == null) {
			throw new InternalServerError("资源不存在");
		}

		String filePath;
		// 缩略图
		if (attachmentId.contains("@")) {
			filePath = file.getThumbFilePath();
		}
		else {
			filePath = file.getFilePath();
		}
		return new ByteArrayResource(storageService.download(filePath));
	}

	@Override
	public List<FileView> listImages(AppConsts.StorageType storageType) {
		QueryWrapper<File> query = new QueryWrapper<>();
		query.eq("storage_type", storageType.getType())
				.and(i -> i.eq("create_by", SecurityContextUtils.getUsername()).or().eq("shared", 1));
		return FileViewMapper.INSTANCE.toFileView(fileMapper.selectList(query));
	}

}
