package com.wordrails.notification;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Created by jonas on 24/07/15.
 */
public class APNService {

	private PushManager<SimpleApnsPushNotification> pushManager = null;
	private final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

	public APNService(){ }

	private void init(String networkCert, String certPassword){
		try {
			this.pushManager = new PushManager<SimpleApnsPushNotification>(
					ApnsEnvironment.getSandboxEnvironment(),
					SSLContextUtil.createDefaultSSLContext(networkCert + ".p12", certPassword),
					null, // Optional: custom event loop group
					null, // Optional: custom ExecutorService for calling listeners
					null, // Optional: custom BlockingQueue implementation
					new PushManagerConfiguration(),
					"ExamplePushManager");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pushManager.start();
	}

	public void shutdown(){
		try {
			pushManager.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void add2Queue(String deviceToken, String notifyMsg) throws InterruptedException, MalformedTokenStringException {
		final byte[] token = TokenUtil.tokenStringToByteArray(
				"<5f6aa01d 8e335894 9b7c25d4 61bb78ad 740f4707 462c7eaf bebcf74f a5ddb387>");

		payloadBuilder.addCustomProperty("msg", notifyMsg);

		String payload = payloadBuilder.buildWithDefaultMaximumLength();

		pushManager.getQueue().put(new SimpleApnsPushNotification(token, payload));
	}

}
