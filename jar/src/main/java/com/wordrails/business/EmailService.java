package com.wordrails.business;

//import org.apache.commons.mail.DefaultAuthenticator;
//import org.apache.commons.mail.Email;
//import org.apache.commons.mail.EmailException;
//import org.apache.commons.mail.HtmlEmail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Service
public class EmailService {

	private final String NO_REPLY = "noreply@xarx.co";
	private final String USERNAME = "AKIAJKZJWC7NU3RF3URQ";
	private final String PASSWORD = "2knHnAfeG5uVXmCXcLjquUUiNnxyWjJcIuCp2mxg";
//	private final String HOST = "email-smtp.us-east-1.amazonaws.com";

	@Async
	public void sendSimpleMail(String emailTo, String subject, String emailBody) {

		//		apache commons email
		//		Email email = new HtmlEmail();
		//
		//		email.setHostName(HOST);
		//		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		//		email.setSSLOnConnect(true);
		//		email.setStartTLSEnabled(true);
		//
		//		try {
		//		    email.setFrom(NO_REPLY);
		//		    email.setSubject(subject);
		//		    email.setMsg(emailBody);
		//		    email.addTo(emailTo);
		//		    email.setSmtpPort(587);
		//		    email.setSSLOnConnect(true);
		//		    email.setStartTLSEnabled(true);
		//		    email.send();
		//		} catch (EmailException e) {
		//		    e.printStackTrace();
		//		}

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses(new String[]{emailTo});

		// Create the subject and body of the message.
		Content subjectc = new Content().withData(subject);
		Content textBody = new Content().withData(emailBody); 
		Body body = new Body().withHtml(textBody);

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject(subjectc).withBody(body);

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource(NO_REPLY).withDestination(destination).withMessage(message);

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
}
