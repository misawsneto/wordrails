package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.mobile.notification.ExpiredTokensListener;
import co.xarx.trix.mobile.notification.FailedConnectionsListener;
import co.xarx.trix.mobile.notification.RejectedNotificationsListener;
import co.xarx.trix.persistence.CertificateIosRepository;
import co.xarx.trix.persistence.PersonNetworkTokenRepository;
import co.xarx.trix.persistence.StationRepository;
import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.*;
import com.rometools.utils.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

@Service
public class APNService {

	private static final Integer APN_NOTIFICATION_SENT_LIMIT = 8999;
	private static final Logger log = LoggerFactory.getLogger(APNService.class);
	@Autowired
	private PersonNetworkTokenRepository personNetworkTokenRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private CertificateIosRepository certificateIosRepository;
	private PushManager<SimpleApnsPushNotification> pushManager;

	private boolean init() {
		CertificateIos certificate = null;
		SynchronousQueue sq = new SynchronousQueue();

		List<CertificateIos> certificates = certificateIosRepository.findAll();
		if(CollectionUtils.isNotEmpty(certificates)) certificate = certificates.get(0); //should have only one anyways

		try {
			if(certificate != null && certificate.isValid()){
				this.pushManager = new PushManager<SimpleApnsPushNotification>(ApnsEnvironment.getProductionEnvironment(), SSLContextUtil.createDefaultSSLContext(certificate.certificateIos.getBinaryStream(), certificate.certificatePassword),
						null, // Optional: custom event loop group
						null, // Optional: custom ExecutorService for calling listeners
						sq, // Optional: custom BlockingQueue implementation
						new PushManagerConfiguration(), "NetworkId_" + String.valueOf(certificate.id));
			} else {
				return false;
			}

		} catch (IOException | KeyManagementException | UnrecoverableKeyException |
				CertificateException | NoSuchAlgorithmException | SQLException | KeyStoreException e) {
			e.printStackTrace();
			return false;
		}
		pushManager.start();

		pushManager.registerRejectedNotificationListener(new RejectedNotificationsListener());
		pushManager.registerFailedConnectionListener(new FailedConnectionsListener()); //it shuts the manager down
		pushManager.registerExpiredTokenListener(new ExpiredTokensListener()); //it removes expired tokens
		pushManager.requestExpiredTokens();

		return true;
	}

	private void shutdown() {
		try {
			pushManager.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void sendToStation(Integer stationId, Notification notification) {
		List<PersonNetworkToken> personNetworkTokens;
		if (stationRepository.isUnrestricted(stationId)) {
			personNetworkTokens = personNetworkTokenRepository.findAll();
		} else {
			personNetworkTokens = personNetworkTokenRepository.findTokenByStationId(stationId);
		}

		if (Lists.isEmpty(personNetworkTokens)) {
			return;
		}

		try {
			if(init()){
				apnNotify(personNetworkTokens, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void apnNotify(List<PersonNetworkToken> personNetworkTokens, final Notification notification) throws MalformedTokenStringException, InterruptedException {

		ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		payloadBuilder.setBadgeNumber(1);
		payloadBuilder.setSoundFileName("default");
		payloadBuilder.addCustomProperty("postId", notification.post.id);
		payloadBuilder.addCustomProperty("stationName", notification.station.name);
		payloadBuilder.setAlertTitle(notification.station.name);
		payloadBuilder.setAlertBody(notification.message);

		int notificationsCounter = 0;
		for (PersonNetworkToken personToken : personNetworkTokens) {
			log.debug("Sending notification: " + personToken.token);

			if (personToken.person != null && personToken.person.id.equals(notification.person.id)) {
				continue; //this is the person that is producing the notification, don't send to him
			}

			pushManager.getQueue().put(new SimpleApnsPushNotification(TokenUtil.tokenStringToByteArray(personToken.token), payloadBuilder.buildWithDefaultMaximumLength()));

//			System.out.println(personToken.token);

			//Limit the number of notifications sent to less than 9000 per second, so it's possible to handle errors
			if (notificationsCounter >= APN_NOTIFICATION_SENT_LIMIT) {
				Thread.sleep(1000);
				notificationsCounter = 0;
			}
			notificationsCounter++;
		}

		shutdown();
	}

	public void updateIosToken(Network network, Person person, String token, Double lat, Double lng) {
		PersonNetworkToken pToken = personNetworkTokenRepository.findOneByToken(token);

		if (pToken == null) {
			pToken = new PersonNetworkToken();
			pToken.network = network;
		}

		pToken.token = token;
		pToken.person = person;
		if (lat != null && lng != null) {
			pToken.lat = lat;
			pToken.lng = lng;
		}
		personNetworkTokenRepository.save(pToken);
	}

}
