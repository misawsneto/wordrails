package com.wordrails.filter;

import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import com.wordrails.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class TrixAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);

		if (username.equals("wordrails")) {
			Network network = wordrailsService.getNetworkFromHost(request);
			return authProvider.anonymousAuthentication(network);
		} else {
			return authProvider.passwordAuthentication(username, password, authProvider.getNetwork());
		}
	}
}
