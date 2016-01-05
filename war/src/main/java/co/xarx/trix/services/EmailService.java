package co.xarx.trix.services;

import co.xarx.trix.domain.Invitation;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

@Service
public class EmailService {

	private final String NO_REPLY = "noreply@trix.rocks";
	private final String USERNAME = "AKIAJKZJWC7NU3RF3URQ";
	private final String PASSWORD = "2knHnAfeG5uVXmCXcLjquUUiNnxyWjJcIuCp2mxg";
	private final String HOST = "email-smtp.us-east-1.amazonaws.com";

	@Async
	public void sendSimpleMail(String emailTo, String subject, String emailBody) {
        sendSimpleMail(NO_REPLY, emailTo, subject, emailBody);
    }

    @Async
    public void sendSimpleMail(String emailFrom, String emailTo, String subject, String emailBody) {

//		 Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[]{emailTo});

		// Create the subject and body of the message.
		Content subjectc = new Content().withData(subject);
		Content textBody = new Content().withData(emailBody);
		Body body = new Body().withHtml(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subjectc).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(emailFrom).withDestination(destination).withMessage(message);

		try
		{
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(USERNAME, PASSWORD));
			Region REGION = Region.getRegion(Regions.US_EAST_1);
			client.setRegion(REGION);
			// Send the email.
			client.sendEmail(request);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

    @Async
    public void sendNetworkInvitation(Network network, Set<Invitation> invitations){
        if(invitations!= null)
        for (Invitation i : invitations){
            sendNetworkInvitation(network, i);
        }
    }

    @Async
    public void sendNetworkInvitation(Network network, Invitation invitation){
        try {
            String filePath = getClass().getClassLoader().getResource("tpl/network-invitation-email.html").getFile();

            filePath = System.getProperty("os.name").contains("indow") ? filePath.substring(1) : filePath;

            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            String template = new String(bytes, Charset.forName("UTF-8"));
            sendNetworkInvitation(network, invitation, template);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public void sendNetworkInvitation(Network network, Invitation invitation, String template){
        try{
            Color c1 = Color.decode(network.mainColor);
            Color c2 = Color.decode(network.navbarColor);

            HashMap<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("name", invitation.personName);
            scopes.put("networkName", network.name);
            scopes.put("primaryColor", "rgb(" + c1.getRed() + ", " + c1.getGreen() + ", "+ c1.getBlue() +" )");
            scopes.put("secondaryColor", "rgb(" + c2.getRed() + ", " + c2.getGreen() + ", "+ c2.getBlue() +" )");
            scopes.put("link", "http://"+network.subdomain+".trix.rocks");
            scopes.put("invitationUrl", invitation.getInvitationUrl());
            scopes.put("networkSubdomain", network.subdomain);
            scopes.put("network", network);
            scopes.put("hash", invitation.hash);

            StringWriter writer = new StringWriter();

            Handlebars handlebars = new Handlebars();

            Template hTemplate = handlebars.compileInline("Hello {{this}}!");

            MustacheFactory mf = new DefaultMustacheFactory();

            Mustache mustache = mf.compile(new StringReader(template), "invitation-email");
            mustache.execute(writer, scopes);
            writer.flush();

            String emailBody = writer.toString();
            String subject = "[ Test ]" + " Cadastro de senha";
            sendSimpleMail("misawsneto@gmail.com", subject, emailBody);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
