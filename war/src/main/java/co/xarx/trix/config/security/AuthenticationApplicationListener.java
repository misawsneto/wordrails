package co.xarx.trix.config.security;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.User;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
class AuthenticationApplicationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
	AsyncService asyncService;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		String username = ((User)event.getAuthentication().getPrincipal()).getUsername();
		asyncService.updatePersonLastLoginDate(tenantId, username);
	}

}