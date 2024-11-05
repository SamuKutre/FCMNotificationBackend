package com.example.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CodeChallengeGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		   // Generate a random code verifier
        String codeVerifier = generateCodeVerifier();

        // Generate code challenge
        String codeChallenge = generateCodeChallenge(codeVerifier);

        // Code challenge method
        String codeChallengeMethod = "S256";

        System.out.println("Code Verifier: " + codeVerifier);
        System.out.println("Code Challenge: " + codeChallenge);
        System.out.println("Code Challenge Method: " + codeChallengeMethod);
	}
	// Generate a random code verifier
    private static String generateCodeVerifier() {
        // Code verifier should be a high-entropy cryptographic random string
        // For simplicity, you can generate a random string of length between 43 to 128 characters
        StringBuilder sb = new StringBuilder();
        int length = 43 + (int) (Math.random() * (128 - 43 + 1));
        String unreservedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * unreservedCharacters.length());
            sb.append(unreservedCharacters.charAt(index));
        }
        return sb.toString();
    }

    // Generate code challenge using SHA-256 and Base64URL encoding without padding
    private static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes());
            return base64UrlEncodeWithoutPadding(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Base64URL encoding without padding
    private static String base64UrlEncodeWithoutPadding(byte[] bytes) {
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        // Replace characters as per specification
        return base64.replace('+', '-').replace('/', '_');
    }

}
