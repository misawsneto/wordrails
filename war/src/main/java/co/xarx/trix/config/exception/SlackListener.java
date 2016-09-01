package co.xarx.trix.config.exception;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import static co.xarx.trix.util.ServerUtil.getServerStatus;

public class SlackListener implements SlackMessagePostedListener {

    private String STATUS_KEYWORD = "status-trix";

    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        String messageContent = event.getMessageContent();
        SlackChannel channel = event.getChannel();

        if (messageContent.contains(STATUS_KEYWORD)) {
            session.sendMessage(channel, getServerStatus());
        }
    }

}
