package com.swingy.model;

import java.security.SecureRandom;

public class Utils {
    public static int getRandomNumber(int a, int b) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(b - a) + a; // Generate random number between a and b
    }
}
