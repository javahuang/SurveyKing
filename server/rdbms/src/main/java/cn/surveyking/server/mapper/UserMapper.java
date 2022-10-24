package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserMapper extends BaseMapper<User> {

	@Select("select t_user.id from t_user " + "left join t_account " + "on t_user.id = t_account.user_id "
			+ "where t_user.name = #{name} and t_account.auth_account = #{authAccount}")
	User getUser(String name, String authAccount);

}
