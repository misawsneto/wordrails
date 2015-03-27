package com.wordrails.ga;

import org.springframework.integration.annotation.ServiceActivator;

import com.github.tasubo.jgmp.ClientID;
import com.github.tasubo.jgmp.Decorating;
import com.github.tasubo.jgmp.MpClient;

class SendHitService {
//	@ServiceActivator
//	public void sendHit(final Hit hit) {
//		Decorating userIp = new Decorating() {			
//			@Override
//			public String getPart() {
//				return "&uip=" + hit.userIp;
//			}
//		};
//		ClientID client = ClientID.seeded(hit.clientSeed);
//		MpClient analytics = MpClient.withTrackingId(hit.trackingId).create();
//		analytics.send(hit.sendable.with(client).with(userIp));
//	}
}