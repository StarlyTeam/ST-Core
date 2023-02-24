package net.starly.core.util;

import javax.crypto.Cipher;
import java.security.Key;

public class RSA2048 {
    public static byte[] encrypt(Key publicKey, byte[] plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText);
    }
}
