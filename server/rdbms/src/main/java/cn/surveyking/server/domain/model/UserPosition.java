package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户岗位
 *
 * @author javahuang
 * @date 2021/11/2
 */
@Data
@TableName(value = "t_user_position", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class UserPosition extends BaseModel {

	private String userId;

	private String deptId;

	private String positionId;

	/**
	 * 是否主岗
	 */
	private Boolean isPrimaryPosition;

	@JsonIgnore
	@TableField(exist = false)
	private boolean deleted = false;

}
