package com.wordrails.auth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.business.*;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;

@Service
public class SocialAuthenticationService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;

	public boolean login(String userId, OAuthService service, Token token) {
		try {
			getFacebookUser(userId, service, token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public FacebookUser getFacebookUser(String userId, OAuthService service, Token token) throws IOException {
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.5/" + userId + "?fields=id,name,email,cover");
		service.signRequest(token, request);
		Response response = request.send();
		System.out.println(response.getBody());

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(response.getBody(), FacebookUser.class);
	}

	public Person getFacebookUser(String userId, OAuthService service, Token token, Network network) throws IOException {
		FacebookUser facebookUser = getFacebookUser(userId, service, token);

		String email = facebookUser.getEmail() == null ? "" : facebookUser.getEmail();
		Person person = personRepository.findByEmailAndNetworkId(email, network.id);

		if (person == null) {
			int i = 1;
			String originalUsername = facebookUser.getName().toLowerCase().replace(" ", "");
			String username = Normalizer.normalize(originalUsername, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			while (userRepository.existsByUsernameAndNetworkId(username, network.id)) {
				username = originalUsername + i++;
			}

			person = new Person();
			person.name = facebookUser.getName();
			person.username = username;
			person.email = email;
		}


		User user = new User();

		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
		authority.network = network;

		user.enabled = true;
		user.username = person.username;
		user.password = "";
		user.network = network;
		authority.user = user;
		user.addAuthority(authority);

		UserConnection userConnection = new UserConnection();
		userConnection.providerId = "facebook";
		userConnection.providerUserId = facebookUser.getId();
		userConnection.email = facebookUser.getEmail();
		userConnection.user = user;
		userConnection.displayName = facebookUser.getName();
		userConnection.profileUrl = "http://facebook.com/" + facebookUser.getId();
		userConnection.imageUrl = fetchPictureUrl(facebookUser.getId());
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;
		person.coverUrl = facebookUser.getCover().getSource();
		person.imageUrl = userConnection.imageUrl;

		return person;
	}

	public String fetchPictureUrl(String userId) {
		String url = "http://graph.facebook.com/" + userId + "/picture" +
				"?type=large&redirect=false";

		JsonNode response = new RestTemplate().getForObject(url, JsonNode.class);
		return response.get("data").get("url").textValue();
	}
}
