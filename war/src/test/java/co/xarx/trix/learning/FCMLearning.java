package co.xarx.trix.learning;

import com.google.android.gcm.server.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FCMLearning {

    public static void main(String[] args) throws IOException {
        List<String> regId = new ArrayList<>();
        regId.add("fB6vdmUzjRc:APA91bG5JPgj-HuKMaHvF5GqXi0iOmvc9eZrRPUHSwqKrCoT4RzI1DXvxO_jHYL3");
        regId.add("e3FUMEiYcBw:APA91bFPZZAGN3gip6czpAqYKwmJxTrosGgaWDpu2d9SVYCuMx_49vu76g-aRqLgNe8glXI6XczqdS7X2D854TgjmyTu6oGKDM_3V6nqSXV_VMSSFHk2ocPY8IVLfRjsj1Hbg6VOQvTO");

        String serverKey = "AIzaSyDqZfV7QfeeQmTXykYgwFyJrUCEWAqxKds";

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
                .timeToLive(86400)
                .build();

        MulticastResult result = sender.send(message, regId, 1);
        for(Result r: result.getResults()){
            System.out.println("Result: " + r.getSuccess() + " " + r.toString());
        }
    }
}
