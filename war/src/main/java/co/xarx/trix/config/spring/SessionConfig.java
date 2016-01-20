package co.xarx.trix.config.spring;

import co.xarx.trix.config.multitenancy.MultitenantCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;

@Configuration
@EnableCaching
@EnableRedisHttpSession
public class SessionConfig extends CachingConfigurerSupport {

	@Bean
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}


	@Bean
	public KeyGenerator keyGenerator() {
		return (o, method, objects) -> {
			// This will generate a unique key of the class name, the method name,
			// and all method parameters appended.
			StringBuilder sb = new StringBuilder();
			sb.append(o.getClass().getName());
			sb.append(method.getName());
			for (Object obj : objects) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}

	@Bean
	public CacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
		cacheManager.setDefaultExpiration(60);
//		cacheManager.setCacheBuilder(CacheBuilder.
//				newBuilder().
//				expireAfterWrite(1, TimeUnit.MINUTES).
//				maximumSize(1000));
		return new MultitenantCacheManager(cacheManager);
	}

	@Bean
	public CookieHttpSessionStrategy cookieHttpSessionStrategy() {
		CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
		cookieHttpSessionStrategy.setCookieName("JSESSIONID");

		return cookieHttpSessionStrategy;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName("meminstance1");
		jedisConnectionFactory.setPort(6379);
		jedisConnectionFactory.setPassword("108dbd3786884ddc94ec2669e948273b09828ec44FFd55a6cdd3b007c" +
				"c018a16e93cb1Fc68060a9c7095324056d1bF9b08Fdd1b78c1c96351ceb129e6e959357");
		return jedisConnectionFactory;
	}
}
