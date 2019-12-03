package com.selectionarts.projectcensio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
	
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String origin;
	
	@Async
	public void sendEmailAsync(String email, String subject, String message) {
		MimeMessage mail = mailSender.createMimeMessage();

		try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            System.out.println("Mail sent to: " + email);
            helper.setTo(email);

            helper.setFrom(origin);
            helper.setSubject(subject);
            helper.setText(message);
            mail.setContent(message, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        mailSender.send(mail);
	}
}
