package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.persistence.AppleCertificateRepository;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public APNSClient(AppleCertificateRepository appleCertificateRepository) throws IOException {
		this.appleCertificateRepository = appleCertificateRepository;
	}

	@Override
	public void send(NotificationView notification, Collection<String> devices) throws IOException {
		initService();

		String payload = APNS.newPayload()
				.badge(1)
				.sound("default")
				.alertBody(notification.message)
				.alertTitle(notification.post.stationName)
				.customField("stationName", notification.post.stationName)
				.customField("postId", notification.postId)
				.forNewsstand()
				.shrinkBody("...")
				.build();

		service.push(devices, payload);
	}

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		Map<String, NotificationResult> errorDevices = new HashMap<>();
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String s : inactiveDevices.keySet()) {
			NotificationResult r = new NotificationResult();
			r.setStatus(Notification.Status.SERVER_ERROR);
			r.setErrorMessage("Device deactivated");
			r.setDeviceDeactivated(true);
			errorDevices.put(s, r);
		}
		return errorDevices;
	}

	private void initService() throws IOException {
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

		service = APNS
				.newService()
				.withCert(is, password)
				.withProductionDestination()
				.build();
	}
}