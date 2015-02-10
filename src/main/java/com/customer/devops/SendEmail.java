package com.customer.devops;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

public class SendEmail {
	
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle("authentication",
				Locale.getDefault());

		String userEmail = bundle.getString("user_name");
		String user = bundle.getString("name");
		String recipient = bundle.getString("recipient");
		String password = bundle.getString("password");

		System.out.println("Email Address From: " + userEmail);
		System.out.println("User: " + user);
		System.out.println("Recipient: " + recipient);

		Properties props = System.getProperties();
		props.put("mail.smtps.host", "smtp.gmail.com");
		props.put("mail.smtps.auth", "true");
		Session session = Session.getInstance(props, null);
		SMTPTransport t = null;
		
		try {
			
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userEmail, user));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(recipient, false));
			
			String subject = getSubject(args[0]);
			msg.setSubject(subject);
			
			//msg.setText("Hi Zoilo!");
			String content = readHtmlFile(args[1]);
			msg.setContent(content, "text/html");
			msg.setSentDate(new Date());

			t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", userEmail, password);
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());

			
		}  catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			
		}  catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (null != t) {
					t.close();
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static String getSubject(String subject) throws Exception {
		String subjectVal= null;
		
		if (null != subject) {
			subjectVal = subject;
			
		} else {
			throw new Exception("No Subject Provided");
			
		}
		System.out.println("Subject: " + subjectVal);
		return subjectVal;
	}

	public static String readHtmlFile(String absolutePath) throws Exception {
		String stringVal = null;
		
		if(null != absolutePath) {
			
			File file = new File(absolutePath);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				
				StringBuilder builder = new StringBuilder();
				int ch;
				while((ch = fis.read()) != -1){
				    builder.append((char)ch);
				}
				
				stringVal = builder.toString();
				System.out.println("Content: " + stringVal);
			} catch (FileNotFoundException e) {	
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			} finally {
				
				if (null != fis) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		} else {
			throw new Exception("No HTML file was provided.");
		}

		return stringVal;
	}
}