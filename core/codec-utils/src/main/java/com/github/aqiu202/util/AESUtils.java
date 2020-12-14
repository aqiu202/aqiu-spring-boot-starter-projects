package com.github.aqiu202.util;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>AES加密算法工具类</b>
 * @author aqiu 2020/6/15 6:04 下午
 **/
public abstract class AESUtils {

    private static final Logger log = LoggerFactory.getLogger(AESUtils.class);

    private static final String ALGORITHM_AES = "AES";
    // BouncyCastleProvider是否已加载标志
    private volatile static boolean initialized = false;
    private static final byte[] DEFAULT_AES_VI = "0102030405060708"
            .getBytes(StandardCharsets.UTF_8);
    private static final AESMode DEFAULT_AES_MODE = AESMode.CBC;
    private static final AESPadding DEFAULT_AES_PADDING = AESPadding.PKCS5Padding;

    /**
     * AES加密
     *
     * @param aesMode   加密模式
     * @param padding   补码方式
     * @param secretKey 秘钥
     * @param ivKey     向量偏移量
     * @param content   要加密的文本
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(AESMode aesMode, AESPadding padding, byte[] content,
            byte[] secretKey,
            byte[] ivKey) {
        // 1.根据指定算法AES自成密码器
        String encryptMode = buildAlgorithm(aesMode, padding);
        try {
            // 2.1 如果是NOPADDING，加密的字符bytes长度必须是16的倍数；
            // 2.2 Java目前不支持ZEROPADDING
            // 2.3 如果是PKCS7Padding，需要调用BouncyCastleProvider让java支持PKCS7Padding
            if (padding == AESPadding.NoPadding) {
                content = paddingBlank(content);
            } else if (padding == AESPadding.PKCS7Padding) {
                initialize();
            }
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM_AES);
            // 3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为向量iv
            Cipher cipher = Cipher.getInstance(encryptMode);
            // 4.ECB模式不需要使用IV，其他的模式都可以添加向量iv（不是必需）
            if (aesMode == AESMode.ECB) {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            } else {
                IvParameterSpec iv = new IvParameterSpec(ivKey);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            }
            // 5.根据密码器的初始化方式--加密：将数据加密并返回
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("AES加密异常", e);
            // 6.结果返回，错误返回null
            return null;
        }
    }

    public static byte[] encrypt(String aesMode, String padding, byte[] content,
            byte[] secretKey,
            byte[] ivKey) {
        return encrypt(AESMode.valueOf(aesMode), AESPadding.valueOf(padding), content, secretKey,
                ivKey);
    }

    public static byte[] encrypt(AESMode aesMode, AESPadding padding, byte[] content,
            byte[] secretKey) {
        return encrypt(aesMode, padding, content, secretKey, DEFAULT_AES_VI);
    }

    public static byte[] encrypt(byte[] content, byte[] secretKey, byte[] ivKey) {
        return encrypt(DEFAULT_AES_MODE, DEFAULT_AES_PADDING, content, secretKey, ivKey);
    }

    public static byte[] encrypt(byte[] content, byte[] secretKey) {
        return encrypt(DEFAULT_AES_MODE, DEFAULT_AES_PADDING, content, secretKey, DEFAULT_AES_VI);
    }

    public static byte[] decrypt(AESMode aesMode, AESPadding padding, byte[] content,
            byte[] secretKey,
            byte[] ivKey) {
        // 1.根据指定算法AES自成密码器
        String encryptMode = buildAlgorithm(aesMode, padding);
        try {
            // 2.1 如果是NOPADDING，加密的字符bytes长度必须是16的倍数；
            // 2.2 Java目前不支持ZEROPADDING
            // 2.3 如果是PKCS7Padding，需要调用BouncyCastleProvider让java支持PKCS7Padding
            if (padding == AESPadding.NoPadding) {
                content = paddingBlank(content);
            } else if (padding == AESPadding.PKCS7Padding) {
                initialize();
            }
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM_AES);
            // 3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为向量iv
            Cipher cipher = Cipher.getInstance(encryptMode);
            // 4.ECB模式不需要使用IV，其他的模式都可以添加向量iv（不是必需）
            if (aesMode == AESMode.ECB) {
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
            } else {
                IvParameterSpec iv = new IvParameterSpec(ivKey);
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            }
            // 5.AES解密，并返回
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("AES解密异常", e);
            // 6.结果返回，错误返回null
            return null;
        }
    }

    public static byte[] decrypt(String aesMode, String padding, byte[] content,
            byte[] secretKey,
            byte[] ivKey) {
        return decrypt(AESMode.valueOf(aesMode), AESPadding.valueOf(padding), content, secretKey,
                ivKey);
    }

    public static byte[] decrypt(AESMode aesMode, AESPadding padding, byte[] encoded,
            byte[] secretKey) {
        return decrypt(aesMode, padding, encoded, secretKey, DEFAULT_AES_VI);
    }

    public static byte[] decrypt(byte[] encoded, byte[] secretKey, byte[] ivKey) {
        return decrypt(DEFAULT_AES_MODE, DEFAULT_AES_PADDING, encoded, secretKey, ivKey);
    }

    public static byte[] decrypt(byte[] encoded, byte[] secretKey) {
        return decrypt(DEFAULT_AES_MODE, DEFAULT_AES_PADDING, encoded, secretKey, DEFAULT_AES_VI);
    }

    private static String buildAlgorithm(AESMode aesMode, AESPadding aesPadding) {
        return ALGORITHM_AES + "/" + aesMode.name() + "/" + aesPadding.name();
    }

    /**
     * 加载BouncyCastleProvider
     */
    private static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    private static byte[] paddingBlank(byte[] content) {
        int len = content.length;
        int m = len % 16;
        if (m != 0) {
            StringBuilder builder = new StringBuilder(new String(content, StandardCharsets.UTF_8));
            for (int i = 0; i < 16 - m; i++) {
                builder.append(" ");
            }
            return builder.toString().getBytes(StandardCharsets.UTF_8);
        }
        return content;
    }

    public enum AESMode {
        /**
         * 电码本模式
         */
        ECB,
        /**
         * 密码分组链接模式
         */
        CBC,
        /**
         * 计算器模式
         */
        CTR,
        /**
         * 密码反馈模式
         */
        CFB,
        /**
         * 输出反馈模式
         */
        OFB
    }

    public enum AESPadding {
        /**
         * 空格填充
         */
        NoPadding,
        /**
         *
         */
        PKCS5Padding,
        ISO10126Padding,
        PKCS7Padding
    }

}
