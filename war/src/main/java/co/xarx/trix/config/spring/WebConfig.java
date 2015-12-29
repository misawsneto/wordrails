package co.xarx.trix.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;

@Configuration
@EnableRedisHttpSession
public class WebConfig {

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
