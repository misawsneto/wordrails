package co.xarx.trix.learning;

import com.google.android.gcm.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FCMLearning {

    public static void main(String[] args) throws IOException {
        List<String> regId = new ArrayList<>();
        regId.add("fJppkVIIrpc:APA91bHT7nYFZK5fbwlgzupG1LixVVzdRYb1Mz75u6ocCKbW7SxrtLjjpkQr2-BokGKrv-YSLczSnbBBaGnZkLSHfeynOlE1v5H7rB4nZ7WuLbjnO6wDAP2zsk1zjm4nFXAHs522MjqT");

//        String serverKey = "AIzaSyDqZfV7QfeeQmTXykYgwFyJrUCEWAqxKds";
        String serverKey = "AIzaSyAb9u29xm5WsjzxjYgYiMPK7C0wFCrRgmg";

        Notification notification = new Notification.Builder("Icon")
                .title("Xarx Rocks!")
                .body("Olá")
                .build();

        Sender sender = new FCMSender(serverKey);
        Message message = new Message.Builder()
                .notification(notification)
                .addData("entity_id", "123")
                .addData("receiver_id", "321")
                .delayWhileIdle(true)
                .timeToLive(0)
                .build();

        MulticastResult result = sender.send(message, regId, 1);
        for(Result r: result.getResults()){
            System.out.println("Result: " + r.getSuccess() + " " + r.toString());
        }
    }
}
