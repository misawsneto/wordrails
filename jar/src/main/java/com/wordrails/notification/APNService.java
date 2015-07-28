package com.wordrails.notification;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.PushManagerConfiguration;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
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

}
