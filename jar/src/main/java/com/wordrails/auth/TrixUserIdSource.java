package com.wordrails.auth;

import com.wordrails.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.stereotype.Component;

@Component("userIdSource")
public class TrixUserIdSource implements UserIdSource {

	@Autowired
	private TrixAuthenticationProvider authProvider;

	@Override
	public String getUserId() {
		User user = authProvider.getUser();
		return user.id + "_" + user.network.id;
	}
}
