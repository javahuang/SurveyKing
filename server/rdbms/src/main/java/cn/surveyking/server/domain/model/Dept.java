package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构
 *
 * @author javahuang
 * @date 2021/11/2
 */
@Data
@TableName(value = "t_dept", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Dept extends BaseModel {

	private String parentId;

	private String name;

	private String shortName;

	private String code;

	/** 负责人id */
	private String managerId;

	private Integer sortCode;

	/** 拓展属性 */
	private String propertyJson;

	private String status;

	private String remark;

}
