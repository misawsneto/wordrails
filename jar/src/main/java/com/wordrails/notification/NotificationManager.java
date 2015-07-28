package com.wordrails.notification;

import com.wordrails.GCMService;
import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;
import com.wordrails.persistence.PersonNetworkRegIdRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jonas on 28/07/15.
 */
public class NotificationManager {
	private GCMService androidNotification;
	private APNService iosNotification;

	private @Autowired
	PersonNetworkRegIdRepository personNetworkRegIdRepository;

	public NotificationManager(){

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
