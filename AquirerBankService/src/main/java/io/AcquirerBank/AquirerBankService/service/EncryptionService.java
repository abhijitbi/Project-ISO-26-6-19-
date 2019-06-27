package io.AcquirerBank.AquirerBankService.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

@Service
public class EncryptionService
{
    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }



}
