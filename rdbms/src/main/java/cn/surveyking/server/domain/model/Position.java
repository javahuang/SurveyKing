package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位
 *
 * @author javahuang
 * @date 2021/11/2
 */
@Data
@TableName(value = "t_position", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Position extends BaseModel {

	private String name;

	private String code;

	/** 是否虚拟岗 */
	private Boolean isVirtual = false;

	/** 数据权限类型 self:本人 */
	private String dataPermissionType;

	/** 拓展属性 */
	private String propertyJson;

}
