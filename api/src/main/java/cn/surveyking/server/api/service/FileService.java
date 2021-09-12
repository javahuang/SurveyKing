package cn.surveyking.server.api.service;

import cn.surveyking.server.api.domain.dto.FileView;
import cn.surveyking.server.core.constant.AppConsts;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/10
 */
public interface FileService {

	FileView upload(MultipartFile file, AppConsts.StorageType storageType);

	List<FileView> listImages(AppConsts.StorageType storageType);

	void deleteImage(String id);

	Resource loadAsResource(String attachmentId);

}
