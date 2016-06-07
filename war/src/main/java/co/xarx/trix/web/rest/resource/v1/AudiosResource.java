package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.AudiosApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class AudiosResource extends AbstractResource implements AudiosApi {

	private AuthService authService;
	private MobileService mobileService;

	@Override
	public void getAudio(int audioId) throws ServletException, IOException {
		forward();
	}

	@Override
	public void findAudiosOrderByDate() throws ServletException, IOException {
		forward();
	}

	@Override
	public void postAudio() throws ServletException, IOException {
		forward();
	}
}