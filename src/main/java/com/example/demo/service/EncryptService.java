package com.example.demo.service;

import com.example.demo.exception.EncryptException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptService {
    private static final String key = "123456789012345678901234";

    private static SecretKey getSecretKey() {
        return new SecretKeySpec(key.getBytes(), "DESede");
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
            throw new EncryptException("Erro ao descriptografar2");
        }
    }
}
