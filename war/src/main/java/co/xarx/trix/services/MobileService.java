package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.AppleCertificateRepository;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Blob;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MobileService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;
	@Value("${trix.apns.default-password}")
	private String defaultApnsPassword;

	private AppleCertificateRepository certificateRepository;
	private MobileDeviceRepository mobileDeviceRepository;
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public MobileService(AppleCertificateRepository certificateRepository, MobileDeviceRepository mobileDeviceRepository, NetworkRepository networkRepository) {
		this.certificateRepository = certificateRepository;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.networkRepository = networkRepository;
	}

	public Collection<String> getDeviceCodes(List<MobileDevice> mobileDevices, Constants.MobilePlatform type) {
		Set<String> deviceCodes = new HashSet<>();
		mobileDevices.stream().forEach(device -> {
			if (type.equals(device.type)) {
				deviceCodes.add(device.deviceCode);
			}
		});

		return deviceCodes;
	}

	public void updateDevice(Integer personId, String deviceCode, Double lat, Double lng, Constants.MobilePlatform
			type) {
		Assert.hasText(deviceCode, "Device code must not be empty");

		MobileDevice device = mobileDeviceRepository.findOne(QMobileDevice.mobileDevice.deviceCode.eq(deviceCode));

		Person person = null;
		if (personId != 0) {
			person = entityManager.getReference(Person.class, personId);
		}
		device = getMobileDevice(person, deviceCode, lat, lng, type, device);

		try {
			mobileDeviceRepository.save(device);
		} catch (ObjectOptimisticLockingFailureException ignored) {
		}
	}

	public void updateAppleCertificateFile(Blob certificate) {
		Assert.notNull(certificate);

		AppleCertificate appleCertificate = getAppleCertificate();

		if (appleCertificate == null) {
			appleCertificate = new AppleCertificate();
			Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
			appleCertificate.setPassword(defaultApnsPassword);
			appleCertificate.setNetwork(network);
		}

		appleCertificate.setFile(certificate);

		certificateRepository.save(appleCertificate);
	}

	public void updateApplePassword(String password) throws Exception {
		Assert.notNull(password);

		AppleCertificate appleCertificate = getAppleCertificate();
		if (appleCertificate == null) {
			throw new Exception("Certificate does not exist. Upload a file first");
		}
		appleCertificate.setPassword(password);

		certificateRepository.save(appleCertificate);
	}

	private AppleCertificate getAppleCertificate() {
		String tenant = TenantContextHolder.getCurrentTenantId();
		AppleCertificate appleCertificate = certificateRepository.findByTenantId(tenant);

		return appleCertificate;
	}

	public MobileDevice getMobileDevice(Person person, String deviceCode,
										Double lat, Double lng, Constants.MobilePlatform type, MobileDevice device) {
		if (person != null && person.id == 0) {
			person = null;
		}

		if (device == null) {
			device = new MobileDevice();

			device.deviceCode = deviceCode;
			device.person = person;
			device.active = true;
			device.type = type;
		}

		device.person = person;
		if(person != null)
			device.lastPersonLogged = person;
		if (lat != null) device.lat = lat;
		if (lng != null) device.lng = lng;

		return device;
	}
}
