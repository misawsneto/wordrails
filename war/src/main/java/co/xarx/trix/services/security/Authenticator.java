package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserRepository;
import co.xarx.trix.services.person.PersonFactory;
import co.xarx.trix.services.user.UserAlreadyExistsException;
import co.xarx.trix.services.user.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@IntegrationTestBean
@Component
public class Authenticator {

	private UserFactory userFactory;
	private PersonFactory personFactory;

	private PersonRepository personRepository;
	private UserRepository userRepository;
	private Map<String, OAuthAuthenticator> authenticators;

	@Autowired
	public Authenticator(UserRepository userRepository, GoogleAuthenticator googleAuthenticator,
						 FacebookAuthenticator facebookAuthenticator, UserFactory userFactory, PersonFactory personFactory, PersonRepository personRepository) {
		this.userRepository = userRepository;
		this.userFactory = userFactory;
		this.personFactory = personFactory;
		this.personRepository = personRepository;
		authenticators = new HashMap<>();
		authenticators.put("google", googleAuthenticator);
		authenticators.put("facebook", facebookAuthenticator);
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

	public boolean socialAuthentication(String providerId, String userId, String appId, String appSecret, String accessToken) throws BadCredentialsException, IOException {
		User user = userRepository.findSocialUser(providerId, userId);

		if (user == null) {
			OAuthAuthenticator authenticator = getAuthenticator(providerId);
			SocialUser socialUser = authenticator.oauth(userId, appId, appSecret, accessToken);

			Person person = personRepository.findByEmail(socialUser.getEmail());

			if(person != null) {
				userFactory.createConnection(person.getUser(), socialUser);
				user = person.getUser();
			} else {
				try {
					user = userFactory.create(socialUser);
					try {
						person = personFactory.create(socialUser.getName(), socialUser.getEmail(), user);
					} catch (Exception e) {
						userFactory.delete(user.username);
						throw e;
					}

				} catch (UserAlreadyExistsException e) {
					user = userRepository.findUserByEmail(socialUser.getEmail());
				}
			}
		} else {
			OAuthAuthenticator authenticator = getAuthenticator(providerId);

			boolean loginSuccess = authenticator.login(userId, appId, appSecret, accessToken);
			if (!loginSuccess) {
				return false;
			}
		}

		if (user == null)
			return false;

		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);

		return true;
	}

	private OAuthAuthenticator getAuthenticator(String providerId) {
		OAuthAuthenticator authenticator = authenticators.get(providerId);
		if (authenticator == null) {
			throw new BadCredentialsException("provider ID is not valid");
		}
		return authenticator;
	}
}
