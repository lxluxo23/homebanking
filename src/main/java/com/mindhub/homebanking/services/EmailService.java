package com.mindhub.homebanking.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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
    public void send(String from, String to, String subject, String text, byte[] attachment, String attachmentName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            ByteArrayResource resource = new ByteArrayResource(attachment);
            helper.addAttachment(attachmentName, resource);
            mailSender.send(message);
            LOGGER.info("Email send to: " + to);
        } catch (MessagingException e) {
            LOGGER.error("Error sending email");
            LOGGER.error(e.getMessage());
        }
    }
}
