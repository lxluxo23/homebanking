package com.mindhub.homebanking;


import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }
    @Test
    public void cardCVVIsCreated(){
        Integer cardCVV = CardUtils.createCVVNumber();
        assertThat(cardCVV,is(not(nullValue())));
        assertThat(cardCVV.toString().length(), is(equalTo(3)));
    }
}
