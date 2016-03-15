package co.xarx.trix.config;

import co.xarx.trix.config.security.BitMaskPermissionGrantingStrategy;
import co.xarx.trix.security.acl.MultitenantAclService;
import co.xarx.trix.security.acl.TrixPermission;
import org.hibernate.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	ApplicationContext context;

	@Bean
	public ProviderManager authenticationManager(UserDetailsService userDetailsService) {
		List<AuthenticationProvider> providers = new ArrayList<>();

		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
//		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder());
//		ReflectionSaltSource saltSource = new ReflectionSaltSource();
//		saltSource.setUserPropertyToUse("username");
//		daoProvider.setSaltSource(saltSource);
		daoProvider.setUserDetailsService(userDetailsService);

		AnonymousAuthenticationProvider anonymousProvider = new AnonymousAuthenticationProvider("anonymousKey");

		providers.add(daoProvider);
		providers.add(anonymousProvider);
		ProviderManager providerManager = new ProviderManager(providers);
		providerManager.setEraseCredentialsAfterAuthentication(true);
		return providerManager;
	}


	@Bean
	public AclAuthorizationStrategy aclAuthorizationStrategy() {
		return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	@Bean
	public PermissionGrantingStrategy aclPermissionGrantingStrategy() {
		return new BitMaskPermissionGrantingStrategy(aclAuditLogger());
	}

	@Bean
	public AuditLogger aclAuditLogger() {
		return new ConsoleAuditLogger();
	}

	@Bean
	@DependsOn("redisConnectionFactory")
	public CacheManager aclCacheManager(RedisConnectionFactory redisConnectionFactory) throws IOException {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();

		RedisCacheManager factory = new RedisCacheManager(redisTemplate);
		factory.afterPropertiesSet();
		return factory;
	}


	@Bean
	public AclCache aclCache(RedisConnectionFactory redisConnectionFactory) throws CacheException, IOException {
		return new SpringCacheBasedAclCache(aclCacheManager(redisConnectionFactory).getCache("acl"), aclPermissionGrantingStrategy(), aclAuthorizationStrategy());
	}

	@Bean
	public PermissionEvaluator permissionEvaluator(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) throws IOException {
		return new AclPermissionEvaluator(aclService(dataSource, redisConnectionFactory));
	}

	@Bean
	public DefaultPermissionFactory aclPermissionFactory() {
		return new DefaultPermissionFactory(TrixPermission.class);
	}

	@Bean
	public LookupStrategy aclLookupStrategy(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) throws CacheException, IOException {
		BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache(redisConnectionFactory), aclAuthorizationStrategy(), aclPermissionGrantingStrategy());
		lookupStrategy.setPermissionFactory(aclPermissionFactory());
		return lookupStrategy;
	}

	@Bean
	public MutableAclService aclService(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) throws CacheException, IOException {
		MultitenantAclService aclService = new MultitenantAclService(dataSource,
				aclLookupStrategy(dataSource, redisConnectionFactory), aclCache(redisConnectionFactory));
		aclService.setClassIdentityQuery("SELECT @@IDENTITY");
		aclService.setSidIdentityQuery("SELECT @@IDENTITY");
		return aclService;
	}

	@Override
	@DependsOn({"redisConnectionFactory", "dataSource"})
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		try {
			DataSource dataSource = context.getBean(DataSource.class);
			RedisConnectionFactory redisConnectionFactory = context.getBean(RedisConnectionFactory.class);
			expressionHandler.setPermissionEvaluator(permissionEvaluator(dataSource, redisConnectionFactory));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return expressionHandler;
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}