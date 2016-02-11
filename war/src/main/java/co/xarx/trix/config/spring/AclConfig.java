package co.xarx.trix.config.spring;

import co.xarx.trix.security.acl.MultitenantAclService;
import co.xarx.trix.security.acl.TrixPermission;
import org.hibernate.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class AclConfig {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JedisConnectionFactory cacheConnectionFactory;

	@Bean
	public AclAuthorizationStrategy aclAuthorizationStrategy() {
		return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Bean
	public PermissionGrantingStrategy aclPermissionGrantingStrategy() {
		return new DefaultPermissionGrantingStrategy(aclAuditLogger());
	}

	@Bean
	public AuditLogger aclAuditLogger() {
		return new ConsoleAuditLogger();
	}

	@Bean
	public CacheManager aclCacheManager() throws IOException {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(cacheConnectionFactory);
		redisTemplate.afterPropertiesSet();

		RedisCacheManager factory = new RedisCacheManager(redisTemplate);
		factory.afterPropertiesSet();
		return factory;
	}


	@Bean
	public AclCache aclCache() throws CacheException, IOException {
		return new SpringCacheBasedAclCache(aclCacheManager().getCache("acl"), aclPermissionGrantingStrategy(), aclAuthorizationStrategy());
	}

	@Bean
	public DefaultPermissionFactory aclPermissionFactory() {
		return new DefaultPermissionFactory(TrixPermission.class);
	}

	@Bean
	public LookupStrategy aclLookupStrategy() throws CacheException, IOException {
		BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), aclPermissionGrantingStrategy());
		lookupStrategy.setPermissionFactory(aclPermissionFactory());
		return lookupStrategy;
	}

	@Bean
	public MutableAclService aclService() throws CacheException, IOException {
		MultitenantAclService aclService = new MultitenantAclService(dataSource, aclLookupStrategy(), aclCache());
		aclService.setClassIdentityQuery("SELECT @@IDENTITY");
		aclService.setSidIdentityQuery("SELECT @@IDENTITY");
		return aclService;
	}
}