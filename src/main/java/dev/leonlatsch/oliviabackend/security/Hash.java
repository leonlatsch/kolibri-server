package dev.leonlatsch.oliviabackend.security;

import java.security.MessageDigest;

/**
 * @author Leon Latsch
 * @since 1.0.0
 */
public class Hash {

    private static final String MD5 = "MD5";
    private static final String SHA256 = "SHA256";

    public static String genMd5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            return createHash(md, str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String genSha256Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            return createHash(md, str);
        } catch (Exception e) {
            return null;
        }
    }

    private static String createHash(MessageDigest md, String str) {
        return createHexString(md.digest(str.getBytes()));
    }

    private static String createHexString(byte[] digest) {
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            if ((0xff & digest[i]) < 0x10) {
                hex.append("0" + Integer.toHexString((0xFF & digest[i])));
            } else {
                hex.append(Integer.toHexString(0xFF & digest[i]));
            }
        }
        return hex.toString();
    }

}
