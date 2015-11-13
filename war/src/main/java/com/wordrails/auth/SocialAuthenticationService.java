package com.wordrails.auth;

import com.wordrails.domain.*;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;

@Service
public class SocialAuthenticationService {

	static Logger log = Logger.getLogger(SocialAuthenticationService.class.getName());

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;

	public boolean facebookLogin(String userId, OAuthService service, Token token) {
		try {
			getFacebookUserFromOAuth(userId, service, token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean googleLogin(String userId, String accessToken) {
		try {
			getGoogleUserFromOAuth(userId, accessToken);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public GoogleUser getGoogleUserFromOAuth(String userId, String accessToken) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/plus/v1/people/" + userId + "?fields=cover/coverPhoto/url,emails,id,name,image");
		request.addHeader("Authorization", "Bearer " + accessToken);
		System.out.println(MapUtils.toString(request.getOauthParameters()));
		System.out.println(MapUtils.toString(request.getHeaders()));
		Response response = request.send();
		System.out.println(response.getBody());

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

	private UserConnection newUserConnection(SocialUser socialUser) {
		UserConnection userConnection = new UserConnection();
		userConnection.providerId = socialUser.getProviderId();
		userConnection.providerUserId = socialUser.getId();
		userConnection.email = socialUser.getEmail();
		userConnection.displayName = socialUser.getName();
		userConnection.profileUrl = socialUser.getProfileUrl();
		userConnection.imageUrl = socialUser.getProfileImageUrl();
		return userConnection;
	}

	private User newUser(Network network, String username) {
		User user = new User();

		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = network;

		user.enabled = true;
		user.username = username;
		user.password = "";
		user.network = network;
		user.userConnections = new HashSet<>();
		authority.user = user;
		user.addAuthority(authority);

		return user;
	}

	private Person findExistingUser(SocialUser socialUser, int networkId) {
		String email = socialUser.getEmail() == null ? "" : socialUser.getEmail();
		Person person = personRepository.findByEmailAndNetworkId(email, networkId);

		if (person == null) {
			int i = 1;
			String originalUsername = socialUser.getName().toLowerCase().replace(" ", "");
			String username = Normalizer.normalize(originalUsername, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			while (userRepository.existsByUsernameAndNetworkId(username, networkId)) {
				username = originalUsername + i++;
			}

			person = new Person();
			person.name = socialUser.getName();
			person.username = username;
			person.email = email;
			person.coverUrl = socialUser.getCoverUrl();
			person.imageUrl = socialUser.getProfileImageUrl();
			person.networkId = networkId;
		}

		return person;
	}

	public FacebookUser getFacebookUserFromOAuth(String userId, OAuthService service, Token token) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.5/" + userId + "?fields=id,name,email,cover,picture.type(large)");
		service.signRequest(token, request);
		Response response = request.send();
		System.out.println(response.getBody());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		FacebookUser fbUser = mapper.readValue(response.getBody(), FacebookUser.class);
		fbUser.setProviderId("facebook");
		fbUser.setProfileUrl("http://facebook.com/" + fbUser.getId());
		fbUser.setProfileImageUrl(mapper.readTree(response.getBody()).get("picture").get("data").get("url").asText());
		return fbUser;
	}

	public Person getPersonFromSocialUser(SocialUser socialUser, Network network) throws IOException {
		Person person = findExistingUser(socialUser, network.id);
		User user;
		if (person.user == null) {
			user = newUser(network, person.username);
		} else {
			user = person.user;
		}

		UserConnection userConnection = newUserConnection(socialUser);
		userConnection.user = user;
		user.userConnections.add(userConnection);
		person.user = user;

		return person;
	}
}
