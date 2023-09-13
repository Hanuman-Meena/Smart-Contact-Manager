package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

	
	public boolean sendEmail(String subject, String message, String to) throws MessagingException
	{
		boolean f = false;
		
		String host = "smtp.gmail.com";  //Variable for gmail
		
		String from = "smartcontactmanager23@gmail.com";
		
		Properties properties = System.getProperties();  //get the system properties
		
		// System.out.println(properties);
		
		//setting important information to properties object
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		//Step 1: to get the session object
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(from, "bdowtbrhixknvbjn");
			}	
		});
		
		session.setDebug(true);
		
		
		//Step 2: Compose the message [text or attachments or  multiMedia]
		
        MimeMessage mimeMessage = new MimeMessage(session);		
        
        try {
        	
        	mimeMessage.setFrom(from); //from this address email will be sent
        	
        	
        	mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); //recipient address
        	
        	
        	mimeMessage.setSubject(subject); //subject of the message/email
        	
        	
        	//mimeMessage.setText(message);  //adding text to message
        	
        	mimeMessage.setContent(message, "text/html");
        	
        	// Step  3: Send the message using transport class
        	
        	Transport.send(mimeMessage);
        	
        	f = true;
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return f;
		
	}
	

}
