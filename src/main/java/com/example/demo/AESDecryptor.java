package com.example.demo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.security.SecureRandom;

public class AESDecryptor {

   public static String encryptSecretKeyUsingAES(String secretKey, String json) {
        try {
            // Generate a random salt
            byte[] salt = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);

            // Generate a random IV
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);

            // Derive a key using PBKDF2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey derivedKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Encrypt the JSON string using AES with CBC mode and PKCS5Padding (or PKCS7Padding)
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, derivedKey, new IvParameterSpec(iv));
            byte[] encryptedBytes = cipher.doFinal(json.getBytes("UTF-8"));

            // Combine salt, IV, and ciphertext
            byte[] combinedData = new byte[salt.length + iv.length + encryptedBytes.length];
            System.arraycopy(salt, 0, combinedData, 0, salt.length);
            System.arraycopy(iv, 0, combinedData, salt.length, iv.length);
            System.arraycopy(encryptedBytes, 0, combinedData, salt.length + iv.length, encryptedBytes.length);

            // Encode the combined data to Base64
            String combinedDataBase64 = Base64.encodeBase64String(combinedData);

            return combinedDataBase64;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Encryption Error: " + e.getMessage());
        }
        return null;
    }

    public static String decryptAESUsingSecretKey(String secretKey, String encryptedData) {
        try {
            // Decode the combined data from Base64
            byte[] combinedData = Base64.decodeBase64(encryptedData);

            // Extract the salt, IV, and ciphertext from the combined data
            byte[] salt = new byte[16];
            System.arraycopy(combinedData, 0, salt, 0, 16);
            System.out.println(new String(salt));

            byte[] iv = new byte[16];
            System.arraycopy(combinedData, 16, iv, 0, 16);
            System.out.println(new String(iv));

            byte[] ciphertext = new byte[combinedData.length - 32];
            System.arraycopy(combinedData, 32, ciphertext, 0, ciphertext.length);
            System.out.println(new String(ciphertext));

            // Derive the key using PBKDF2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey derivedKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Decrypt the ciphertext using AES with CBC mode and PKCS5Padding (or PKCS7Padding)
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, derivedKey, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(ciphertext);

            // Convert the decrypted data to a string
            String decryptedJson = new String(decryptedBytes, "UTF-8");

            return decryptedJson;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Decryption Error: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        // Example usage
    	String secretKey = ".Qn+;�,4�u`N�_��{.�s�/��`��YC";
        String jsonData ="{\"username\":\"SUxBTkdP\",\"userpassword\":\"YXl1c2hpMDYwN0A=\"}";
//String encryptedData="RjtzzGUiDFbWYH6pnv5j3nS0PkESSV6SH+qfIJGDcErzdvmGKvHElD6jNQ5AWKu0";
        

        // Encrypt
        String encryptedData = encryptSecretKeyUsingAES(secretKey, jsonData);
        System.out.println("Encrypted Data: " + encryptedData);

        // Decrypt
        String decryptedData = decryptAESUsingSecretKey(secretKey, encryptedData);
        System.out.println("Decrypted Data: " + decryptedData);
    }
}