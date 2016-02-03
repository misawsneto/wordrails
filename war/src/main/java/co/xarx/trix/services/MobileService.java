package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonNetworkRegId;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PersonNetworkRegIdRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.util.Logger;
import com.rometools.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MobileService {

	@Autowired
	public NotificationService notificationService;
	@Autowired
	public APNService apnService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private PersonNetworkRegIdRepository mobileDeviceRepository;

	private Set<String> getDevices(List<PersonNetworkRegId> regIds) {
		return regIds.stream().map(regId -> regId.regId).collect(Collectors.toSet());
	}

	public void buildNotification(Post post) {
		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = post.title;

		List<PersonNetworkRegId> mobileDevices;

		if (stationRepository.isUnrestricted(post.station.id)) {
			mobileDevices = mobileDeviceRepository.findAll();
		} else {
			mobileDevices = mobileDeviceRepository.findRegIdByStationId(post.station.id);
		}

		if (Lists.isNotEmpty(mobileDevices)) {
			for (PersonNetworkRegId device : mobileDevices) {
				if (device.person != null && device.person.equals(post.author)) {
					mobileDevices.remove(device);
					break;
				}
			}
		}

		Set<String> deviceCodes = this.getDevices(mobileDevices);

		try {
			if (post.station != null) {
				asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationService.sendNotifications(notification, post, deviceCodes));
//				asyncService.run(TenantContextHolder.getCurrentTenantId(),
//						() -> apnService.sendToStation(post.station.id, notification));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRegId(Person person, String regId, Double lat, Double lng) {
		try {
			PersonNetworkRegId pnregId = mobileDeviceRepository.findOneByRegId(regId);
			if (pnregId == null || pnregId.regId == null) {
				pnregId = new PersonNetworkRegId();
				pnregId.regId = regId;
			}

			pnregId.person = person == null || person.username.equals("wordrails") ? null : person;
			if (lat != null && lng != null) {
				pnregId.lat = lat;
				pnregId.lng = lng;
			}
			mobileDeviceRepository.save(pnregId);
		} catch (Exception e) {
			Logger.error(e.getLocalizedMessage());
		}
	}
}
