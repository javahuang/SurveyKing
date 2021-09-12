package cn.surveyking.server.storage;

import cn.surveyking.server.core.constant.AppConsts;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author javahuang
 * @date 2021/9/6
 */
public interface StorageService {

	List<String> SUPPORT_IMAGE_LIST = Arrays.asList(AppConsts.SUPPORT_IMAGE_TYPE);

	default void init() {
	}

	StorePath uploadFile(MultipartFile file);

	/**
	 * 上传附件并且生成缩略图 缩略图为上传文件名+缩略图后缀 _150x150，如 xxx.jpg，缩略图为 xxx_150x150.jpg
	 */
	StorePath uploadImage(MultipartFile file);

	byte[] download(String filePath);

}
