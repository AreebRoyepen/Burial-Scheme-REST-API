package com.example.BurialSchemeRestApi.services;

import java.io.ByteArrayInputStream;

import javax.mail.MessagingException;

public interface EmailService {

	void sendEmailWithAttachment(String reportName, String to, String body, String subject, ByteArrayInputStream report, String type) throws MessagingException;
	
	void sendSimpleEmail(String to, String body, String subject) throws MessagingException;
	
}
