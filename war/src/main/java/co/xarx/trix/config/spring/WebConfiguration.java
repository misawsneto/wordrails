package co.xarx.trix.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;

@Configuration
@EnableRedisHttpSession
public class WebConfiguration {

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
		return jedisConnectionFactory;
	}
}
