package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AndroidApp;
import co.xarx.trix.persistence.AndroidAppRepository;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/builder")
@Component
public class BuilderResource {

	@Autowired
	private AndroidAppRepository androidAppRepository;
	@Autowired
	private AsyncService asyncService;

	@GET
	@Path("/generateApk")
	public Response getApk(@Context HttpServletRequest request) {
		AndroidApp androidApp = androidAppRepository.findAll().get(0);

		try {
			asyncService.buildAndroidApp(TenantContextHolder.getCurrentTenantId(), "/opt/trix_android", androidApp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.ok().build();
	}
}
