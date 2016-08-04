package co.xarx.trix.config.security;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
class AuthenticationApplicationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
	PersonRepository personRepository;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		String username = ((User)event.getAuthentication().getPrincipal()).getUsername();
		Person person = personRepository.findByUsernameAndTenantId(username, tenantId);
		if (person != null) {
			person.lastLogin = new Date();
			personRepository.save(person);
		}
	}

}