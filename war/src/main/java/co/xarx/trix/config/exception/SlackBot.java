//package co.xarx.trix.config.exception;
//
//
//import com.ullink.slack.simpleslackapi.SlackChannel;
//import com.ullink.slack.simpleslackapi.SlackSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
//import static co.xarx.trix.util.ServerUtil.getServerStatus;
//
//@Service
//public class SlackBot {
//    private SlackSession session;
//    private String logChannel;
//
//    @Autowired
//    public SlackBot(SlackSession session, @Value("${log-channel}") String logChannel){
//        this.session = session;
//        this.logChannel = logChannel;
//    }
//
//    public void logError(String errorMessage){
//        sendMessage(errorMessage + getServerStatus(), logChannel);
//    }
//
//    public void sendMessage(String message){
//        sendMessage(message, logChannel);
//    }
//
//    private void sendMessage(String message, String channelName){
//        SlackChannel channel = session.findChannelByName(channelName);
//        session.sendMessage(channel, message);
//    }
//
//    @PostConstruct
//    public void startupBot(){
//        session.addMessagePostedListener(new SlackListener());
//        sendMessage("Startup done!" + getServerStatus(), logChannel);
//    }
//}
