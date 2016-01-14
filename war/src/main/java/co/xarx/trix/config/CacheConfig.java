package co.xarx.trix.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

//@Configuration
//@EnableCaching
public class CacheConfig {//extends CachingConfigurerSupport {

//	@Bean
//	public CacheManager cacheManager(RedisTemplate redisTemplate) { // redistemplate bean is defined else where and invoked here
//		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//		// Number of seconds before expiration. Defaults to unlimited (0)
//		cacheManager.setDefaultExpiration(60); // 1 min
//		return cacheManager;
//	}
}