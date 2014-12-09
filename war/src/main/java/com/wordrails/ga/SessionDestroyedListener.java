package com.wordrails.ga;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent> {
	private @Autowired GoogleAnalytics analytics;

	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		HttpSession session = (HttpSession) event.getSource();
		String userIp = (String) session.getAttribute(AuthenticationSuccessListener.USER_IP);
		if (userIp != null) {
			for (SecurityContext context : event.getSecurityContexts()) {
				String username = context.getAuthentication().getName();
				analytics.sessionEnded(username, userIp);
			}
		}
	}
}