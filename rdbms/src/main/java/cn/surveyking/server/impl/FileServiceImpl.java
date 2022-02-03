package cn.surveyking.server.impl;

import cn.surveyking.server.core.exception.InternalServerError;
import cn.surveyking.server.domain.dto.FileQuery;
import cn.surveyking.server.domain.dto.FileView;
import cn.surveyking.server.domain.mapper.FileViewMapper;
import cn.surveyking.server.domain.model.File;
import cn.surveyking.server.mapper.FileMapper;
import cn.surveyking.server.service.FileService;
import cn.surveyking.server.storage.StorageService;
import cn.surveyking.server.storage.StorePath;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

	private final StorageService storageService;

	private final FileViewMapper fileViewMapper;

	@Override
	public void deleteFile(String id) {
		removeById(id);
	}

	@Override
	public FileView upload(MultipartFile uploadFile, int storageType) {
		StorePath storePath;
		if (isSupportImage(uploadFile.getOriginalFilename())) {
			storePath = storageService.uploadImage(uploadFile);
		}
		else {
			storePath = storageService.uploadFile(uploadFile);
		}
		File file = fileViewMapper.toFile(storePath, uploadFile.getOriginalFilename(), storageType);
		save(file);
		FileView fileView = fileViewMapper.toFileView(file);
		return fileView;
	}

	@Override
	public Resource loadAsResource(String attachmentId) {
		String fileId = attachmentId;
		if (fileId.contains("@")) {
			fileId = attachmentId.substring(0, attachmentId.lastIndexOf("@"));
		}
		File file = getById(fileId);
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
	public List<FileView> listFiles(FileQuery query) {
		return fileViewMapper.toFileView(list(Wrappers.<File>lambdaQuery()
				.eq(query.getType() != null, File::getStorageType, query.getType())
				.in(query.getIds() != null && query.getIds().size() > 0, File::getId, query.getIds())));
	}

}
