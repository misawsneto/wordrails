package co.xarx.trix.services.notification;

import co.xarx.trix.services.notification.client.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCMSender extends Sender {
    private String fcmUrl = "https://fcm.googleapis.com/fcm/send";

    public FCMSender(String key) {
        super(key);
    }

    @Override
    protected HttpURLConnection getConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(fcmUrl).openConnection();
    }
}
