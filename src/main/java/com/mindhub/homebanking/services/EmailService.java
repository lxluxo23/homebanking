/*package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailWithAttachment(String to, String subject, String text, byte[] attachmentData, String attachmentName) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        ByteArrayResource attachment = new ByteArrayResource(attachmentData);
        helper.addAttachment(attachmentName, attachment);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}*/
