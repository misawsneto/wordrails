package co.xarx.trix.config.spring;

import co.xarx.trix.config.cache.MultitenantCacheManager;
import co.xarx.trix.config.web.CookieAndHeaderHttpSessionStrategy;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableRedisHttpSession
public class SessionConfig extends CachingConfigurerSupport {

	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager() {
		Map<String, Map<RedisTemplate, Integer>> templates = new HashMap<>();

		RedisTemplate personRedisTemplate = redisTemplate();
		personRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Person.class));

		RedisTemplate userRedisTemplate = redisTemplate();
		userRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(User.class));

		RedisTemplate imageRedisTemplate = redisTemplate();
		imageRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Map.class));

		templates.put("person", new HashMap<RedisTemplate, Integer>(){{put(personRedisTemplate, 60);}});
		templates.put("user", new HashMap<RedisTemplate, Integer>(){{put(userRedisTemplate, 60);}});
		templates.put("image", new HashMap<RedisTemplate, Integer>(){{put(imageRedisTemplate, 600);}});

		return new MultitenantCacheManager(templates);
	}

	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
//		CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
//		cookieHttpSessionStrategy.setCookieName("JSESSIONID");

		CookieAndHeaderHttpSessionStrategy sessionStrategy = new CookieAndHeaderHttpSessionStrategy();
		sessionStrategy.setCookieName("JSESSIONID");

		return sessionStrategy;
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

	@Bean
	public RedisOperationsSessionRepository sessionRepository(@Qualifier("sessionRedisTemplate")
																  RedisOperations<String, ExpiringSession> sessionRedisTemplate) {
		RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(sessionRedisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(345600);

		return sessionRepository;
	}
}