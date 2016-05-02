package co.xarx.trix.services;

import co.xarx.trix.domain.Invitation;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
	public class EmailService {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	private final String NO_REPLY = "noreply@trix.rocks";
	private final String USERNAME = "AKIAJKZJWC7NU3RF3URQ";
	private final String PASSWORD = "2knHnAfeG5uVXmCXcLjquUUiNnxyWjJcIuCp2mxg";
	private final String HOST = "email-smtp.us-east-1.amazonaws.com";
	private final String defaultMessage = "{{inviterName}} convidou você para fazer parte da rede {{networkName}}.\n";

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
	public void sendNetworkInvitation(Network network, Set<Invitation> invitations, Person inviter){
		if(invitations!= null)
		for (Invitation i : invitations){
			notifyPersonCreation(network, i, inviter);
		}
	}

	@Async
	public void notifyPersonCreation(Network network, Invitation invitation, Person inviter){
		try {

			String templateFile;

			if(invitation.sendPlainPassword){
				templateFile = "subscription-with-password-email.html";
			} else {
				templateFile = "complete-subscription-email.html";
			}

			String filePath = new ClassPathResource(templateFile).getFile().getAbsolutePath();

			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			String template = new String(bytes, Charset.forName("UTF-8"));
			notifyPersonCreation(network, invitation, template, inviter);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Async
	public void notifyPersonCreation(Network network, Invitation invitation, String template, Person inviter){
		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(template), "invitation-email");

		Map scopes = parseTemplateData(invitation, network, inviter);

		try{
			mustache.execute(writer, scopes);
			writer.flush();

			String emailBody = writer.toString();
			String subject = network.name + " - Convite enviado por " + inviter.getName();
			sendSimpleMail(invitation.person.email, subject, emailBody);
		}catch (Exception e){
			log.info(e.getMessage());
		}
	}

	public Map parseTemplateData(Invitation invitation, Network network, Person inviter){
		Color c1 = Color.decode(network.mainColor);
		Color c2 = Color.decode(network.navbarColor);
		Integer bgColor = Integer.parseInt(network.backgroundColor.replace("#", ""), 16);
		Integer referenceColor = Integer.parseInt("ffffff", 16);

		String networkNameColor = (bgColor > referenceColor / 2) ? "black" : "white";

		if (network.invitationMessage == null) network.invitationMessage = defaultMessage
				.replace("{{inviterName}}", inviter.getName())
				.replace("{{networkName}}", network.getName());

		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("networkName", network.name);
		scopes.put("primaryColor", "rgb(" + c1.getRed() + ", " + c1.getGreen() + ", " + c1.getBlue() + " )");
		scopes.put("secondaryColor", "rgb(" + c2.getRed() + ", " + c2.getGreen() + ", " + c2.getBlue() + " )");
		scopes.put("network", network);
		scopes.put("inviterName", inviter.getName());
		scopes.put("inviterEmail", inviter.getEmail());
		scopes.put("networkNameColor", networkNameColor);

		if (invitation.person != null && invitation.person.user != null) {
			scopes.put("name", invitation.getPerson().getName());
		}

		if (invitation.sendPlainPassword) {
			scopes.put("plainPassword", invitation.person.user.password);
			scopes.put("link", "http://" + network.getRealDomain());
		} else {
			scopes.put("link", invitation.getInvitationUrl());
		}

		return scopes;
	}
}
