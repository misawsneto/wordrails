package co.xarx.trix.web.rest;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.DBAndroidApp;
import co.xarx.trix.persistence.DBAndroidAppRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.AndroidBuilderService;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/builder")
@Component
public class BuilderResource {

	@Autowired
	private DBAndroidAppRepository dbAndroidAppRepository;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	public AndroidBuilderService androidBuilderService;

	@GET
	@Path("/generateApk")
	public Response getApk(@Context HttpServletRequest request) {
		DBAndroidApp androidApp = dbAndroidAppRepository.findAll().get(0);

		asyncService.run(TenantContextHolder.getCurrentTenantId(), () -> {
			try {
				File apk = androidBuilderService.run("/opt/trix_android", androidApp);
				String hash = amazonCloudService.uploadAPK(apk, apk.length(), "application/vnd.android.package-archive", false);
				String fileUrl = amazonCloudService.getPublicApkURL(hash);
				androidApp.setApkUrl(fileUrl);
				dbAndroidAppRepository.save(androidApp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return Response.ok().build();
	}
}
