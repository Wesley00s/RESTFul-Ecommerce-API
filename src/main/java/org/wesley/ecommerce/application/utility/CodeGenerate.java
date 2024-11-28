package org.wesley.ecommerce.application.utility;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerate {

    public static String randomCode() {
        String numbers = randomDigits();

        String letters = randomCharacters();

        return numbers + letters;
    }

    private static String randomDigits() {
        Random rand = new SecureRandom();
        StringBuilder numb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            numb.append(rand.nextInt(20));
        }

        return numb.toString();
    }

    private static String randomCharacters() {
        String alp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String characters = "!@#$%^&*()_-";
        String set = alp + characters;
        Random rand = new Random();
        StringBuilder letters = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int randomIndex = rand.nextInt(set.length());
            letters.append(set.charAt(randomIndex));
        }

        return letters.toString();
    }
}