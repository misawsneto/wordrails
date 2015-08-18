package com.wordrails.notification;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.*;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component
public class APNService {

	@Autowired private PersonNetworkTokenRepository personNetworkTokenRepository;
	@Autowired private StationRepository stationRepository;
	@Autowired private NetworkRepository networkRepository;
	@Autowired private TrixAuthenticationProvider authProvider;
	@Autowired private CertificateIosRepository certificateIosRepository;
	private PushManager<SimpleApnsPushNotification> pushManager;
	private static final Integer APN_NOTIFICATION_SENT_LIMIT = 8999;

	private static final Logger log = LoggerFactory.getLogger(APNService.class);

	private void init(Integer networkId){
		CertificateIos certificate;
		SynchronousQueue sq = new SynchronousQueue();

		certificate = certificateIosRepository.findOne(networkId);

		try {
			this.pushManager = new PushManager<SimpleApnsPushNotification>(
					ApnsEnvironment.getProductionEnvironment(),
					SSLContextUtil.createDefaultSSLContext(certificate.certificateIos.getBinaryStream(),
							certificate.certificatePassword),
					null, // Optional: custom event loop group
					null, // Optional: custom ExecutorService for calling listeners
					sq, // Optional: custom BlockingQueue implementation
					new PushManagerConfiguration(),
					"NetworkId_" + String.valueOf(networkId));
		} catch (IOException | KeyManagementException | UnrecoverableKeyException |
				CertificateException | NoSuchAlgorithmException | SQLException | KeyStoreException e) {
			e.printStackTrace();
		}
		pushManager.start();

		pushManager.registerRejectedNotificationListener(new RejectedNotificationsListener());
		pushManager.registerFailedConnectionListener(new FailedConnectionsListener()); //it shuts the manager down
		pushManager.registerExpiredTokenListener(new ExpiredTokensListener()); //it removes expired tokens
		pushManager.requestExpiredTokens();
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
		Network network = authProvider.getNetwork();
		List<PersonNetworkToken> personNetworkTokens;
		if(stationRepository.isUnrestricted(stationId)) {
			personNetworkTokens = personNetworkTokenRepository.findByNetwork(network);
		} else {
			personNetworkTokens = personNetworkTokenRepository.findByStationId(stationId);
		}

		try {
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
		payloadBuilder.addCustomProperty("postId", notification.post.id);
		payloadBuilder.addCustomProperty("stationName", notification.station.name);
		payloadBuilder.setAlertTitle(notification.station.name);
		payloadBuilder.setAlertBody(notification.message);

		int notificationsCounter = 0;
		for(PersonNetworkToken personToken: personNetworkTokens){
			log.debug("Sending notification: " + personToken.token);

			if(personToken.person != null && personToken.person.id.equals(notification.person.id)){
				continue; //this is the person that is producing the notification, don't send to him
			}

			pushManager.getQueue().put(new SimpleApnsPushNotification(
					TokenUtil.tokenStringToByteArray(personToken.token),
					payloadBuilder.buildWithDefaultMaximumLength()
			));

//			System.out.println(personToken.token);

			//Limit the number of notifications sent to less than 9000 per second, so it's possible to handle errors
			if(notificationsCounter >= APN_NOTIFICATION_SENT_LIMIT){
				Thread.sleep(1000);
				notificationsCounter = 0;
			}
			notificationsCounter++;
		}

		shutdown();
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
