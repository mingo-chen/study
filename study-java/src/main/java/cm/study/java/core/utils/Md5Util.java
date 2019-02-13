package cm.study.java.core.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

    private static ThreadLocal<MessageDigest> digestHolder = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("no algorithm");
        }
    });

    public static String digest(String plain) {
        String target = null;
        try {
            target = plain;
            MessageDigest md = digestHolder.get();
            target = Hex.encodeHexString(md.digest(target.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return target;
    }
}
