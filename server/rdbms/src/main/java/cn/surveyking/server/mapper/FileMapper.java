package cn.surveyking.server.mapper;

import cn.surveyking.server.domain.model.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;

/**
 * @author javahuang
 * @date 2021/9/8
 */
public interface FileMapper extends BaseMapper<File> {

	@Cacheable(cacheNames = "fileCache", key = "#id", unless = "#result == null")
	File selectById(Serializable id);

}