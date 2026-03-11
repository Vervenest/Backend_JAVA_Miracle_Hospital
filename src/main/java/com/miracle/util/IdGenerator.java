package com.miracle.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();

    public String generateUniqueId() {
        try {
            long timestamp = System.currentTimeMillis();
            int randomNum = random.nextInt(10000000);
            String combined = timestamp + String.valueOf(randomNum);

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(combined.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.substring(hexString.length() - 16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating ID", e);
        }
    }
}
