package cn.surveyking.server.domain.model;

import cn.surveyking.server.core.model.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author javahuang
 * @date 2021/8/23
 */
@Data
@TableName(value = "t_user", autoResultMap = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class User extends BaseModel {

	private String name;

	private String phone;

	private String email;

	private String avatarUrl;

	private String gender;

	private String birthday;

	/**
	 * 0失活 1 激活
	 */
	private Integer status;

}
