package cn.surveyking.server.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author javahuang
 * @date 2021/9/7
 */
@ConfigurationProperties("file-storage")
@Data
public class StorageProperties {

	/**
	 * 本次存储配置
	 */
	private final LocalStorage local = new LocalStorage();

	public final ThumbImage thumbImage = new ThumbImage();

	@Data
	public class ThumbImage {

		/**
		 * 生成的缩略图的宽度
		 */
		private int width = 640;

		/**
		 * 生成的缩略图的高度
		 */
		private int height = 480;

	}

	@Data
	public class LocalStorage {

		/**
		 * 本次存储目录的根目录
		 */
		private String rootPath;

	}

}
