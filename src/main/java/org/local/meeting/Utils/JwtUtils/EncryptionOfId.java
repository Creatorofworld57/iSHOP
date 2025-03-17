package org.local.meeting.Utils.JwtUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionOfId {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256; // Используем 256-битный ключ (если поддерживается)
    private static final int GCM_TAG_LENGTH = 128; // Длина тега аутентификации (в битах)
    private static final int IV_LENGTH = 12; // Рекомендуемая длина IV (12 байт)
    SecretKey key = generateKey();

    public EncryptionOfId() throws Exception {
    }

    /**
     * Генерация безопасного AES ключа
     */
    @PostConstruct
    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom()); // 256-битный ключ
        return keyGenerator.generateKey();
    }
    /**
     * Шифрование user_id
     */
    public String encrypt(String userId) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

        // Генерация случайного IV
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] encryptedData = cipher.doFinal(userId.getBytes());

        // Объединяем IV и зашифрованные данные
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(combined);
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


