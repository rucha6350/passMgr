package com.rucha.PasswordManager.util;

import com.rucha.PasswordManager.constants.JWTConstants;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
@Component
public final class PasswordUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_PASSWORD = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random random = new SecureRandom();
    private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int iterations = 10000;
    private static final int keylength = 256;

    /* Method to generate the salt value. */
    public static String getSaltvalue(int length)
    {
        StringBuilder finalval = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }
        return new String(finalval);
    }

    /* Method to generate the hash value */
    public static byte[] hash(char[] password, byte[] salt)
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        }
        finally {
            spec.clearPassword();
        }
    }

    /* Method to encrypt the password using the original password and salt value. */
    public static String generateSecurePassword(String password, String salt)
    {
        String finalval = null;
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        finalval = Base64.getEncoder().encodeToString(securePassword);
        return finalval;
    }

    /*ENCRYPTION AND DECRYPTION OF PASSWORDS STORED*/
    // Method to generate AES key from password and salt using PBKDF2
    public static SecretKeySpec generateSecretKey(byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(SECRET_PASSWORD.toCharArray(), salt, iterations, keylength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // Method to encrypt using AES with a salt value
    public static String encrypt(String plainText, byte[] salt) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(salt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new javax.crypto.spec.IvParameterSpec(salt));
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedTextBytes);
    }

    // Method to decrypt using AES with a salt value
    public static String decrypt(String encryptedText, byte[] salt) throws Exception {
        SecretKeySpec secretKey = generateSecretKey(salt);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new javax.crypto.spec.IvParameterSpec(salt));
        byte[] decryptedTextBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedTextBytes);
    }
    /*ENCRYPTION AND DECRYPTION OF PASSWORDS STORED*/

    /* Method to verify if both password matches or not */
//    public static boolean verifyUserPassword(String providedPassword,
//                                             String securedPassword, String salt) {
//        boolean finalval = false;
//        /* Generate New secure password with the same salt */
//        String newSecurePassword = generateSecurePassword(providedPassword, salt);
//        /* Check if two passwords are equal */
//        finalval = newSecurePassword.equalsIgnoreCase(securedPassword);
//        return finalval;
//    }


}
