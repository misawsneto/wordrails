package co.xarx.trix.web.filter;


import co.xarx.trix.WordrailsService;
import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.Network;
import org.springframework.beans.factory.annotation.Autowired;
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
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));
		return authProvider.anonymousAuthentication(network);
	}
}