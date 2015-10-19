package com.wordrails.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.business.*;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.Normalizer;
import java.util.HashSet;

@Service
public class SocialAuthenticationService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;

//	public Person getFacebookUser(Facebook facebook, String userId, Network network) {
//		org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile(userId);
//
//		String email = profile.getEmail() == null ? "" : profile.getEmail();
//		Person person = personRepository.findByEmailAndNetworkId(email, network.id);
//
//		if (person == null) {
//			int i = 1;
//			String originalUsername = profile.getFirstName().toLowerCase() + profile.getLastName().toLowerCase();
//			String username = Normalizer
//					.normalize(originalUsername, Normalizer.Form.NFD)
//					.replaceAll("[^\\p{ASCII}]", "");
//			while (userRepository.existsByUsernameAndNetworkId(username, network.id)) {
//				username = originalUsername + i++;
//			}
//
//			person = new Person();
//			person.name = profile.getName();
//			person.username = username;
//			person.email = email;
//		}
//
//
//		User user = new User();
//
//		UserGrantedAuthority authority = new UserGrantedAuthority("ROLE_USER");
//		authority.network = network;
//
//		user.enabled = true;
//		user.username = person.username;
//		user.password = "";
//		user.network = network;
//		authority.user = user;
//		user.addAuthority(authority);
//
//		UserConnection userConnection = new UserConnection();
//		userConnection.providerId = "facebook";
//		userConnection.providerUserId = userId;
//		userConnection.email = profile.getEmail();
//		userConnection.user = user;
//		userConnection.displayName = profile.getName();
//		userConnection.profileUrl = profile.getLink();
//		userConnection.imageUrl = fetchPictureUrl(userId, ImageType.LARGE);
//		user.userConnections = new HashSet<>();
//		user.userConnections.add(userConnection);
//
//		person.user = user;
//		person.coverUrl = profile.getCover().getSource();
//		person.imageUrl = userConnection.imageUrl;
//
//		return person;
//	}

	public Person getFacebookUser(FacebookUser facebookUser, Network network) {
		String email = facebookUser.getEmail() == null ? "" : facebookUser.getEmail();
		Person person = personRepository.findByEmailAndNetworkId(email, network.id);

		if (person == null) {
			int i = 1;
			String originalUsername = facebookUser.getName().toLowerCase().replace(" ", "");
			String username = Normalizer
					.normalize(originalUsername, Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", "");
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
		userConnection.imageUrl = fetchPictureUrl(facebookUser.getId(), ImageType.LARGE);
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;
		person.coverUrl = facebookUser.getCover().getSource();
		person.imageUrl = userConnection.imageUrl;

		return person;
	}

	public String fetchPictureUrl(String userId, ImageType imageType) {
		URI uri = URIBuilder.fromUri("http://graph.facebook.com/" + userId + "/picture" +
				"?type=" + imageType.toString().toLowerCase() + "&redirect=false").build();

		RestTemplate restTemplate = new RestTemplate();
		JsonNode response = restTemplate.getForObject(uri, JsonNode.class);
		return response.get("data").get("url").textValue();
	}
}
