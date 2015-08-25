package com.wordrails.filter;


import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Network;
import com.wordrails.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class TrixAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

	@Autowired
	private WordrailsService wordrailsService;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	public TrixAnonymousAuthenticationFilter(String key) {
		super(key);
	}

	public Authentication createAuthentication(HttpServletRequest request) {
		Network network = wordrailsService.getNetworkFromHost(request);
		return authProvider.anonymousAuthentication(network);
	}
}