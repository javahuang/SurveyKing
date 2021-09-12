package cn.surveyking.server.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import lombok.Data;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 使用 caffeine 自定义 cache，允许针对每个 cacheName 设置单独的 expireTime
 *
 * @author javahuang
 * @date 2021/9/8
 */
@Configuration
@EnableCaching
@ConfigurationProperties("custom-cache")
@Data
public class CacheConfig extends CachingConfigurerSupport {

	private Map<String, Duration> entries;

	@Bean
	@Override
	public CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

			@Override
			protected Cache createConcurrentMapCache(final String name) {
				return new ConcurrentMapCache(name, Caffeine.newBuilder().expireAfter(new Expiry<Object, Object>() {
					@Override
					public long expireAfterCreate(Object key, Object value, long currentTime) {
						return entries.get(name).toNanos();
					}

					@Override
					public long expireAfterUpdate(Object key, Object value, long currentTime,
							@NonNegative long currentDuration) {
						return currentDuration;
					}

					@Override
					public long expireAfterRead(Object key, Object value, long currentTime,
							@NonNegative long currentDuration) {
						return currentDuration;
					}
				}).maximumSize(100).build().asMap(), false);
			}
		};

		cacheManager.setCacheNames(entries.entrySet().stream().map(x -> x.getKey()).collect(Collectors.toList()));
		return cacheManager;
	}

}
