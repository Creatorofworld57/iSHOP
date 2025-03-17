package org.local.meeting.Utils.JwtUtils;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;


import java.util.Base64;


public class EncryptionOfId {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256; // Используем 256-битный ключ (если поддерживается)
    private static final int GCM_TAG_LENGTH = 128; // Длина тега аутентификации (в битах)
    private static final int IV_LENGTH = 12; // Рекомендуемая длина IV (12 байт)
    SecretKey key ;



    /**
     * Шифрование user_id
     */
    public String encrypt(String userId) {


        return Base64.getEncoder().encodeToString(userId.getBytes());
    }

    /**
     * Дешифрование user_id
     */
    public String decrypt(String encryptedData) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        // Извлекаем IV и зашифрованные данные
        byte[] iv = new byte[IV_LENGTH];
        byte[] ciphertext = new byte[decodedData.length - IV_LENGTH];

        System.arraycopy(decodedData, 0, iv, 0, IV_LENGTH);
        System.arraycopy(decodedData, IV_LENGTH, ciphertext, 0, ciphertext.length);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes);
    }


}


