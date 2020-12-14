package com.github.aqiu202.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.annotation.Nonnull;

/**
 * <b>加密算法的公共方法</b>
 *
 * @author aqiu 2020/6/15 6:04 下午
 **/
public final class RSAUtils {

    private RSAUtils() {
    }

    public static boolean isRsaKey(@Nonnull String key) {
        return key.startsWith("-----BEGIN");
    }

    public static String readRsaPublicKey(String key) {
        return key.substring(0, key.length() - 24).substring(26).replace("\n", "");
    }

    public static String readRsaPrivateKey(String key) {
        return key.substring(0, key.length() - 25).substring(27).replace("\n", "");
    }

    public static RSAPublicKey generateRSAPublicKey(String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.trim();
        //过滤掉前后缀和回车符号
        if (isRsaKey(key)) {
            key = readRsaPublicKey(key);
        }
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getMimeDecoder().decode(key)));
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generateRSAPublicKey(new String(key, StandardCharsets.UTF_8));
    }

    public static PrivateKey generatePrivateKey(String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        key = key.trim();
        //过滤掉前后缀和回车符号
        if (isRsaKey(key)) {
            key = readRsaPrivateKey(key);
        }
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(
                        Base64.getMimeDecoder().decode(key.getBytes(StandardCharsets.UTF_8))));
    }

    public static PrivateKey generatePrivateKey(byte[] key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return generatePrivateKey(new String(key, StandardCharsets.UTF_8));
    }
}
