package com.mindhub.homebanking.utils;

import java.util.Random;

public class CardUtils {

    public static String generateCardNumber(){
        Random random = new Random();
        int firstGroup = random.nextInt(10000);
        int secondGroup = random.nextInt(10000);
        int thirdGroup = random.nextInt(10000);
        int fourthGroup = random.nextInt(10000);
        return String.format("%04d-%04d-%04d-%04d", firstGroup, secondGroup, thirdGroup, fourthGroup);
    }

    public static int createCVVNumber() {
        Random random = new Random();
        return random.nextInt(900)+100;
    }
}
