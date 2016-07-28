package co.xarx.trix.web.filter;


import co.xarx.trix.util.Constants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class AnonymousAuthenticationFilter extends org.springframework.security.web.authentication.AnonymousAuthenticationFilter {


	public AnonymousAuthenticationFilter(String key) {
		super(key);
	}

	public Authentication createAuthentication(HttpServletRequest request) {
		return new AnonymousAuthenticationToken("anonymousKey",
				Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);
	}
}