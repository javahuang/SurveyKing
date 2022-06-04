package cn.surveyking.server.storage;

import cn.surveyking.server.core.constant.LocalStorageNameStrategyEnum;
import cn.surveyking.server.core.constant.LocalStoragePathStrategyEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.File;

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
	@NestedConfigurationProperty
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

		/**
		 * 路劲策略：
		 *     1. byNo:所有文件存储在 rootPath 下
		 *     2. byId:按照项目的short-id分文件夹存储,例如 rootPath/RyP2rR
		 *     3. byDate:按照上传日期存储，例如 rootPath/2022/06/01
		 */
		private String pathStrategy = LocalStoragePathStrategyEnum.BY_ID.getStrategy();

		/**
		 * 日期格式
		 */
		private String dateFormat = "yyyyMM" + File.separator + "dd";

		/**
		 * 文件名策略
		 * 		1. seqAndOriginalName: 序列号加原文件名
		 * 		2. originalNameAndSeq: 原文件名+序列号
		 * 		3. Seq: 序列号（项目启动时间戳的自增）
		 * 		4. UUID: 去除短杠'-'的UUID
		 */
		private String nameStrategy = LocalStorageNameStrategyEnum.SEQ_ADN_ORIGINAL_NAME.getStrategy();
	}

}
