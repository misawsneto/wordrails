package co.xarx.trix.services.security;

import co.xarx.trix.domain.social.SocialUser;

import java.io.IOException;

public interface OAuthAuthenticator {
	boolean login(String userId, String appId, String appSecret, String accessToken);

	SocialUser oauth(String userId, String appId, String appSecret, String accessToken) throws IOException;
}
