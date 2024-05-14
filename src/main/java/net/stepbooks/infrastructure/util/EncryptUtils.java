package net.stepbooks.infrastructure.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

    /**
     * 客户端先将密码用md5加密传到后台，后台再将md5进行2次加密之后存到数据库
     *
     * @param md5
     * @return
     */
    public static String encodePassword(String md5) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(md5);
    }

    /**
     * 判断客户端传入的md5密码是否和数据库的加密密码匹配
     *
     * @param md5
     * @param encodedPassword
     * @return
     */
    public static boolean matches(String md5, String encodedPassword) {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(md5, encodedPassword);
    }

    public static String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static void main(String[] args) {
        System.out.println(md5("admin"));
        System.out.println(encodePassword("21232f297a57a5a743894a0e4a801fc3"));
        System.out.println(matches("21232f297a57a5a743894a0e4a801fc3",
                "{bcrypt}$2a$10$6wPIE3NHmfHpKHvPpObpB.g3vnV02Rbg.u3irtewFHNIQ39Xk48pm"));
    }
}
