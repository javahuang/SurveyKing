package cn.surveyking.server.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author javahuang
 * @date 2021/8/6
 */
@Configuration
@MapperScan("cn.surveyking.server.web.mapper")
public class MybatisPlugConfig {

	private final ObjectMapper objectMapper;

	public MybatisPlugConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		// 允许 MybatisPlus 使用配置的 ObjectMapper 来操作 json
		JacksonTypeHandler.setObjectMapper(objectMapper);
	}

	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
		return interceptor;
	}

	// @Bean
	// public ConfigurationCustomizer configurationCustomizer() {
	// return configuration ->{
	// };
	// }

}
