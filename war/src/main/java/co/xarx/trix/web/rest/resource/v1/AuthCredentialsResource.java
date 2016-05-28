package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AuthCredentialsApi;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthCredentialsResource extends AbstractResource implements AuthCredentialsApi {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	@Override
	public void getAuthCredentials(int postId) throws ServletException, IOException {
		forward();
	}

	@Override
	public void putAuthCredentials(Integer id) throws ServletException, IOException {
		forward();
	}

	@Override
	public void postAuthCredentials() throws ServletException, IOException {
		forward();
	}

}