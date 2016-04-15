package co.xarx.trix.web.rest;

import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.services.PasswordService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.api.AuthApi;
import co.xarx.trix.web.rest.resource.AuthResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AuthResourceTest {

	private static InMemoryRestServer server;

	@BeforeClass
	public static void beforeClass() throws Exception {
		NetworkRepository networkRepository = mock(NetworkRepository.class);
		AuthService authService = new AuthService(null, null, null);
		PasswordService passwordService = mock(PasswordService.class);

		AuthApi resource = new AuthResource(networkRepository, authService, passwordService);
		server = InMemoryRestServer.create(resource);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		server.close();
	}

	@Test
	public void postSimpleBody() throws Exception {
		Form form = new Form("email", "arthur.hvt@gmail.com");
		Entity<Form> formEntity = Entity.form(form);

		Response response = server
				.newRequest("/auth/forgotPassword").request()
				.accept(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.buildPost(formEntity).invoke();

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
}
