package com.wordrails.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import com.wordrails.services.FileService;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.Normalizer;
import java.util.HashSet;

@Service
public class SocialAuthenticationService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private FileService fileService;

//	public Person getTwitterUser(Twitter twitter, String userId, Network network) {
//		org.springframework.social.twitter.api.TwitterProfile profile = twitter.userOperations().getUserProfile(userId);
//
//		String email = userId + "@twitter.com";
//		Person person = personRepository.findByEmailAndNetworkId(email, network.id);
//
//		if (person == null) {
//			int i = 1;
//			String originalUsername = profile.getName().toLowerCase();
//			String username = originalUsername;
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
//		userConnection.email = email;
//		userConnection.user = user;
//		userConnection.displayName = profile.getName();
//		userConnection.profileUrl = profile.getProfileUrl();
//		userConnection.imageUrl = profile.getProfileImageUrl();
//		user.userConnections = new HashSet<>();
//		user.userConnections.add(userConnection);
//
//		person.user = user;
//
//		if(person.image == null) {
//			try {
//				Image coverPicture = getImageFromBytes(profile.getBackgroundImageUrl(), Image.Type.COVER);
//				imageEventHandler.handleBeforeCreate(coverPicture);
//				imageRepository.save(coverPicture);
//				person.cover = coverPicture;
//			} catch (IOException | SQLException e) {
//				e.printStackTrace();
//			}
//
//			try {
//				Image profilePicture = getImageFromBytes(profile.getProfileImageUrl(), Image.Type.PROFILE_PICTURE);
//				imageEventHandler.handleBeforeCreate(profilePicture);
//				imageRepository.save(profilePicture);
//				person.image = profilePicture;
//			} catch (IOException | SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return person;
//	}

	public Person getFacebookUser(Facebook facebook, String userId, Network network) {
		org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile(userId);

		String email = profile.getEmail() == null ? "" : profile.getEmail();
		Person person = personRepository.findByEmailAndNetworkId(email, network.id);

		if (person == null) {
			int i = 1;
			String originalUsername = profile.getFirstName().toLowerCase() + profile.getLastName().toLowerCase();
			String username = Normalizer
					.normalize(originalUsername, Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", "");
			while (userRepository.existsByUsernameAndNetworkId(username, network.id)) {
				username = originalUsername + i++;
			}

			person = new Person();
			person.name = profile.getName();
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
		userConnection.providerUserId = userId;
		userConnection.email = profile.getEmail();
		userConnection.user = user;
		userConnection.displayName = profile.getName();
		userConnection.profileUrl = profile.getLink();
		userConnection.imageUrl = fetchPictureUrl(userId, ImageType.LARGE);
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;

		if (person.image == null) {
			try {
				String fileHash = fileService.newFile(profile.getCover().getSource(), "image/pgn", network.domain);

				Image coverPicture = new Image();
				coverPicture.type = Image.Type.COVER.toString();
				coverPicture.originalHash = fileHash;

				imageRepository.save(coverPicture);
				person.cover = coverPicture;
			} catch (IOException | FileUploadException e) {
				e.printStackTrace();
			}

			try {
				InputStream is = new ByteArrayInputStream(facebook.userOperations().getUserProfileImage(ImageType.LARGE));
				String fileHash = fileService.newFile(is, "image/pgn", network.domain);

				Image profilePicture = new Image();
				profilePicture.type = Image.Type.PROFILE_PICTURE.toString();
				profilePicture.originalHash = fileHash;

				imageRepository.save(profilePicture);
				person.image = profilePicture;
			} catch (IOException | FileUploadException e) {
				e.printStackTrace();
			}
		}

		return person;
	}

	private static final String GRAPH_API_URL = "http://graph.facebook.com/";

	public String fetchPictureUrl(String userId, ImageType imageType) {
		URI uri = URIBuilder.fromUri(GRAPH_API_URL + userId + "/picture" +
				"?type=" + imageType.toString().toLowerCase() + "&redirect=false").build();

		RestTemplate restTemplate = new RestTemplate();
		JsonNode response = restTemplate.getForObject(uri, JsonNode.class);
		return response.get("data").get("url").textValue();
	}
}
