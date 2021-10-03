package cn.surveyking.server.web.domain.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/9/8
 */
@Data
@TableName(value = "t_file", autoResultMap = true)
@Accessors(chain = true)
public class File {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

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

	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT, select = false)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private String updateBy;

	/**
	 * 与其他用户共享
	 */
	private Integer shared;

	@TableLogic
	private Integer deleted;

}
