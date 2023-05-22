package com.mindhub.homebanking.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CoordinateCardUtils {
    private static final int CARD_SIZE = 6;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 99;

    public int[][] generateCoordinates(){
        int[][] coordinates = new int[CARD_SIZE][CARD_SIZE];
        Random random = new Random();

        Set<Integer> generatedNumbers = new HashSet<>();

        for (int i = 0; i < CARD_SIZE; i++){
            for (int j = 0; j < CARD_SIZE; j++){
                int randomNum;
                do {
                    randomNum = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
                } while (generatedNumbers.contains(randomNum));

                generatedNumbers.add(randomNum);
                coordinates[i][j] = randomNum;
            }
        }
        return coordinates;
    }
}
