package co.xarx.trix.services;

import co.xarx.trix.domain.Invitation;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonValidation;
import co.xarx.trix.util.FileUtil;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jcodec.common.logging.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
	public class EmailService {

	public final String VALIDATION_TEMPLATE_FILENAME = "validation-template.html";
	public final String INVITATION_TEMPLATE_FILENAME = "invitation-template.html";
	public final String CREDENTIALS_TEMPLATE_FILENAME = "credentials-email.html";

	private final String NO_REPLY = "noreply@trix.rocks";
	private final String USERNAME = "AKIAJKZJWC7NU3RF3URQ";
	private final String PASSWORD = "2knHnAfeG5uVXmCXcLjquUUiNnxyWjJcIuCp2mxg";

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

		try {
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(USERNAME, PASSWORD));
			Region REGION = Region.getRegion(Regions.US_EAST_1);
			client.setRegion(REGION);
			// Send the email.
			client.sendEmail(request);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void validatePersonCreation(Network network, Person inviter, PersonValidation newcome) throws Exception {
		String template = FileUtil.loadTemplateHTML(VALIDATION_TEMPLATE_FILENAME);

		String message = network.getValidationMessage();
		Map messageScope = new HashMap<String, String>();
		messageScope.put("networkName", network.getName());

		String validationMessage = parseScope(messageScope, message);

		Map emailsScope = new HashMap<String, String>();
		emailsScope.put("validationLink", "http://" + network.getRealDomain() + "/access/signup?validation=" + newcome.hash);
		emailsScope.put("name", newcome.getPerson().getName());
		emailsScope.put("validationMessage", validationMessage);

		String emailBody = parseScope(emailsScope, template);
		String subject = network.name + " - Boas-vindas";
		sendSimpleMail(newcome.person.email, subject, emailBody);
	}

	public void sendInvitation(Network network, Invitation invitation, Person inviter, String emailTemplate){
		Map messageScope = new HashMap<String, String>();
		messageScope.put("inviterName", inviter.getName());
		messageScope.put("networkName", network.getName());
		messageScope.put("inviteLink", "http://" + network.getRealDomain() + "/access/signup?invitation=" + invitation.hash);

		String emailBody = null;
		try {
			emailBody = parseScope(messageScope, emailTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String subject = network.name + " - Convite enviado por " + inviter.getName();
		sendSimpleMail(invitation.email, subject, emailBody);
		Logger.debug("Sent email to: " + invitation.email);
	}

	public void sendCredentials(Network network, Person inviter, Person invitee) throws Exception {
		String template = FileUtil.loadTemplateHTML(CREDENTIALS_TEMPLATE_FILENAME);

		Map messageScope = new HashMap<String, String>();
		messageScope.put("inviterName", inviter.getName());
		messageScope.put("networkName", network.getName());

		String message = parseScope(messageScope, network.getInvitationMessage());

		Map emailScope = new HashMap<String, String>();
		emailScope.put("name", invitee.getName());
		emailScope.put("invitationMessage", message);
		emailScope.put("password", invitee.getUser().getPassword());
		emailScope.put("username", invitee.getUsername());
		emailScope.put("link", "http://" + network.getRealDomain());

		String emailBody = parseScope(emailScope, template);
		String subject = "Boas-vindas - " + network.name;

		sendSimpleMail(invitee.email, subject, emailBody);
	}

	public String parseScope(Map scope, String template) throws Exception {
		Set<String> keyWords = scope.keySet();
		for(String keyWord: keyWords){
			if(!template.contains(keyWord)) {
				throw new Exception("Invalid template message. Missing key-word: " + keyWord);
			}
		}

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(template), "another_email_message");

		mustache.execute(writer, scope);
		writer.flush();
		return writer.toString();
	}

	public Map parseTemplateData(String inviteeName, Network network, Person inviter){
		Color c1 = Color.decode(network.primaryColors.get("500"));
		Color c2 = Color.decode(network.secondaryColors.get("300"));
		Integer bgColor = Integer.parseInt(network.backgroundColors.get("500").replace("#", ""), 16);
		Integer referenceColor = Integer.parseInt("ffffff", 16);

		String networkNameColor = (bgColor > referenceColor / 2) ? "black" : "white";

		HashMap<String, Object> scope = new HashMap<>();
		if(inviteeName != null) scope.put("name", inviteeName);
		scope.put("networkName", network.name);
		scope.put("primaryColor", "rgb(" + c1.getRed() + ", " + c1.getGreen() + ", " + c1.getBlue() + " )");
		scope.put("secondaryColor", "rgb(" + c2.getRed() + ", " + c2.getGreen() + ", " + c2.getBlue() + " )");
		scope.put("network", network);
		scope.put("inviterName", inviter.getName());
		scope.put("inviterEmail", inviter.getEmail());
		scope.put("networkNameColor", networkNameColor);

		return scope;
	}
}
