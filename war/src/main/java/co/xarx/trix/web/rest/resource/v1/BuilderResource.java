package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.JpaAndroidApp;
import co.xarx.trix.persistence.JpaAndroidAppRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.AndroidBuilderService;
import co.xarx.trix.services.AsyncService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.BuilderApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.File;

@Component
@NoArgsConstructor
public class BuilderResource extends AbstractResource implements BuilderApi {

	private JpaAndroidAppRepository jpaAndroidAppRepository;
	private AsyncService asyncService;
	private AmazonCloudService amazonCloudService;
	private AndroidBuilderService androidBuilderService;



	@Autowired
	public BuilderResource(JpaAndroidAppRepository jpaAndroidAppRepository, AsyncService asyncService,
						   AmazonCloudService amazonCloudService, AndroidBuilderService androidBuilderService) {
		this.jpaAndroidAppRepository = jpaAndroidAppRepository;
		this.asyncService = asyncService;
		this.amazonCloudService = amazonCloudService;
		this.androidBuilderService = androidBuilderService;
	}

	@Override
	public Response getApk() {
		JpaAndroidApp androidApp = jpaAndroidAppRepository.findAll().get(0);

		asyncService.run(TenantContextHolder.getCurrentTenantId(), () -> {
			try {
				File apk = androidBuilderService.run("/opt/trix_android", androidApp);
				String hash = amazonCloudService.uploadAPK(apk, apk.length(), "application/vnd.android.package-archive", false);
				String fileUrl = amazonCloudService.getPublicApkURL(hash);
				androidApp.setApkUrl(fileUrl);
				jpaAndroidAppRepository.save(androidApp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return Response.ok().build();
	}
}
