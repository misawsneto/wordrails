package co.xarx.trix.services.notification.util;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DummyNotificationData {
	public DummyNotificationData(Integer numOfDevices) {
		List<String> devices = new ArrayList<>();
		for (int i = 0; i < numOfDevices; i++) {
			devices.add("device" + i);
		}

		NotificationView notification = TestArtifactsFactory.createNotification();
		Post post = TestArtifactsFactory.createPost();

		this.devices = devices;
		this.notification = notification;
		this.post = post;
	}

	public Collection<String> devices;
	public NotificationView notification;
	public Post post;
}
