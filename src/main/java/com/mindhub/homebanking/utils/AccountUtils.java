package com.mindhub.homebanking.utils;

import java.util.Random;

public class AccountUtils {
    public static String generateVinNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(99999999-1) +1;
        return "VIN" + randomNumber;
    }
}
