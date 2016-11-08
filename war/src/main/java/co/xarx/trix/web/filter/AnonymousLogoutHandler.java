package co.xarx.trix.web.filter;

import co.xarx.trix.util.Constants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the navigation on logout by delegating to the
 * {@link AbstractAuthenticationTargetUrlRequestHandler} base class logic.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class AnonymousLogoutHandler extends
		AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication authentication) throws IOException, ServletException {
		AnonymousAuthenticationToken anonymousKey = new AnonymousAuthenticationToken("anonymousKey",
				Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);
		SecurityContextHolder.getContext().setAuthentication(anonymousKey);
		super.handle(request, response, authentication);
	}

}