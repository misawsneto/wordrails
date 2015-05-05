package com.wordrails;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
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
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NotificationRepository;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.util.WordrailsUtil;

@Component
public class GCMService {

	@Value("${gcm.key}")
	private String GCM_KEY;
	private Sender sender;

	@Autowired private PersonRepository personRepository;
	@Autowired private NetworkRepository networkRepository;
	@Autowired private PersonNetworkRegIdRepository personNetworkRegIdRepository;
	@Autowired private NotificationRepository notificationRepository; 
	private ObjectMapper mapper;

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
			mapper.setSerializationInclusion(Inclusion.NON_DEFAULT)
				  .setSerializationInclusion(Inclusion.NON_NULL)
				  .setSerializationInclusion(Inclusion.NON_EMPTY);
		}
	}
	
	public void gcmNotify(List<PersonNetworkRegId> personNetworkRegIds, Notification notification) throws Exception{
		init();
		// make a copy
		List<String> devices = new ArrayList<String>();

		for (PersonNetworkRegId pnRegId : personNetworkRegIds) {
			devices.add(pnRegId.regId);
		}
		
		Post post = new Post();
		WordrailsUtil.nullifyProperties(post);
		post.title = notification.post.title;
		post.body = notification.post.body;
		notification.post = post;
		
		Network network = new Network();
		WordrailsUtil.nullifyProperties(network);
		network.id = notification.network.id;
		network.name = notification.network.name;
		notification.network = network;
		
		Station station = new Station();
		WordrailsUtil.nullifyProperties(station);
		station.id = notification.station.id;
		station.name = notification.station.name;
		notification.station = station;
		
		String notificationJson = mapper.valueToTree(notification).toString();
		
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
					System.out.println("Unregistered device: " + regId);
				} else {
					System.out.println("Error sending message to " + regId + ": " + error);
				}
			}
		}
	}
	

	public void updateRegId(Network network, Person person, String regId) {
		PersonNetworkRegId pnregId;
		List<PersonNetworkRegId> ids = personNetworkRegIdRepository.findByPersonAndNetwork(person, network);
		if(ids != null && ids.size() > 0){
			pnregId = ids.get(0);
			pnregId.regId = regId;
			personNetworkRegIdRepository.save(pnregId);
		}else{
			pnregId = new PersonNetworkRegId();
			pnregId.network = network;
			pnregId.person = person;
			pnregId.regId = regId;
			personNetworkRegIdRepository.save(pnregId);
		}
	}
}
