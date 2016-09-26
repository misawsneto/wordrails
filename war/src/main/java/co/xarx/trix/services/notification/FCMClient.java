package co.xarx.trix.services.notification.job;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationServerClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class FCMClient implements NotificationServerClient {

    @Autowired
    public FCMClient(){

    }

    @Override
    public Map<String, NotificationResult> getErrorDevices() {
        return null;
    }

    @Override
    public void send(NotificationView notification, Collection<String> devices) throws IOException {

    }
}
