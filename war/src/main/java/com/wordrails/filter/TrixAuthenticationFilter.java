package com.wordrails.filter;

import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrixAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);

		Network network = wordrailsService.getNetworkFromHost(request);
		if (username.equals("wordrails")) {
			return authProvider.anonymousAuthentication(network);
		} else {
			return authProvider.passwordAuthentication(username, password, network);
		}
	}
}
