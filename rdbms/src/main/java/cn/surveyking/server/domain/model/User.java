package cn.surveyking.server.domain.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author javahuang
 * @date 2021/8/23
 */
@Data
@TableName(value = "t_user", autoResultMap = true)
@Accessors(chain = true)
public class User {

	private String id;

	private String fullName;

	private String avatar;

	private String gender;

	private String username;

	private String phone;

	private String password;

	private String salt;

	/**
	 * 0失活 1 激活
	 */
	private Integer status;

	@TableField(fill = FieldFill.INSERT, select = false)
	private Date createAt;

	@TableField(fill = FieldFill.INSERT, select = false)
	private String createBy;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private Date updateAt;

	@TableField(fill = FieldFill.UPDATE, select = false)
	private String updateBy;

	@TableLogic
	private Integer deleted;

	public User() {
	}

}
