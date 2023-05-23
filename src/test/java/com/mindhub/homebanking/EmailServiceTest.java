package com.mindhub.homebanking;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.mindhub.homebanking.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.mail.internet.MimeMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
@SpringBootTest
class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig().withUser("user@gmail.com", "password"));

    @Autowired
    private EmailService emailService;

    @Test
    void send() throws Exception {
        emailService.send(
                "no-reply@sense-it.cl",
                "lxluxo23@gmail.com",
                "Hello",
                "How are you?");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        MimeMessage receivedMessage = receivedMessages[0];

        assertEquals("no-reply@sense-it.cl", receivedMessage.getFrom()[0].toString());
        assertEquals("lxluxo23@gmail.com", receivedMessage.getAllRecipients()[0].toString());
        assertEquals("Hello", receivedMessage.getSubject().trim());
        assertEquals("How are you?", receivedMessage.getContent().toString().trim());

    }
}
 */



//Despues retomo este test


