package co.xarx.trix.config;

import co.xarx.trix.config.security.BitMaskPermissionGrantingStrategy;
import co.xarx.trix.config.security.MultitenantAclService;
import co.xarx.trix.config.security.Permissions;
import org.hibernate.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclTestConfig {


	@Autowired
	ApplicationContext context;

	@Bean
	public ProviderManager authenticationManager(UserDetailsService userDetailsService) {
		List<AuthenticationProvider> providers = new ArrayList<>();

		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
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
	public AuditLogger aclAuditLogger() {
		return new ConsoleAuditLogger();
	}

	@Bean
	public CacheManager aclCacheManager() throws IOException {
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
		simpleCacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("acl")));
		return simpleCacheManager;
	}

	@Bean
	public PermissionGrantingStrategy permissionGrantingStrategy() {
		return new BitMaskPermissionGrantingStrategy();
	}


	@Bean
	public AclCache aclCache() throws CacheException, IOException {
		return new SpringCacheBasedAclCache(aclCacheManager().getCache("acl"),
				permissionGrantingStrategy(), aclAuthorizationStrategy());
	}

	@Bean
	public PermissionEvaluator permissionEvaluator(DataSource dataSource) throws IOException {
		return new AclPermissionEvaluator(aclService(dataSource));
	}

	@Bean
	public DefaultPermissionFactory aclPermissionFactory() {
		return new DefaultPermissionFactory(Permissions.class);
	}

	@Bean
	public LookupStrategy aclLookupStrategy(DataSource dataSource) throws CacheException, IOException {
		BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource, aclCache(),
				aclAuthorizationStrategy(), permissionGrantingStrategy());
		lookupStrategy.setPermissionFactory(aclPermissionFactory());
		return lookupStrategy;
	}

	@Bean
	public MutableAclService aclService(DataSource dataSource) throws CacheException, IOException {
		MultitenantAclService aclService = new MultitenantAclService(dataSource,
				aclLookupStrategy(dataSource), aclCache());
		aclService.setClassIdentityQuery("SELECT @@IDENTITY");
		aclService.setSidIdentityQuery("SELECT @@IDENTITY");
		return aclService;
	}

//	@Bean
//	public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
//		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//		try {
//			DataSource dataSource = context.getBean(DataSource.class);
//			expressionHandler.setPermissionEvaluator(permissionEvaluator(dataSource));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return expressionHandler;
//	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
