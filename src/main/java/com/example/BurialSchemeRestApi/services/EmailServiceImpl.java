package com.example.BurialSchemeRestApi.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService{

	private JavaMailSender emailSender;

	public EmailServiceImpl(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	public void sendEmailWithAttachment(String reportName, String to, String body, String subject, ByteArrayInputStream report, String type) throws MessagingException {

		
		try {
			MimeMessage message = emailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);
			ByteArrayDataSource resource;
			resource = new ByteArrayDataSource(report, type);
			helper.addAttachment( reportName, resource);
			
			emailSender.send(message);
			
		} catch (IOException e) {

			System.out.println("UNABLE TO PRINT PDF!");
			e.printStackTrace();
		}
		

		
	}

	@Override
	public void sendSimpleEmail(String to, String body, String subject) throws MessagingException {

		SimpleMailMessage message = new SimpleMailMessage(); 
		System.out.println(to);
		System.out.println(body);
		System.out.println(subject);
		
		
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(body);
        
        System.out.println(message.getSubject());
        emailSender.send(message);
		
	}

}
