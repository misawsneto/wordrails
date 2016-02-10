package co.xarx.trix.web.filter;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserPassAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		String username = super.obtainUsername(request);

		if (username == null || username.isEmpty() || username.equals("wordrails")) {
			return new AnonymousAuthenticationToken("anonymousKey", "anonymousKey", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
		} else {
			return super.attemptAuthentication(request, response);
		}
	}
}
