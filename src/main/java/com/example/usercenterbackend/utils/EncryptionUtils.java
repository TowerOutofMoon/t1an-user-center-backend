package com.example.usercenterbackend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {
    final static String INPUT_KEY = "hty";
    public static String encryptToMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            input = INPUT_KEY + input;
            // 加密
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                // 转16进制
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
