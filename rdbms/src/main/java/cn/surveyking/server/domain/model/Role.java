package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/24
 */
@Data
@TableName(value = "t_role", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseModel {

	private String name;

	private String code;

	private String remark;

	/** 用户权限列表，以逗号分割 */
	private String authority;

	@TableField(fill = FieldFill.INSERT)
	private Date createAt;

}
