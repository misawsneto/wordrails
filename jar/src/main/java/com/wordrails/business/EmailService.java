package com.wordrails.business;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final String INVITATION_SUBJECT = "Convite";
	private final String NO_REPLY = "noreply@xarx.co";
	private final String USERNAME = "AKIAJZY66TLYMPY3I2GQ";
	private final String PASSWORD = "As5jyp6bjAVFs60UfBp18GM12GouhBHZ1gE/FvjX8xkS";
	private final String HOST = "email-smtp.us-east-1.amazonaws.com";
	
	@Async
	public void sendSimpleMail(InternetAddress emailTo, String emailBody) {
		try {
			Properties props = new Properties();

			String host = HOST;
			String username = USERNAME;
			String password = PASSWORD;
			String noreply = NO_REPLY;
			int port = 465;

			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", port); 
			// Set properties indicating that we want to use STARTTLS to encrypt the connection.
			// The SMTP session will begin on an unencrypted connection, and then the client
			// will issue a STARTTLS command to upgrade to an encrypted connection.
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			// Create a Session object to represent a mail session with the specified properties. 
			Session session = Session.getDefaultInstance(props);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(noreply));
			message.setRecipient(Message.RecipientType.TO, emailTo);
			message.setSubject(INVITATION_SUBJECT);
			message.setText(emailBody, "utf-8", "html");
			// Create a transport.        
			Transport transport = session.getTransport();

			try {
				transport.connect(host, username, password);
				transport.sendMessage(message, message.getAllRecipients());

			} catch (MessagingException e) {
				throw new RuntimeException(e);

			}finally{
				transport.close();
			}

		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			e1.printStackTrace();
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
