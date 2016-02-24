package co.xarx.trix.config.spring;

import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.web.filter.TrixAnonymousAuthenticationFilter;
import co.xarx.trix.web.filter.TrixAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TrixAuthenticationProvider authenticationProvider;

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
	public TrixAuthenticationFilter trixAuthenticationFilter() throws Exception {
		TrixAuthenticationFilter authFilter = new TrixAuthenticationFilter();
		authFilter.setAuthenticationManager(authenticationManager());
		return authFilter;
	}

	@Bean
	public TrixAnonymousAuthenticationFilter trixAnonymousAuthenticationFilter(){
		return new TrixAnonymousAuthenticationFilter("anonymousKey");
	}

	@Bean
	public SimpleUrlAuthenticationFailureHandler authFailureHandler(){
		return new SimpleUrlAuthenticationFailureHandler();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//.formLogin().and() // .formLogin()// .defaultSuccessUrl("/resource").and()
				//.logout().and()
				.authorizeRequests()
				//.antMatchers("/", "/app/**", "/index.html", "/home", "/login", "/data", "/data/**", "/access", "/logout", "/util/**").permitAll()
				//.antMatchers("/admin/api/**").hasAnyAuthority("ADMIN", "SUPERUSER")
				.antMatchers("/**").permitAll()
				.anyRequest().authenticated()
				.and().csrf().disable()
				.exceptionHandling()
				.and()
				.formLogin()
				.loginProcessingUrl("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.failureHandler(authFailureHandler())
				.and()
				.logout()
				.logoutUrl("/j_spring_security_logout")
				.and()
				.sessionManagement().maximumSessions(-1).sessionRegistry(sessionRegistry());


		http.addFilterBefore(trixAnonymousAuthenticationFilter(), TrixAnonymousAuthenticationFilter.class);
		http.addFilterBefore(trixAuthenticationFilter(), TrixAuthenticationFilter.class);

	}

}