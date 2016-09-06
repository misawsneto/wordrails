package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.persistence.AppleCertificateRepository;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class APNSClient implements NotificationServerClient {

	private ApnsService service;
	private AppleCertificateRepository appleCertificateRepository;
	private String apnsProfile;

	@Autowired
	public APNSClient(AppleCertificateRepository appleCertificateRepository, @Value("${apns.profile:prod}") String apnsProfile) throws IOException {
		this.appleCertificateRepository = appleCertificateRepository;
		this.apnsProfile = apnsProfile;
	}

	@Override
	public void send(NotificationView notification, Collection<String> devices) throws IOException {
		String payload = APNS.newPayload()
				.badge(1)
				.sound("default")
				.alertBody(notification.message)
				.alertTitle(notification.post.stationName)
				.customField("stationName", notification.post.stationName)
				.customField("postId", notification.postId)
				.forNewsstand()
				.shrinkBody("...")
				.instantDeliveryOrSilentNotification()
				.build();

		Collection<? extends ApnsNotification> push = service.push(devices, payload);
		Logger.debug(push != null && push.size() > 0 ? push.toString() : "");
	}

	@Override
	public void start() {
		try {
			initService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		service.stop();
	}

	@Override
	public Map<String, NotificationResult> getErrorDevices(){
		Map<String, NotificationResult> errorDevices = new HashMap<>();
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String s : inactiveDevices.keySet()) {
			NotificationResult r = new NotificationResult();
			r.setStatus(MobileNotification.Status.SERVER_ERROR);
			r.setErrorMessage("Device deactivated");
			r.setDeviceDeactivated(true);
			errorDevices.put(s, r);
		}
		return errorDevices;
	}

	public void initService() throws IOException {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		AppleCertificate certificate = appleCertificateRepository.findByTenantId(tenantId);
		Assert.isNotNull(certificate, "Apple certificate not found");

		InputStream is;
		String password;

		try {
			is = certificate.file.getBinaryStream();
			password = certificate.password;
		} catch (SQLException e) {
			throw new IOException(e);
		}

		Assert.isNotNull(password, "Password must not be null");
		Assert.isNotNull(is, "Certificate must not be null");

		if (apnsProfile.equals("dev")){
			service = APNS
					.newService()
					.withCert(is, password)
					.withSandboxDestination()
					.asPool(1)
					.build();
		} else {
			service = APNS
					.newService()
					.withCert(is, password)
					.withProductionDestination()
					.asPool(3)
					.build();
		}
	}
}
