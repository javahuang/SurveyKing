package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author javahuang
 * @date 2021/10/12
 */
@Data
@TableName(value = "t_account", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class Account extends BaseModel {

	/** 用户ID */
	private String userId;

	/** 用户类型 */
	private String userType;

	/** 认证方式 */
	private String authType;

	/** 认证账户 */
	private String authAccount;

	/** 密码 */
	private String authSecret;

	/** 加密盐 */
	private String secretSalt;

	/** 0失活 1激活 */
	private Integer status;

}
