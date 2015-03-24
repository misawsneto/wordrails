package com.wordrails.business;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final String NO_REPLY = "noreply@xarx.co";
	private final String USERNAME = "AKIAJZY66TLYMPY3I2GQ";
	private final String PASSWORD = "As5jyp6bjAVFs60UfBp18GM12GouhBHZ1gE/FvjX8xkS";
	private final String HOST = "email-smtp.us-east-1.amazonaws.com";
	
	@Async
	public void sendSimpleMail(String emailTo, String subject, String emailBody) {
		
		Email email = new HtmlEmail();

		email.setHostName(HOST);
		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		email.setSSLOnConnect(true);
		email.setStartTLSEnabled(true);

		try {
		    email.setFrom(NO_REPLY);
		    email.setSubject(subject);
		    email.setMsg(emailBody);
		    email.addTo(emailTo);
		    email.setSmtpPort(587);
		    email.setStartTLSEnabled(true);
		    email.send();
		} catch (EmailException e) {
		    e.printStackTrace();
		}

	}
}
