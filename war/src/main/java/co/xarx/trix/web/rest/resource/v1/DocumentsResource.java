package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.DocumentsApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@NoArgsConstructor
public class DocumentsResource extends AbstractResource implements DocumentsApi {

	private AuthService authService;
	private MobileService mobileService;

	@Override
	public void getDocument(int documentId) throws ServletException, IOException {
		forward();
	}

	@Override
	public void findDocumentsOrderByDate() throws ServletException, IOException {
		forward();
	}

	@Override
	public void postDocument() throws ServletException, IOException {
		forward();
	}
}