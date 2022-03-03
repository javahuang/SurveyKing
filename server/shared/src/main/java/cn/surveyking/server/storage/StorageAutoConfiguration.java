package cn.surveyking.server.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author javahuang
 * @date 2021/9/6
 */
@Configuration
@ConfigurationPropertiesScan("cn.surveyking.server.storage")
public class StorageAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "file-storage.local", name = "root-path")
	@ConditionalOnMissingBean
	public StorageService storageService(StorageProperties properties) throws IOException {
		return new LocalStorageService(properties);
	}

}
