package com.wordrails.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import com.wordrails.services.CacheService;
import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Component;
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
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;


@Component
public class TrixAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CacheService cacheService;

	@Qualifier("userConnectionRepository")
	@Autowired
	private UserConnectionRepository userConnectionRepository;

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


	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		return auth; //won't do any validation because we ensure it's validated in both cases: social login and user/password
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.equals(authentication));
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			User user = new User();
			user.username = "wordrails";
			user.password = "wordrails";
			return user;
		}

		return (User) auth.getPrincipal();
	}

	public Network getNetwork() {
		return getUser().network;
	}

	public Integer getNetworkId() {
		if (getUser().isAnonymous() || getUser().network == null) return 0;

		return getUser().network.id;
	}

	public Person getLoggedPerson() {
		User user = getUser();

		Person person;
		if (user.isAnonymous()) { //Legacy code for old iOS and Android app
			person = new Person();
			person.id = 0;
			person.username = "wordrails";
			person.password = "wordrails";
			person.email = "";
			person.name = "";
			person.imageId = 0;
			person.coverId = 0;
			person.bookmarks = new HashSet<>();
			person.recommends = new HashSet<>();

			return person;
		}

		try {
			person = cacheService.getPersonByUsernameAndNetworkId(user.username, getNetworkId());
		} catch (ExecutionException e) {
			person = personRepository.findByUser(user);
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return person;
	}

	public boolean isLogged() {
		return !getUser().isAnonymous();
	}

	public Authentication anonymousAuthentication(Network network) {
		User user = new User();
		user.username = "wordrails";
		user.network = network;

		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

		Authentication auth = new AnonymousAuthenticationToken("anonymousKey", user, authorities);

		SecurityContextHolder.getContext().setAuthentication(auth);

		return auth;
	}

	public Authentication passwordAuthentication(String username, String password, Network network) throws BadCredentialsException {
		Set<User> users;
		try {
			users = cacheService.getUsersByUsername(username);
		} catch (ExecutionException e) {
			users = userRepository.findByUsernameAndEnabled(username, true);
		}

		if (users == null || users.isEmpty()) {
			throw new BadCredentialsException("Wrong username");
		}

		User user = null;
		for (User u : users) {
			if (u.network != null) { //find by network
				if (Objects.equals(u.network.id, network.id) && password.equals(u.password)) { //if this is the user for this network, is the password right?
					user = u;
					break;
				}
			} else if (password.equals(u.password)) { //find by password, if it enters here, the network is not set
				user = u;
				break;
			}
		}

		if (user == null) { //didn't find by password or network.
			throw new BadCredentialsException("Wrong password");
		}

		if (user.network == null || Objects.equals(user.network.id, 0)) {
			user.network = getNetwork();
			userRepository.save(user);
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);

		return auth;
	}

	public boolean socialAuthentication(String providerId, String userId, String accessToken, Network network) throws BadCredentialsException {
		UserConnection userConnection = userConnectionRepository.findByProviderIdAndProviderUserId(providerId, userId, network.id);

		User user = null;
		Facebook facebook = new FacebookTemplate(accessToken);
		if (userConnection == null) {
			if (providerId.equals("facebook")) {
				Person person = getFacebookUser(facebook, userId, network);
				personRepository.save(person);

				user = person.user;
			}
		} else {
			if (providerId.equals("facebook")) {
				org.springframework.social.facebook.api.User profile = facebook.userOperations().getUserProfile(userId);

				if (profile == null) {
					return false;
				} else {
					user = userConnection.user;
				}
			}
		}

		if (user == null) return false;

		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);

		return true;
	}

	private Person getFacebookUser(Facebook facebook, String userId, Network network) {
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
//			try {
//				Image coverPicture = getImageFromBytes(profile.getCover().getSource(), Image.Type.COVER);
//				imageEventHandler.handleBeforeCreate(coverPicture);
//				imageRepository.save(coverPicture);
//				person.cover = coverPicture;
//			} catch (IOException | SQLException e) {
//				e.printStackTrace();
//			}
//
//			try {
//				Image profilePicture = getImageFromBytes(facebook.userOperations().getUserProfileImage(ImageType.LARGE),
//						userConnection.imageUrl, Image.Type.PROFILE_PICTURE);
//				imageEventHandler.handleBeforeCreate(profilePicture);
//				imageRepository.save(profilePicture);
//				person.image = profilePicture;
//			} catch (IOException | SQLException e) {
//				e.printStackTrace();
//			}
		}

		return person;
	}

	private Image getImageFromBytes(String imageUrl, Image.Type type) throws IOException {
		URL imageURL = new URL(imageUrl);
		BufferedImage originalImage = ImageIO.read(imageURL);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);
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

	public boolean isLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}
