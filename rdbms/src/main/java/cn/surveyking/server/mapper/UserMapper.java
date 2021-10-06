package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author javahuang
 * @date 2021/8/24
 */
public interface UserMapper extends BaseMapper<User> {

	// @Override
	// @Cacheable // 是否可以使用 Cacheable 缓存
	// User selectOne(Wrapper<User> queryWrapper);

}
