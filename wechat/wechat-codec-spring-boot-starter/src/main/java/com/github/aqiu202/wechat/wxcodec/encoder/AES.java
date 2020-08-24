package com.github.aqiu202.wechat.wxcodec.encoder;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES加密
 * @author aqiu
 */
public class AES {

    private AES() {
    }

    private final static Logger log = LoggerFactory.getLogger(AES.class);

    private static boolean initialized = false;

    /**
     * 微信小程序用户隐私信息解密
     *
     * @param content 密文
     * @param keyByte 秘钥
     * @param ivByte 向量
     * @return byte[] 明文
     */
    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding"); //NOSONAR
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("小程序用户信息解密异常：", e);
        }
        return null;
    }

    /**
     * 微信退款解密
     * @author aqiu 2019年4月16日 下午7:07:58
     * @param content 密文
     * @param keyByte 秘钥
     * @return byte[] 明文
     */
    public static byte[] decrypt(byte[] content, byte[] keyByte) {
        initialize();
        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); //NOSONAR
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); //NOSONAR
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);// 初始化
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("微信退款信息解密异常：", e);
        }
        return null;
    }

    private static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    // 生成iv
    private static AlgorithmParameters generateIV(byte[] iv) {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(iv));
            return params;
        } catch (InvalidParameterSpecException | NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
