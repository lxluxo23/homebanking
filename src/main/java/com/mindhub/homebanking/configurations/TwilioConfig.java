package com.mindhub.homebanking.configurations;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    public TwilioConfig(@Value("${twilio.account.sid}") String accountSid,
                        @Value("${twilio.auth.token}") String authToken) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        Twilio.init(accountSid, authToken);

    }
}
