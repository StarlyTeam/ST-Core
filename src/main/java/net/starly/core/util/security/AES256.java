package net.starly.core.util.security;

import javax.crypto.Cipher;
import java.security.Key;

public class AES256 {
    public static byte[] encrypt(Key key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text);
    }
}
