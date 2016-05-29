package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.AuthCredentialDto;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AuthCredentialsApi;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthCredentialsResource extends AbstractResource implements AuthCredentialsApi {

	@Autowired
	private NetworkService networkService;

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	public void updateAuthCredentials(AuthCredentialDto authCredential) throws ServletException, IOException {
		networkService.updateAuthCredentials(authCredential);
	}

	@Override
	public AuthCredential getAuthCredentials() throws ServletException, IOException {
		AuthCredential authCredential = networkService.getAuthCredentials();
		if(authCredential != null)
			authCredential.network = null;
		return authCredential;
	}
}