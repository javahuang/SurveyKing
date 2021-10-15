package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 这个只用来存用户自定义的权限
 *
 * @author javahuang
 * @date 2021/10/12
 */
@Data
@TableName(value = "t_permission", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Permission extends BaseModel {

	private String name;

	private String code;

	private String remark;

}
