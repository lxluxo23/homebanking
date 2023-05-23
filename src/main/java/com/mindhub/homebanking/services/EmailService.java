package com.mindhub.homebanking.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String from, String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            LOGGER.info("Sending email to : " + to);
        }
        catch (Exception e) {
            LOGGER.error("Error sending email");
            LOGGER.error(e.getMessage());
        }

    }
}
