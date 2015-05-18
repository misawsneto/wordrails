package com.wordrails;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.wordrails.business.Network;
import com.wordrails.business.Notification;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NotificationRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.util.NotificationDto;
import com.wordrails.util.WordrailsUtil;

@Component
public class GCMService {

	@Value("${gcm.key}")
	private String GCM_KEY;
	private Sender sender;

	@Autowired private PersonRepository personRepository;
	@Autowired private NetworkRepository networkRepository;
	@Autowired private StationRepository stationRepository;
	@Autowired private PersonNetworkRegIdRepository personNetworkRegIdRepository;
	@Autowired private NotificationRepository notificationRepository; 
	private ObjectMapper mapper;
	
	@Async
	@Transactional
	public void sendToStation(Integer stationId, Notification notification){
		List<PersonNetworkRegId> personNetworkRegIds = personNetworkRegIdRepository.findRegIdByStationId(stationId);
		try {
			gcmNotify(personNetworkRegIds, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendToNetwork(Integer networkId, Notification notification){
		Network network = networkRepository.findOne(networkId);
		sendToNetwork(network, notification);
	}

	@Async
	@Transactional
	public void sendToNetwork(Network network, Notification notification){
		List<PersonNetworkRegId> personNetworkRegIds = personNetworkRegIdRepository.findByNetwork(network);
		try {
			gcmNotify(personNetworkRegIds, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		if(sender == null){
			sender = new Sender(GCM_KEY);
		}

		if(mapper == null){
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Inclusion.NON_EMPTY);
			mapper.setSerializationInclusion(Inclusion.NON_DEFAULT);
		}
	}

	public void gcmNotify(List<PersonNetworkRegId> personNetworkRegIds, final Notification notification) throws Exception{
		init();
		// make a copy
		List<String> devices = new ArrayList<String>();
		
		for (PersonNetworkRegId pnRegId : personNetworkRegIds) {
			Notification noti = new Notification();
			noti.message = notification.message + "";
			noti.network = pnRegId.network;
			noti.seen = notification.seen;
			noti.station = notification.station;
			noti.person = pnRegId.person;
			noti.post = notification.post;
			noti.type = notification.type + "";

			devices.add(pnRegId.regId);
			notification.person = pnRegId.person;
			notificationRepository.save(noti);
		}

		NotificationDto notificationDto = new NotificationDto(); 
		notificationDto.seen = notification.seen;
		notificationDto.type = notification.type + "";
		notificationDto.message = notification.message + "";
		notificationDto.personId = notification.person != null ? notification.person.id : null;
		notificationDto.personName = notification.person != null ? notification.person.name : null;
		notificationDto.networkId = notification.network != null ? notification.network.id : null;
		notificationDto.networkName = notification.network != null ? notification.network.name : null;;
		notificationDto.stationId = notification.station != null ? notification.station.id : null;
		notificationDto.stationName = notification.station != null ? notification.station.name : null;
		notificationDto.postId = notification.post != null ? notification.post.id : null;
		notificationDto.postTitle = notification.post != null ? notification.post.title : null;;
		notificationDto.postSnippet = notification.post != null ? WordrailsUtil.simpleSnippet(notification.post.body) : null;
		notificationDto.imageSmallId = notification.post != null ? notification.post.imageSmallId : null;
		notificationDto.imageMediumId = notification.post != null ? notification.post.imageMediumId : null;

		String notificationJson = mapper.valueToTree(notificationDto).toString();

		Message message = new Message.Builder().addData("message", notificationJson).build();
		MulticastResult multicastResult;

		try {
			multicastResult = sender.send(message, devices, 5);
		} catch (Exception e) {
			e.printStackTrace();
			if(notification.id != null)
				notificationRepository.delete(notification.id);
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
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					personNetworkRegIdRepository.deleteByRegId(result.getCanonicalRegistrationId());
				} else {
					System.out.println("Error sending message to " + regId + ": " + error);
				}
			}
		}
	}

	public void updateRegId(Network network, Person person, String regId) {
		try{
			
			PersonNetworkRegId pnregId = personNetworkRegIdRepository.findOneByRegId(regId);
			if(pnregId == null || pnregId.regId == null){
				pnregId = new PersonNetworkRegId();
				pnregId.regId = regId;
			}
			
			pnregId.network = network;
			pnregId.person = person;
			personNetworkRegIdRepository.save(pnregId);
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}
}
