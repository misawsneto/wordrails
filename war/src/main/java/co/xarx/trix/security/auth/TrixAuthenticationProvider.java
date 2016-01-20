package co.xarx.trix.security.auth;

import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserConnection;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserConnectionRepository;
import co.xarx.trix.persistence.UserRepository;
import co.xarx.trix.util.Constants;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;


@Component
public class TrixAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private UserRepository userRepository;
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
			person.recommends = new HashSet<>();

			return person;
		}

		person = personRepository.findByUsername(user.getUsername());

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return person;
	}

	public void updateLoggedPerson(Person person) {
		logout();
		person = personRepository.findByUsername(person.username);

		Authentication auth = new UsernamePasswordAuthenticationToken(person.user, person.user.password, person.user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public boolean isLogged() {
		return !getUser().isAnonymous();
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

	public Authentication passwordAuthentication(String username, String password) throws BadCredentialsException {
		if(username.equals("wordrails"))
			return new AnonymousAuthenticationToken("anonymousKey",
					Constants.Authentication.ANONYMOUS_USER, Constants.Authentication.ANONYMOUS_USER.authorities);

		User user = userRepository.findUserByUsername(username);

		return passwordAuthentication(user, password);
	}

	public boolean socialAuthentication(String providerId, OAuthService service, String userId, Token token) throws BadCredentialsException, IOException {
		UserConnection userConnection = userConnectionRepository.findByProviderIdAndProviderUserId(providerId, userId);

		User user;
		if (userConnection == null) {
			SocialUser socialUser = null;
			if (providerId.equals("facebook")) {
				socialUser = socialAuthenticationService.getFacebookUserFromOAuth(userId, service, token);
			} else if (providerId.equals("google")) {
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

	public boolean isLogged(Integer personId) {
		Person person = getLoggedPerson();

		return person != null && Objects.equals(personId, person.id);
	}

	public void logout() {
		if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}
}
