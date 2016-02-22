package co.xarx.trix.config.spring;

import co.xarx.trix.web.filter.UserPassAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
				"/login/**",
				"/fonts/**"
		);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler authFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}

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
				.formLogin().loginProcessingUrl("/login")
				.usernameParameter("username").passwordParameter("password")
				.failureHandler(authFailureHandler())
				.and()
				.logout().logoutUrl("/j_spring_security_logout")
				.and()
				.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());


		http.addFilterBefore(new AnonymousAuthenticationFilter("anonymousUser"), AnonymousAuthenticationFilter.class);
		http.addFilterBefore(userPassAuthenticationFilter(), UserPassAuthenticationFilter.class);
	}

}