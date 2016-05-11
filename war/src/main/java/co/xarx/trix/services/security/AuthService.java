package co.xarx.trix.services.security;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserConnection;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserConnectionRepository;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthService {

	private PersonRepository personRepository;
	private UserConnectionRepository userConnectionRepository;
	private SocialAuthenticationService socialAuthenticationService;

	@Autowired
	public AuthService(PersonRepository personRepository, UserConnectionRepository
			userConnectionRepository, SocialAuthenticationService socialAuthenticationService) {
		this.personRepository = personRepository;
		this.userConnectionRepository = userConnectionRepository;
		this.socialAuthenticationService = socialAuthenticationService;
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() instanceof String) {
			User user = new User();
			user.username = "wordrails";
			user.password = "wordrails";
			return user;
		}

		return (User) auth.getPrincipal();
	}

	public String getLoggedUsername() {
		User user = getUser();

		if(user == null || user.getUsername().equals("wordrails")) {
			throw new AuthenticationCredentialsNotFoundException("Session has anonymous user");
		}

		return user.getUsername();
	}

	public boolean isAnonymous() {
		return getUser().isAnonymous();
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

			return person;
		}

		person = personRepository.findByUsername(user.getUsername());

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return person;
	}

	public void updateLoggedPerson(User user) {
		logout();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public Authentication passwordAuthentication(User user, String password) throws BadCredentialsException {
		if (user == null) {
			throw new BadCredentialsException("Wrong username");
		} else if(user.password.equals(password)) {
			Authentication auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);

			return auth;
		}

		throw new BadCredentialsException("Wrong password");
	}

	public boolean socialAuthentication(String providerId, OAuthService service, String userId, Token token) throws BadCredentialsException, IOException {
		UserConnection userConnection = userConnectionRepository.findByProviderIdAndProviderUserId(providerId, userId);

		User user;
		if (userConnection == null) {
			SocialUser socialUser = null;
			if (providerId.equals("facebook")) {
				socialUser = socialAuthenticationService.getFacebookUserFromOAuth(userId, service, token);
			} else if (providerId.contains("google")) {
				socialUser = socialAuthenticationService.getGoogleUserFromOAuth(userId, token.getToken());
			}

			Person person = socialAuthenticationService.getPersonFromSocialUser(socialUser);
			personRepository.save(person);

			user = person.user;
		} else {
			if (providerId.equals("facebook")) {
				if (!socialAuthenticationService.facebookLogin(userId, service, token)) {
					return false;
				}
			} else if (providerId.equals("google")) {
				if (!socialAuthenticationService.googleLogin(userId, token.getToken())) {
					return false;
				}
			}

			user = userConnection.user;
		}

		if (user == null) return false;

		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);

		return true;
	}

	public void logout() {
		if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}
}
