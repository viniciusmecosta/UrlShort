package com.example.demo.service;

import com.example.demo.exception.EncryptException;
import io.github.cdimascio.dotenv.Dotenv;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptService {
    static Dotenv dotenv = Dotenv.load();
    private static final String keyAES = dotenv.get("SECRET_KEY_AES");
    private static final String key = dotenv.get("SECRET_KEY");

    private static SecretKey getSecretKey() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DESede");
        return secretKeySpec;
    }

    private static SecretKey getSecretKeyAES() throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(keyAES.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
        }

    public static String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encrypted = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new EncryptException("Erro ao criptografar");
        }
    }

    public static String decrypt(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            byte[] decoded = Base64.getDecoder().decode(encryptedMessage);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new EncryptException("Erro ao descriptografar");
        }
    }

    public static String encryptAES(String message) {
        try {
            SecretKey secretKey = getSecretKeyAES();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedMessageBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encryptedMessageBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedMessageBytes, 0, combined, iv.length, encryptedMessageBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new EncryptException("Erro ao criptografar com AES");
        }
    }

    public static String decryptAES(String encryptedMessageBase64) {
        try {
            SecretKey secretKey = getSecretKeyAES();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] combined = Base64.getDecoder().decode(encryptedMessageBase64);

            byte[] iv = new byte[16];
            if (combined.length < iv.length) {
                throw new EncryptException("Mensagem criptografada inválida: muito curta para conter IV.");
            }
            System.arraycopy(combined, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            byte[] encryptedMessageBytes = new byte[combined.length - iv.length];
            System.arraycopy(combined, iv.length, encryptedMessageBytes, 0, encryptedMessageBytes.length);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedMessageBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptException("Erro ao descriptografar com AES" );
        }
    }
}