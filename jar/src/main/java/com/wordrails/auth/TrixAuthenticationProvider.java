package com.wordrails.auth;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.User;
import com.wordrails.business.UserConnection;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.UserConnectionRepository;
import com.wordrails.persistence.UserRepository;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

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
	@Autowired
	private SocialAuthenticationService socialAuthenticationService;

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
			person.imageHash = "";
			person.coverHash = "";
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
				Person person = socialAuthenticationService.getFacebookUser(facebook, userId, network);
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

	public boolean isLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}
}
