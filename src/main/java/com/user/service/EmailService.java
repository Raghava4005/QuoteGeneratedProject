package com.user.service;

public interface EmailService {
	
	public boolean sendEmail(String subject, String body, String to);

}
