package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.social.FacebookUser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@IntegrationTestBean
public class FacebookAuthenticator implements OAuthAuthenticator {


	public boolean login(String userId, String appId, String appSecret, String accessToken) throws BadCredentialsException {
		try {
			oauth(userId, appId, appSecret, accessToken);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public FacebookUser oauth(String userId, String appId, String appSecret, String accessToken) throws IOException, BadCredentialsException {
		Response response = oauthRequest(userId, appId, appSecret, accessToken);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		FacebookUser fbUser = mapper.readValue(response.getBody(), FacebookUser.class);
		fbUser.setProviderId("facebook");
		fbUser.setProfileUrl("http://facebook.com/" + fbUser.getId());
		fbUser.setProfileImageUrl("https://graph.facebook.com/" + fbUser.getId() + "/picture?type=large");

		if(fbUser.getEmail() == null)
			throw new BadCredentialsException("Request did not return email");

		return fbUser;
	}

	private Response oauthRequest(String userId, String appId, String appSecret, String accessToken) throws BadCredentialsException {
		OAuthService service;
		Token token;
		try {
			service = new ServiceBuilder()
					.provider(FacebookApi.class)
					.apiKey(appId)
					.apiSecret(appSecret)
					.build();
			token = new Token(accessToken, appSecret);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid app id and/or app secret for facebook login");
		}

		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.6/" + userId + "?fields=id," +
				"name,email,cover,picture.type(large)");
		service.signRequest(token, request);
		Response response = request.send();
		System.out.println(response.getBody());
		return response;
	}
}
