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
            numb.append(rand.nextInt(10));
        }

        String digits = numb.toString();
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length(); i++) {
            if (i > 0 && i % 3 == 0) {
                formatted.append('.');
            }
            formatted.append(digits.charAt(i));
        }

        return formatted.toString();
    }

    private static String randomCharacters() {
        String alp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new SecureRandom();
        StringBuilder letters = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int randomIndex = rand.nextInt(alp.length());
            letters.append(alp.charAt(randomIndex));
        }

        return letters.toString();
    }
}