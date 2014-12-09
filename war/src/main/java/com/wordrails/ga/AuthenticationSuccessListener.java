package com.wordrails.ga;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
	static final String USER_IP =  AuthenticationSuccessListener.class.getName() + ".USER_IP";
	
	private @Autowired HttpServletRequest request;
	private @Autowired GoogleAnalytics analytics;	
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String userIp = request.getRemoteAddr();
		HttpSession session = request.getSession();
		session.setAttribute(USER_IP, userIp);

		String username = event.getAuthentication().getName();
		analytics.sessionStarted(username, userIp);
	}
}