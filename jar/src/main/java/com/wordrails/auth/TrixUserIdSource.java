package com.wordrails.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.UserIdSource;
import org.springframework.stereotype.Component;

@Component("userIdSource")
public class TrixUserIdSource implements UserIdSource {

	@Autowired
	private TrixAuthenticationProvider authProvider;

	@Override
	public String getUserId() {
		return authProvider.getUser().id.toString();
	}
}
