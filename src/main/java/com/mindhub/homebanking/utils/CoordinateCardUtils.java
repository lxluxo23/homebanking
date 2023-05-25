package com.mindhub.homebanking.utils;

import java.util.*;

public class CoordinateCardUtils {
    private static final int CARD_SIZE = 6;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 99;

    public static Map<String, Integer> generateCoordinates() {
        Map<String, Integer> coordinates = new HashMap<>();
        Random random = new Random();
        Set<Integer> generatedNumbers = new HashSet<>();

        for (int i = 1; i <= CARD_SIZE; i++) {
            for (int j = 1; j <= CARD_SIZE; j++) {
                String key = getColumnLetter(j) + i;
                int randomNum;
                do {
                    randomNum = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
                } while (generatedNumbers.contains(randomNum));

                generatedNumbers.add(randomNum);
                coordinates.put(key, randomNum);
            }
        }
        formatCoordinates(coordinates);
        return coordinates;
    }

    public static String getColumnLetter(int column) {
        return String.valueOf((char) (column + 64));
    }

    public static void formatCoordinates(Map<String, Integer> coordinates) {
        for (String key : coordinates.keySet()) {
            int number = coordinates.get(key);
            if (number < 10) {
                coordinates.put(key, Integer.parseInt("0" + number));
            }
        }
    }
}
