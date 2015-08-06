package com.wordrails.notification;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.*;
import com.wordrails.business.Network;
import com.wordrails.business.Notification;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkToken;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.PersonNetworkTokenRepository;
import com.wordrails.persistence.PersonRepository;
import com.wordrails.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.*;
import java.rmi.UnexpectedException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by jonas on 24/07/15.
 */
@Component
public class APNService {

	@Autowired private PersonNetworkTokenRepository personNetworkTokenRepository;
	@Autowired private NetworkRepository networkRepository;
	@Autowired private StationRepository stationRepository;
	private PushManager<SimpleApnsPushNotification> pushManager;
	private SynchronousQueue sq;

	private void init(Integer networkId){

		InputStream certificate = null;
		String certificatePassword = null;
		sq = new SynchronousQueue();

		try {
			certificate = networkRepository.findCertificateIosById(networkId).getBinaryStream();
			certificatePassword = networkRepository.findCertificatePasswordById(networkId);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			this.pushManager = new PushManager<SimpleApnsPushNotification>(
					ApnsEnvironment.getSandboxEnvironment(),
					SSLContextUtil.createDefaultSSLContext(certificate, certificatePassword),
					null, // Optional: custom event loop group
					null, // Optional: custom ExecutorService for calling listeners
					sq, // Optional: custom BlockingQueue implementation
					new PushManagerConfiguration(),
					"NetworkId_" + String.valueOf(networkId));
			pushManager.start();
		} catch (IOException | KeyManagementException | UnrecoverableKeyException |
				CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
	}

	private void shutdown(){
		try {
			pushManager.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Async
	@Transactional
	public void sendToStation(Integer stationId, Notification notification) throws UnexpectedException {
		List<PersonNetworkToken> personNetworkTokens = personNetworkTokenRepository
				.findTokenByStationId(stationId);

		Network network;
		if((network = stationRepository.findByStationId(stationId)) == null){
			throw new UnexpectedException("There is no such network...");
		}

		try {
			removeNotificationProducer(personNetworkTokens, notification);
			apnNotify(personNetworkTokens, notification, network);
		} catch (Exception e){
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
		List<PersonNetworkToken> personNetworkTokens = personNetworkTokenRepository
				.findByNetwork(network);
		try {
			removeNotificationProducer(personNetworkTokens, notification);
			apnNotify(personNetworkTokens, notification, network);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void apnNotify(List<PersonNetworkToken> personNetworkTokens,
	                      final Notification notification, Network network)
			throws UnexpectedException, MalformedTokenStringException, InterruptedException {

		if(personNetworkTokens == null || notification == null){
			throw new UnexpectedException("Unexpected error...");
		}
		if(personNetworkTokens.size() == 0) return;

		init(network.id);

		ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		payloadBuilder.setBadgeNumber(1);
		payloadBuilder.setSoundFileName("default");
		payloadBuilder.addCustomProperty("postId", notification.postId);
		payloadBuilder.addCustomProperty("stationName", notification.station.name);
		payloadBuilder.setAlertTitle(notification.station.name);
		payloadBuilder.setAlertBody(notification.message);

		for(PersonNetworkToken personToken: personNetworkTokens){
			pushManager.getQueue().put(new SimpleApnsPushNotification(
					TokenUtil.tokenStringToByteArray(personToken.token),
					payloadBuilder.buildWithDefaultMaximumLength()
			));
		}

		shutdown();

	}

	private void removeNotificationProducer(
			List<PersonNetworkToken> personNetworkTokens,
			Notification notification){
		if(personNetworkTokens != null && notification.person.id != null){
			Iterator<PersonNetworkToken> it = personNetworkTokens.iterator();
			while(it.hasNext()){
				PersonNetworkToken token = it.next();
				if(token.person != null && token.person.id.equals(notification.person.id)){
					it.remove();
				}
			}
		}
	}

	public void updateIosToken(Network network, Person person, String token,
	                           Double lat, Double lng){
		try{
			PersonNetworkToken pToken = personNetworkTokenRepository.findOneByToken(token);
			if(token == null || pToken.token == null){
				pToken = new PersonNetworkToken();
				pToken.token = token;
			}

			pToken.network = network;
			pToken.person = person;
			if(lat != null && lng != null){
				pToken.lat = lat;
				pToken.lng = lng;
			}
			personNetworkTokenRepository.save(pToken);
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
	}

}
