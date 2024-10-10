package org.wesley.ecommerce.application.utility;

import java.util.Random;

public class CodeGenerate {

    public static String randomCode() {
        String numbers = randomDigits();

        String letters = randomCharacters();

        return numbers + letters;
    }

    private static String randomDigits() {
        Random rand = new Random();
        StringBuilder numb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            numb.append(rand.nextInt(10));
        }

        return numb.toString();
    }

    private static String randomCharacters() {
        String alp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String characters = "!@#$%^&*()_-";
        String set = alp + characters;
        Random rand = new Random();
        StringBuilder letters = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int randomIndex = rand.nextInt(set.length());
            letters.append(set.charAt(randomIndex));
        }

        return letters.toString();
    }
}