package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.save.SavePageRequest;
import co.xarx.trix.web.rest.InMemoryRestServer;
import co.xarx.trix.web.rest.api.v2.V2PagesApi;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class V2PagesResourceTest {

	private static InMemoryRestServer server;

	@Before
	public void setUp() throws Exception {
		V2PagesApi api = new V2PagesResource();
		server = InMemoryRestServer.create(api);
	}

	@Test
	@Ignore
	public void testCreatePage() throws Exception {
		SavePageRequest pageRequest = new SavePageRequest();
		pageRequest.setTitle("dummy title");
		Entity<SavePageRequest> json = Entity.json(pageRequest);

		Response response = server
				.newRequest("/v2/stations/20/pages").request()
				.accept(MediaType.APPLICATION_JSON)
				.buildPost(json).invoke();

		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		assertNotNull(response.getEntity());
	}
}