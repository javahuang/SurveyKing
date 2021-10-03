package cn.surveyking.server.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.surveyking.server.web.domain.model.User;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserMapper extends BaseMapper<User> {

	// @Override
	// @Cacheable // 是否可以使用 Cacheable 缓存
	// User selectOne(Wrapper<User> queryWrapper);

}
