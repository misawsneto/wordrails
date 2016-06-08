package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.social.FacebookUser;
import co.xarx.trix.domain.social.GoogleUser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.MapUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@IntegrationTestBean
public class SocialAuthenticationService {

	boolean facebookLogin(String userId, OAuthService service, Token token) {
		try {
			getFacebookUserFromOAuth(userId, service, token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	boolean googleLogin(String userId, String accessToken) {
		try {
			getGoogleUserFromOAuth(userId, accessToken);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	GoogleUser getGoogleUserFromOAuth(String userId, String accessToken) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/" + userId + "?fields=cover/coverPhoto/url,emails,id,name,image");
		request.addHeader("Authorization", "Bearer " + accessToken);
		System.out.println(MapUtils.toString(request.getOauthParameters()));
		System.out.println(MapUtils.toString(request.getHeaders()));
		Response response = request.send();
		System.out.println(response.getBody());

		if(!response.isSuccessful())
			throw new IOException("ERROR: " + response.getCode() + " - " + response.getMessage());

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(response.getBody());

		GoogleUser googleUser = new GoogleUser();

		googleUser.setProviderId("google");
		googleUser.setId(rootNode.get("id").asText());
		googleUser.setProfileUrl("http://plus.google.com/" + googleUser.getId());
		googleUser.setName(rootNode.get("name").get("givenName").asText() + " " + rootNode.get("name").get("familyName").asText());
		try {
			googleUser.setCoverUrl(rootNode.get("cover").get("coverPhoto").get("url").asText());
		} catch (NullPointerException e) {
			log.debug("user " + googleUser.getId() + " doesnt have cover");
		}

		try {
			googleUser.setProfileImageUrl(rootNode.get("image").get("url").asText());
			//image comes in size 50px. replace the url to show the default sized image
			if (googleUser.getProfileImageUrl().contains("?sz=50")) {
				googleUser.setProfileImageUrl(googleUser.getProfileImageUrl().replace("?sz=50", ""));
			}
		} catch (NullPointerException e) {
			log.debug("user " + googleUser.getId() + " doesnt have profile image");
		}

		for (JsonNode emailNode : rootNode.get("emails")) {
			if (emailNode.get("type").asText().equals("account")) {
				googleUser.setEmail(emailNode.get("value").asText());
			}
		}

		return googleUser;
	}

	FacebookUser getFacebookUserFromOAuth(String userId, OAuthService service, Token token) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.5/" + userId + "?fields=id,name,email,cover,picture.type(large)");
		service.signRequest(token, request);
		Response response = request.send();
		System.out.println(response.getBody());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		FacebookUser fbUser = mapper.readValue(response.getBody(), FacebookUser.class);
		fbUser.setProviderId("facebook");
		fbUser.setProfileUrl("http://facebook.com/" + fbUser.getId());
		fbUser.setProfileImageUrl("https://graph.facebook.com/" + fbUser.getId() + "/picture?type=large");
		return fbUser;
	}
}
