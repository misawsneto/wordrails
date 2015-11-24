package co.xarx.trix.web.filter;

import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrixAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private TrixAuthenticationProvider authProvider;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = super.obtainUsername(request);
		String password = super.obtainPassword(request);

		if (username.equals("wordrails")) {
			return new AnonymousAuthenticationToken("anonymousKey",
					Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);
		} else {
			return authProvider.passwordAuthentication(username, password);
		}
	}
}
