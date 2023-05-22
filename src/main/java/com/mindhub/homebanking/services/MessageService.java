package com.mindhub.homebanking.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
public class MessageService {
    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @Value("${twilio.whatsapp.number}")
    private String whatsappPhoneNumber;
    public void sendSms(String toPhoneNumber, String body){
        try {
          Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    body
            ).create();
            LOGGER.info("Message send... to phone number: " + toPhoneNumber);
        }
        catch (Exception e){
            LOGGER.info("Message sending... to phone number: " + toPhoneNumber);
            LOGGER.error("error sending message");
            LOGGER.error(e.getMessage());
        }
    }
    public void sendWhatsapp(String toPhoneNumber, String body){
        try {
            Message.creator(
                    new PhoneNumber("whatsapp:"+toPhoneNumber),
                    new PhoneNumber("whatsapp:"+whatsappPhoneNumber),
                    body
            ).create();
            LOGGER.info("Whatsapp message send... to phone number: " + toPhoneNumber);
        }
        catch (Exception e){

            LOGGER.error("Error sending whatsapp message");
            LOGGER.error(e.getMessage());
        }
    }
}
