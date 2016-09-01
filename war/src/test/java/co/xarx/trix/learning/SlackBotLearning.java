package co.xarx.trix.learning;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;

public class SlackBotLearning {

    public static void main(String[] args) throws IOException {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("a-string-token");
        session.connect();
        System.out.println(session.getTeam().getName());

        SlackChannel channel = session.findChannelByName("trix-log");
        session.sendMessage(channel, "Hey there");
        session.disconnect();
    }
}
