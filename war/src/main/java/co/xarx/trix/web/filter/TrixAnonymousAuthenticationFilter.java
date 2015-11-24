package co.xarx.trix.web.filter;


import co.xarx.trix.util.Constants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public class TrixAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {


	public TrixAnonymousAuthenticationFilter(String key) {
		super(key);
	}

	public Authentication createAuthentication(HttpServletRequest request) {
		return new AnonymousAuthenticationToken("anonymousKey",
				Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);
	}
}