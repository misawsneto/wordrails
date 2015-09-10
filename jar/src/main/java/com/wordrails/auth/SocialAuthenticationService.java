package com.wordrails.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.support.URIBuilder;
//import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;

@Service
public class SocialAuthenticationService {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ImageEventHandler imageEventHandler;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

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

		String email = profile.getEmail() == null ? userId + "@facebook.com" : profile.getEmail();
		Person person = personRepository.findByEmailAndNetworkId(email, network.id);

		if (person == null) {
			int i = 1;
			String originalUsername = profile.getFirstName().toLowerCase() + profile.getLastName().toLowerCase();
			String username = originalUsername;
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
		userConnection.email = email;
		userConnection.user = user;
		userConnection.displayName = profile.getName();
		userConnection.profileUrl = profile.getLink();
		userConnection.imageUrl = fetchPictureUrl(userId, ImageType.LARGE);
		user.userConnections = new HashSet<>();
		user.userConnections.add(userConnection);

		person.user = user;

		if(person.image == null) {
			try {
				Image coverPicture = getImageFromBytes(profile.getCover().getSource(), Image.Type.COVER);
				imageEventHandler.handleBeforeCreate(coverPicture);
				imageRepository.save(coverPicture);
				person.cover = coverPicture;
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

			try {
				Image profilePicture = getImageFromBytes(facebook.userOperations().getUserProfileImage(ImageType.LARGE),
						userConnection.imageUrl, Image.Type.PROFILE_PICTURE);
				imageEventHandler.handleBeforeCreate(profilePicture);
				imageRepository.save(profilePicture);
				person.image = profilePicture;
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}

		return person;
	}

	private Image getImageFromBytes(String imageUrl, Image.Type type) throws IOException {
		URL imageURL = new URL(imageUrl);
		BufferedImage originalImage= ImageIO.read(imageURL);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos );
		byte[] bytes = baos.toByteArray();

		return getImageFromBytes(bytes, imageUrl, type);
	}

	private Image getImageFromBytes(byte[] bytes, String imageUrl, Image.Type type) {
		Image image = new Image();
		image.type = type.toString();
		File file = new File();
		file.type = File.INTERNAL_FILE;
		file.url = imageUrl;

		fileRepository.save(file);
		LobCreator creator = Hibernate.getLobCreator((Session) manager.getDelegate());
		FileContents contents = fileContentsRepository.findOne(file.id);
		contents.contents = creator.createBlob(bytes);
		image.original = file;
		fileContentsRepository.save(contents);

		return image;
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
