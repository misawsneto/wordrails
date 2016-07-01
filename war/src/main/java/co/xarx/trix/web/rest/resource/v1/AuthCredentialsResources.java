package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AuthCredentialsApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthCredentialsResources extends AbstractResource implements AuthCredentialsApi{

	@Override
	public void findAuthCredentialByTenantId() throws IOException {
		forward();
	}

	@Override
	public void putAuthCredentials(Integer id) throws ServletException, IOException {
		forward();
	}
}
