package co.xarx.trix.mobile.notification;

import co.xarx.trix.domain.*;
import co.xarx.trix.dto.NotificationDto;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.persistence.PersonNetworkRegIdRepository;
import co.xarx.trix.persistence.PersonNetworkTokenRepository;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Component
public class GCMService {

	@Value("${gcm.key}")
	private String GCM_KEY;
	private Sender sender;
	private final int GCM_WINDOW_SIZE = 1000;

	@Autowired private NetworkRepository networkRepository;
	@Autowired private PersonNetworkRegIdRepository personNetworkRegIdRepository;
	@Autowired private PersonNetworkTokenRepository personNetworkTokenRepository;
	@Autowired private NotificationRepository notificationRepository;
	private ObjectMapper mapper;

	@Async
	@Transactional
	public void sendToStation(Network network, Station station, Notification notification){

		Integer stationId = station.id;

		List<PersonNetworkRegId> personNetworkRegIds;

		if(station.visibility.equals(Station.UNRESTRICTED)){
			personNetworkRegIds = personNetworkRegIdRepository.findByNetwork(network);
		}else{
			personNetworkRegIds = personNetworkRegIdRepository.findRegIdByStationId(stationId);
		}

		try {
			removeNotificationProducer(personNetworkRegIds, notification);
			gcmNotify(personNetworkRegIds, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeNotificationProducer(
			List<PersonNetworkRegId> personNetworkRegIds,
			Notification notification) {
		if(personNetworkRegIds !=null && notification.person != null){
			Iterator<PersonNetworkRegId> it = personNetworkRegIds.iterator();
			while(it.hasNext()){
				PersonNetworkRegId regId = it.next();
				if(regId.person != null && regId.person.id.equals(notification.person.id))
					it.remove();
			}
		}
	}

	private void init() {
		if(sender == null){
			sender = new Sender(GCM_KEY);
		}

		if(mapper == null){
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		}
	}

	private void gcmNotify(List<PersonNetworkRegId> personNetworkRegIds,
	                      final Notification notification) throws Exception{

		if(personNetworkRegIds == null || notification == null)
			throw new UnexpectedException("Erro inesperado...");
		if(personNetworkRegIds.size() == 0)
			return;

		init();
		// make a copy
		HashSet<String> devices = new HashSet<String>();
		
		notification.hash = StringUtil.generateRandomString(10, "Aa#");
		ArrayList<Notification> notis = new ArrayList<Notification>();
		for (PersonNetworkRegId pnRegId : personNetworkRegIds) {
			Notification noti = new Notification();
			noti.message = notification.message + "";
			noti.network = pnRegId.network;
			noti.seen = notification.seen;
			noti.station = notification.station;
			noti.person = pnRegId.person;
			noti.post = notification.post;
			noti.type = notification.type + "";
			noti.hash = notification.hash + "";
			notification.person = pnRegId.person;

			devices.add(pnRegId.regId);

			if(pnRegId.person!=null)
				notis.add(noti);
		}
		
		notificationRepository.save(notis);

		NotificationDto notificationDto = new NotificationDto();
		notificationDto.seen = notification.seen;
		notificationDto.type = notification.type + "";
		notificationDto.message = notification.message + "";
		notificationDto.hash = notification.hash + "";
		notificationDto.personId = notification.person != null ? notification.person.id : null;
		notificationDto.personName = notification.person != null ? notification.person.name : null;
		notificationDto.networkId = notification.network != null ? notification.network.id : null;
		notificationDto.networkName = notification.network != null ? notification.network.name : null;;
		notificationDto.stationId = notification.station != null ? notification.station.id : null;
		notificationDto.stationName = notification.station != null ? notification.station.name : null;
		notificationDto.postId = notification.post != null ? notification.post.id : null;
		notificationDto.postTitle = notification.post != null ? notification.post.title : null;;
		notificationDto.postSnippet = notification.post != null ? TrixUtil.simpleSnippet(notification.post.body, 50) : null;
		notificationDto.imageSmallId = notification.post != null ? notification.post.imageSmallHash : null;
		notificationDto.imageMediumId = notification.post != null ? notification.post.imageMediumHash : null;

		String notificationJson = mapper.valueToTree(notificationDto).toString();

		try{
			Message message = new Message.Builder().addData("message", notificationJson).build();
			List<List<String>> parts = TrixUtil.partition(new ArrayList<String>(devices), GCM_WINDOW_SIZE);
			for (List<String> part : parts) {
				sendBulkMessages(message, part, notification.hash);
				Thread.sleep(2000);
			}
		}catch(Throwable e){
			System.err.println(notificationJson + " \n RegIds: " + personNetworkRegIds.size());
			e.printStackTrace();
		}
	}

	private void sendBulkMessages(Message message, List<String> devices, String notificationHash){
		MulticastResult multicastResult;
		try {
			System.out.println("sending messages... " + devices.size() + " hash: " + notificationHash);
			multicastResult = sender.send(message, devices, 5);
		} catch (Exception e) {
			e.printStackTrace();
			if(notificationHash!= null)
				notificationRepository.deleteByHash(notificationHash);
			return;
		}
		List<Result> results = multicastResult.getResults();
		// analyze the results
		for (int i = 0; i < devices.size(); i++) {
			String regId = devices.get(i);
			Result result = results.get(i);
			String messageId = result.getMessageId();
			if (messageId != null) {
				//				logger.fine("Succesfully sent message to device: " + regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					System.out.println("same device has more than on registration id: update it");
					try{
						personNetworkRegIdRepository.deleteByRegId(devices.get(i));
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					personNetworkRegIdRepository.deleteByRegId(devices.get(i));
				} else {
					System.out.println("Error sending message to " + regId + ": " + error);
				}
			}
		}
	}

	public void updateRegId(Network network, Person person, String regId, Double lat, Double lng) {
		try{

			PersonNetworkRegId pnregId = personNetworkRegIdRepository.findOneByRegId(regId);
			if(pnregId == null || pnregId.regId == null){
				pnregId = new PersonNetworkRegId();
				pnregId.regId = regId;
			}

			pnregId.network = network;
			pnregId.person = person == null || person.username.equals("wordrails") ? null : person;
			if(lat != null && lng != null){
				pnregId.lat = lat;
				pnregId.lng = lng;
			}
			personNetworkRegIdRepository.save(pnregId);
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}
}