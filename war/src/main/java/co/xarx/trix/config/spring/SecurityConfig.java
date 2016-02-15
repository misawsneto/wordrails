package co.xarx.trix.config.spring;

import co.xarx.trix.web.filter.UserPassAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
				"/js/**",
				"/_newapp/**",
				"/css/**",
				"/libs/**",
				"/tpl/**",
				"/l10n/**",
				"/img/**",
				"/images/**",
				"/invitation/**",
				"/api/persons/search/findByUsername/**",
				"/fonts/**"
		);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public UsernamePasswordAuthenticationFilter userPassAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter authFilter = new UserPassAuthenticationFilter();
		authFilter.setAuthenticationManager(authenticationManager());
		authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/persons/login", "POST"));
		authFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
		authFilter.setUsernameParameter("username");
		authFilter.setPasswordParameter("password");
		return authFilter;
	}

	@Bean
	public ProviderManager authenticationManager() {
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

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider);
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//.formLogin().and() // .formLogin()// .defaultSuccessUrl("/resource").and()
				//.logout().and()
				.authorizeRequests()
				//.antMatchers("/", "/app/**", "/index.html", "/home", "/login", "/data", "/data/**", "/access", "/logout", "/util/**").permitAll()
				//.antMatchers("/admin/api/**").hasAnyAuthority("ADMIN", "SUPERUSER")
				.antMatchers("/**").permitAll().anyRequest().authenticated()
				.and()
				.csrf().disable().exceptionHandling()
				.and()
				.logout().logoutUrl("/j_spring_security_logout")
				.and()
				.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());


		http.addFilterBefore(new AnonymousAuthenticationFilter("anonymousUser"), AnonymousAuthenticationFilter.class);
		http.addFilterBefore(userPassAuthenticationFilter(), UserPassAuthenticationFilter.class);
	}

}