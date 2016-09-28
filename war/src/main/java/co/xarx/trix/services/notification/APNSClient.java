package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.persistence.AppleCertificateRepository;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import lombok.Getter;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

@Component
@Getter
public class APNSClient implements NotificationServerClient {

	private String apnsProfile;
	private ApnsService service;
	private Map<String, NotificationResult> errorDevices;
	private Map<String, NotificationResult> successDevices;
	private AppleCertificateRepository appleCertificateRepository;

	@Autowired
	public APNSClient(AppleCertificateRepository appleCertificateRepository, @Value("${apns.profile:prod}") String apnsProfile) throws IOException {
		this.apnsProfile = apnsProfile;
		errorDevices = new HashMap<>();
		successDevices = new HashMap<>();
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
				.instantDeliveryOrSilentNotification()
				.build();

		service.push(devices, payload);
		extractErrorDevices();
		extractSuccessDevices(devices);
	}

	public void extractSuccessDevices(Collection<String> devices) {
		for(String device: devices){
			NotificationResult result = new NotificationResult();
			result.setStatus(MobileNotification.Status.SUCCESS);
			successDevices.put(device, result);
		}
	}

	public void extractErrorDevices() {
		Map<String, Date> inactiveDevices = service.getInactiveDevices();
		for (String s : inactiveDevices.keySet()) {
			NotificationResult r = new NotificationResult();
			r.setStatus(MobileNotification.Status.SERVER_ERROR);
			r.setErrorMessage("Device deactivated");
			r.setDeviceDeactivated(true);
			errorDevices.put(s, r);
		}
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

		if (apnsProfile.equals("dev")){
			service = APNS
					.newService()
					.withCert(is, password)
					.withSandboxDestination()
					.build();
		} else {
			service = APNS
					.newService()
					.withCert(is, password)
					.withProductionDestination()
					.build();
		}
	}
}
