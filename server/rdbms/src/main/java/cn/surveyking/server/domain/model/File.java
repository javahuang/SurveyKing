package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Data
@TableName(value = "t_file", autoResultMap = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class File extends BaseModel {

	/**
	 * 原始文件名称
	 */
	private String originalName;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 缩略图路径
	 */
	private String thumbFilePath;

	/**
	 * 文件存储类型
	 */
	private Integer storageType;

	/**
	 * 与其他用户共享
	 */
	private Integer shared;

}
