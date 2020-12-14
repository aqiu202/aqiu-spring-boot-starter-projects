package com.github.aqiu202.wechat.wxpay.sdk;


import com.github.aqiu202.wechat.wxpay.sdk.WXPayConstants.SignType;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WXPayUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     */
    public static Map<String, String> xmlToMap(String strXML) {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            try (InputStream stream = new ByteArrayInputStream(
                    strXML.getBytes(StandardCharsets.UTF_8))) {
                org.w3c.dom.Document doc = documentBuilder.parse(stream);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getDocumentElement().getChildNodes();
                for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                    Node node = nodeList.item(idx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                        data.put(element.getNodeName(), element.getTextContent());
                    }
                }
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger()
                    .warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}",
                            ex.getMessage(), strXML);
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     */
    public static String mapToXml(Map<String, String> data) {
        Document document;
        try {
            document = WXPayXmlUtil.newDocument();
            org.w3c.dom.Element root = document.createElement("xml");
            document.appendChild(root);
            for (String key : data.keySet()) {
                String value = data.get(key);
                if (value == null) {
                    value = "";
                }
                value = value.trim();
                org.w3c.dom.Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(value));
                root.appendChild(filed);
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            String output;
            try (StringWriter writer = new StringWriter()) {
                StreamResult result = new StreamResult(writer);
                transformer.transform(source, result);
                output = writer.getBuffer().toString();
            }
            return output;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) {
        return generateSignedXml(data, key, SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key,
            SignType signType) {
        String sign = generateSignature(data, key, signType);
        data.put(WXPayConstants.FIELD_SIGN, sign);
        return mapToXml(data);
    }

    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @param key    API密钥
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(String xmlStr, String key) {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(WXPayConstants.FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(Map<String, String> data, String key,
            SignType signType) {
        if (!data.containsKey(WXPayConstants.FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key,
            SignType signType) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
            {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return md5(sb.toString()).toUpperCase();
        } else if (SignType.HMACSHA256.equals(signType)) {
            return hmacsha256(sb.toString(), key);
        } else {
            throw new IllegalArgumentException(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String md5(String data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5"); //NOSONAR
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     */
    public static String hmacsha256(String data, String key) {
        Mac sha256HMAC;
        try {
            sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            sha256HMAC.init(secret_key);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] array = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 日志
     * @return logger
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger("wxpay java sdk");
    }

    /**
     * 获取当前时间戳，单位秒
     * @return 时间戳（单位秒）
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return 时间戳（单位毫秒）
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    public static MapHandlerBuilder receive(HttpServletRequest request) {
        return receiveAsString(request).map();
    }

    public static StringHandlerBuilder receiveAsString(HttpServletRequest request) {
        String result;
        try (InputStream in = request
                .getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            result = new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return new StringHandlerBuilder(result);
    }

    public static final class MapHandlerBuilder extends Responseable {

        private final Map<String, String> data;

        private MapHandlerBuilder(Map<String, String> data) {
            this.data = data;
        }

        public MapHandlerBuilder then(WxPayNotifyHandler handler) {
            handler.handle(this.data);
            return this;
        }

    }

    public static final class StringHandlerBuilder extends Responseable {

        private final String data;

        private StringHandlerBuilder(String data) {
            this.data = data;
        }

        public StringHandlerBuilder then(WxPayStringNotifyHandler handler) {
            handler.handle(this.data);
            return this;
        }

        public MapHandlerBuilder map() {
            return new MapHandlerBuilder(WXPayUtil.xmlToMap(this.data));
        }

    }

    public static final class ResponseWriter {

        public void write(HttpServletResponse response) {
            String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            try (BufferedOutputStream out = new BufferedOutputStream(
                    response.getOutputStream())) {
                out.write(resXml.getBytes());
                out.flush();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
