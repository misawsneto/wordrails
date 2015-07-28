package com.wordrails.notification;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.wordrails.business.Network;
import com.wordrails.business.Notification;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.persistence.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.scheduling.annotation.Async;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jonas on 24/07/15.
 */
public class NotificationService {

	protected PersonRepository personRepository;
	protected NetworkRepository networkRepository;
	protected StationRepository stationRepository;
	protected PersonNetworkRegIdRepository personNetworkRegIdRepository;
	protected NotificationRepository notificationRepository;
	protected ObjectMapper mapper;

	protected void init(){}

	public void notify(List<PersonNetworkRegId> personNetworkRegIds, final Notification notification) throws Exception { }

	protected void sendBulkMessages(Message message, List<String> devices, String notificationHash){ }

	@Async
	@Transactional
	public void sendToStation(Integer stationId, Notification notification){
		List<PersonNetworkRegId> personNetworkRegIds = personNetworkRegIdRepository.findRegIdByStationId(stationId);
		try {
			removeNotificationProducer(personNetworkRegIds, notification);
			notify(personNetworkRegIds, notification);
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
			removeNotificationProducer(personNetworkRegIds, notification);
			notify(personNetworkRegIds, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void removeNotificationProducer(
			List<PersonNetworkRegId> personNetworkRegIds,
			Notification notification) {
		if(personNetworkRegIds !=null && notification.person != null){
			Iterator<PersonNetworkRegId> it = personNetworkRegIds.iterator();
			while(it.hasNext()){
				PersonNetworkRegId regId = it.next();
				if(regId.person.id.equals(notification.person.id))
					it.remove();
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
