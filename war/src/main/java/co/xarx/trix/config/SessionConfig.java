package co.xarx.trix.config;

import co.xarx.trix.api.PostByTerm;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.api.v2.StationData;
import co.xarx.trix.config.cache.MultitenantCacheManager;
import co.xarx.trix.config.web.CookieAndHeaderHttpSessionStrategy;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
@EnableRedisHttpSession
public class SessionConfig extends CachingConfigurerSupport {

	@Value("${trix.auth.header:'x-auth-trix-token'}")
	private String trixAuthHeader;
	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.password:}")
	private String redisPassword;
	@Value("${spring.redis.db:}")
	private Integer redisDB;

	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager() {
		Map<String, Map<RedisTemplate, Integer>> templates = new HashMap<>();

		RedisTemplate networkRedisTemplate = redisTemplate();
		networkRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Network.class));

		RedisTemplate personRedisTemplate = redisTemplate();
		personRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Person.class));

		RedisTemplate userRedisTemplate = redisTemplate();
		userRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(User.class));

		RedisTemplate imageRedisTemplate = redisTemplate();
		imageRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Map.class));

		RedisTemplate stationsRedisTemplate = redisTemplate();
		stationsRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(StationData.class));

		RedisTemplate stationsIdsRedisTemplate = redisTemplate();
		stationsIdsRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(List.class));

		RedisTemplate postsIdsRedisTemplate = redisTemplate();
		postsIdsRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(List.class));

		RedisTemplate postViewByIdRedisTemplate = redisTemplate();
		postViewByIdRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(PostView.class));

		RedisTemplate postViewBySlugRedisTemplate = redisTemplate();
		postViewBySlugRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(PostView.class));

		RedisTemplate termPerspectiveViewRedisTemplate = redisTemplate();
		termPerspectiveViewRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(TermPerspectiveView.class));

		RedisTemplate postsByTermRedisTemplate = redisTemplate();
		postsByTermRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(PostByTerm.class));

		templates.put("network", new HashMap<RedisTemplate, Integer>() {{put(networkRedisTemplate, 60);}});
		templates.put("person", new HashMap<RedisTemplate, Integer>(){{put(personRedisTemplate, 60);}});
		templates.put("user", new HashMap<RedisTemplate, Integer>(){{put(userRedisTemplate, 60);}});
		templates.put("image", new HashMap<RedisTemplate, Integer>(){{put(imageRedisTemplate, 600);}});
		templates.put("stations", new HashMap<RedisTemplate, Integer>(){{put(stationsRedisTemplate, 600);}});
		templates.put("stationsIds", new HashMap<RedisTemplate, Integer>(){{put(stationsIdsRedisTemplate, 600);}});
		templates.put("postsIds", new HashMap<RedisTemplate, Integer>(){{put(postsIdsRedisTemplate, 600);}});
		templates.put("postViewById", new HashMap<RedisTemplate, Integer>(){{put(postViewByIdRedisTemplate, 600);}});
		templates.put("postViewBySlug", new HashMap<RedisTemplate, Integer>(){{put(postViewBySlugRedisTemplate, 600);}});
		templates.put("termPerspectiveView", new HashMap<RedisTemplate, Integer>(){{put
				(termPerspectiveViewRedisTemplate, 30);}});
		templates.put("postsByTerm", new HashMap<RedisTemplate, Integer>(){{put(postsByTermRedisTemplate, 600);}});

		return new MultitenantCacheManager(templates);
	}

	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		CookieAndHeaderHttpSessionStrategy sessionStrategy = new CookieAndHeaderHttpSessionStrategy();
		sessionStrategy.setCookieName("JSESSIONID");
		sessionStrategy.setHeaderName(trixAuthHeader);

		return sessionStrategy;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setDatabase(redisDB);
		jedisConnectionFactory.setHostName(redisHost);
		jedisConnectionFactory.setPort(redisPort);
		if (redisPassword != null && !redisPassword.isEmpty()) {
			jedisConnectionFactory.setPassword(redisPassword);
		}
		return jedisConnectionFactory;
	}

	@Bean
	public RedisOperationsSessionRepository sessionRepository(@Qualifier("sessionRedisTemplate")
																  RedisOperations<Object, Object>
																		  sessionRedisTemplate) {
		RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(sessionRedisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(345600);

		return sessionRepository;
	}
}
