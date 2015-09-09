package com.wordrails.auth;

import com.wordrails.business.User;
import com.wordrails.business.UserConnection;
import com.wordrails.persistence.UserConnectionRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.concurrent.ExecutionException;

/**
 * This is called after a social login happens and we find an user already signed up to that account
 */
@Component
public class TrixSignInAdapter implements SignInAdapter {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private UserRepository userRepository;

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		User user;
		try {
			user = cacheService.getUser(Integer.valueOf(userId));
		} catch (ExecutionException e) {
			user = userRepository.findOne(Integer.valueOf(userId));
		}


		Authentication auth = new SocialAuthenticationToken(connection, user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return null;
	}
}
