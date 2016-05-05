package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.VideosApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class VideosResource extends AbstractResource implements VideosApi {

	private AuthService authService;
	private MobileService mobileService;

	@Override
	public void getVideo(int videoId) throws ServletException, IOException {
		forward();
	}

	@Override
	public void postVideo() throws ServletException, IOException {
		forward();
	}
}