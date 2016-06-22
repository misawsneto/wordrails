package co.xarx.trix.services.security;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@IntegrationTestBean
@Component
public class AuthService {

	private PersonRepository personRepository;

	@Autowired
	public AuthService(PersonRepository personRepository) {
		this.personRepository = personRepository;
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

	public Sid getCurrentSid() {
		User user = getUser();
		if (user.isAnonymous()) {
			return new GrantedAuthoritySid("ROLE_ANONYMOUS");
		} else {
			return new PrincipalSid(user.getUsername());
		}
	}

	public void updateLoggedPerson(User user) {
		logout();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, user.password, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void logout() {
		if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}
}
