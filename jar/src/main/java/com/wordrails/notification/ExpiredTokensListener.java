package com.wordrails.notification;

import com.relayrides.pushy.apns.ExpiredToken;
import com.relayrides.pushy.apns.ExpiredTokenListener;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import com.wordrails.persistence.PersonNetworkTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Created by jonas on 06/08/15.
 */

public class ExpiredTokensListener implements ExpiredTokenListener<SimpleApnsPushNotification> {

	@Autowired private PersonNetworkTokenRepository personNetworkTokenRepository;

	public void handleExpiredTokens(final PushManager<? extends SimpleApnsPushNotification> pushManager,
			final Collection<ExpiredToken> expiredTokens) {

		System.err.println("Problem with tokens: #" + expiredTokens.size());

		for (final ExpiredToken expiredToken : expiredTokens) {
			System.err.println("Problem during push notification");
//			System.out.println(expiredToken.getExpiration().toString());
			personNetworkTokenRepository.deleteByToken(TokenUtil.tokenBytesToString(expiredToken.getToken()));
		}
	}
}