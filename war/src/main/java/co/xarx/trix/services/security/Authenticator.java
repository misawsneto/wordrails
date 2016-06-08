package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserRepository;
import co.xarx.trix.services.person.PersonAlreadyExistsException;
import co.xarx.trix.services.person.PersonFactory;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;


@IntegrationTestBean
@Component
public class Authenticator {

	private PersonFactory personFactory;
	private PersonRepository personRepository;
	private UserRepository userRepository;
	private SocialAuthenticationService socialAuthenticationService;

	@Autowired
	public Authenticator(PersonFactory personFactory, PersonRepository personRepository, UserRepository
			userRepository, SocialAuthenticationService socialAuthenticationService) {
		this.personFactory = personFactory;
		this.personRepository = personRepository;
		this.userRepository = userRepository;
		this.socialAuthenticationService = socialAuthenticationService;
	}

	public Authentication passwordAuthentication(User user, String password) throws BadCredentialsException {
		if (user == null) {
			throw new BadCredentialsException("Wrong username");
		} else if (user.password.equals(password)) {
			Authentication auth = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);

			return auth;
		}

		throw new BadCredentialsException("Wrong password");
	}

	public boolean socialAuthentication(String providerId, OAuthService service, String userId, Token token) throws BadCredentialsException, IOException {
		User user = userRepository.findSocialUser(providerId, userId);

		if (user == null) {
			SocialUser socialUser;
			if (providerId.equals("facebook")) {
				socialUser = socialAuthenticationService.getFacebookUserFromOAuth(userId, service, token);
			} else if (providerId.contains("google")) {
				socialUser = socialAuthenticationService.getGoogleUserFromOAuth(userId, token.getToken());
			} else {
				throw new BadCredentialsException("provider ID is not valid");
			}

			Person person;
			try {
				person = personFactory.create(socialUser.getName(), socialUser.getEmail(), socialUser);
				personRepository.save(person);
			} catch (PersonAlreadyExistsException e) {
				person = personRepository.findByEmail(socialUser.getEmail());
			}

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
			} else {
				throw new BadCredentialsException("provider ID is not valid");
			}
		}

		if (user == null) return false;

		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);

		return true;
	}
}
