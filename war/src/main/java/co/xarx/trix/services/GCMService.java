package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonNetworkRegId;
import co.xarx.trix.dto.NotificationDto;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.persistence.PersonNetworkRegIdRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.util.*;
import com.amazonaws.services.dynamodbv2.xspec.S;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.*;
import com.google.android.gcm.server.Constants;
import com.rometools.utils.Lists;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GCMService {

	@Autowired
	private Sender sender;
	private final int GCM_WINDOW_SIZE = 1000;

	@Autowired
	private PersonNetworkRegIdRepository personNetworkRegIdRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	@Qualifier("gcmMapper")
	private ObjectMapper mapper;
	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	public List<Notification> sendNotifications(Notification notification, List<PersonNetworkRegId> personNetworkRegIds){
		Assert.isNotNull(personNetworkRegIds, "Null regIds");
		Assert.isNotNull(notification, "Null notification");

		Set<String> devices = getDevices(personNetworkRegIds);

		List<List<String>> parts = TrixUtil.partition(new ArrayList<>(devices), GCM_WINDOW_SIZE);

		NotificationDto notificationDto = makeDto(notification);
		String notificationJson = mapper.valueToTree(notificationDto).toString();

		Message message = new Message.Builder()
					.addData("message", notificationJson)
					.delayWhileIdle(true)
					.timeToLive(86400)
					.build();

		List<Result> results;
		List<Notification> notifications = new ArrayList<>();
		for (List<String> part : parts) {

			String hash = StringUtil.generateRandomString(10, "Aa#");
			try {
				results = sendMessageToDevices(message, part, notification.hash);

				
			} catch (IOException e) {
				notifications.addAll(this.getNotifications(part, notification, "Failed to send to GCM",
						co.xarx.trix.util.Constants.Notification.STATUS_SEND_ERROR, hash));
			}



			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}

		return notifications;
	}

	private List<Notification> addResultsToNotifications(){

	}

	@Transactional
	public void sendToStation(Integer stationId, Notification notification){
		Assert.isNotNull(notification.person, "Author is null");
		List<PersonNetworkRegId> personNetworkRegIds;

		if (stationRepository.isUnrestricted(stationId)) {
			personNetworkRegIds = personNetworkRegIdRepository.findAll();
		}else{
			personNetworkRegIds = personNetworkRegIdRepository.findRegIdByStationId(stationId);
		}

		try {
			if (Lists.isNotEmpty(personNetworkRegIds)) {
				Iterator<PersonNetworkRegId> it = personNetworkRegIds.iterator();
				while (it.hasNext()) {
					PersonNetworkRegId regId = it.next();
					if (regId.person != null && regId.person.id.equals(notification.person.id)) it.remove();
				}
			}

			gcmNotify(personNetworkRegIds, notification);
		} catch (Exception e) {
			Logger.error(e.toString());
		}
	}

	private List<Notification> getNotifications(List<String> regIds,
												final Notification notification,
												String errorMessage,
												String status,
												String hash){
		Assert.isNotNull(regIds, "Null regIds");
		Assert.isNotNull(notification, "Null notification");

		notification.hash = hash;
		List<Notification> notis = new ArrayList<Notification>();

		for (String pnRegId : regIds) {
			Notification noti = new Notification();
			noti.message = notification.message;
			noti.regId = pnRegId;
			noti.post = notification.post;
			noti.type = notification.type;
			noti.hash = hash;
			noti.status = status;
			noti.errorCodeName = errorMessage;
			notis.add(noti);
		}

		return notis;
	}

	private Set<String> getDevices(List<PersonNetworkRegId> regIds){
		return regIds.stream().map(regId -> regId.regId).collect(Collectors.toSet());
	}

	private NotificationDto makeDto(Notification notification){
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.seen = notification.seen;
		notificationDto.type = notification.type + "";
		notificationDto.message = notification.message + "";
		notificationDto.hash = notification.hash + "";
		notificationDto.personId = notification.person != null ? notification.person.id : null;
		notificationDto.personName = notification.person != null ? notification.person.name : null;
		notificationDto.stationId = notification.station != null ? notification.station.id : null;
		notificationDto.stationName = notification.station != null ? notification.station.name : null;
		notificationDto.postId = notification.post != null ? notification.post.id : null;
		notificationDto.postTitle = notification.post != null ? notification.post.title : null;

		notificationDto.postSnippet = notification.post != null ? StringUtil.simpleSnippet(notification.post.body) : null;
		if("dev_prod".equals(profile) || "prod".equals(profile))
			notificationDto.test = false;

		return notificationDto;
	}

	private void gcmNotify(List<PersonNetworkRegId> personNetworkRegIds,
						  final Notification notification) throws Exception{

		List<Notification> notis = getNotifications(personNetworkRegIds, notification);
		Set<String> devices = getDevices(personNetworkRegIds);

		notificationRepository.save(notis);

		NotificationDto notificationDto = makeDto(notification);

		String notificationJson = mapper.valueToTree(notificationDto).toString();

		try{
			Message message = new Message.Builder()
					.addData("message", notificationJson)
					.delayWhileIdle(true)
					.timeToLive(86400)
					.build();
			List<List<String>> parts = TrixUtil.partition(new ArrayList<>(devices), GCM_WINDOW_SIZE);
			for (List<String> part : parts) {
				sendBulkMessages(message, part, notification.hash);
				Thread.sleep(2000);
			}
		}catch(Throwable e){
			Logger.error(notificationJson + " \n RegIds: " + personNetworkRegIds.size());
			Logger.error(e.toString());
		}
	}

	public List<Result> sendMessageToDevices(Message message, List<String> devices, String notificationHash) throws IOException {
		MulticastResult multicastResult;

		Logger.info("sending messages... " + devices.size() + " hash: " + notificationHash);
		multicastResult = sender.send(message, devices, 5);

		return multicastResult.getResults();
	}

	public void sendBulkMessages(Message message, List<String> devices, String notificationHash){
		MulticastResult multicastResult;

		try {
			Logger.info("sending messages... " + devices.size() + " hash: " + notificationHash);
			multicastResult = sender.send(message, devices, 5);
		} catch (Exception e) {
			Logger.error(e.toString());
			if(notificationHash!= null)
				notificationRepository.deleteByHash(notificationHash);
			return;
		}
		List<Result> results = multicastResult.getResults();
		// analyze the results
		for (int i = 0; i < devices.size(); i++) {
			String regId = devices.get(i);
			Result result = results.get(i);
			String messageId = result.getErrorCodeName()getMessageId();
			if (messageId != null) {
				//				logger.fine("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					Logger.info("same device has more than one registration id: update it");
					try{
						personNetworkRegIdRepository.deleteByRegId(devices.get(i));
					}catch(Exception e){
						Logger.error(e.toString());
					}
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					personNetworkRegIdRepository.deleteByRegId(devices.get(i));
				} else {
					Logger.error("Error sending message to " + regId + ": " + error);
				}
			}
		}
	}

	public void updateRegId(Network network, Person person, String regId, Double lat, Double lng) {
		try {
			PersonNetworkRegId pnregId = personNetworkRegIdRepository.findOneByRegId(regId);
			if (pnregId == null || pnregId.regId == null) {
				pnregId = new PersonNetworkRegId();
				pnregId.regId = regId;
			}

			pnregId.network = network;
			pnregId.person = person == null || person.username.equals("wordrails") ? null : person;
			if (lat != null && lng != null) {
				pnregId.lat = lat;
				pnregId.lng = lng;
			}
			personNetworkRegIdRepository.save(pnregId);
		}catch(Exception e){
			Logger.error(e.getLocalizedMessage());
		}
	}
}
