package com.github.aqiu202.qrcode.util;

import com.github.aqiu202.qrcode.param.QrCodeProperties;
import com.github.aqiu202.util.ImageUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 二维码生成工具类
 *
 * @author aqiu 2018/12/5
 */
public final class QRCodeHelper {

    private final static QrCodeProperties DEFAULT_CONFIGURATION = new QrCodeProperties();

    /**
     * 生成二维码
     *
     * @param content      二维码内容
     * @param configuration  二维码参数配置
     * @return 图片
     * @throws IOException IO异常
     * @throws WriterException ZXing写异常
     */
    public static BufferedImage createImage(String content, QrCodeProperties configuration)
            throws IOException, WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, configuration.getCorrectionLevel());
        hints.put(EncodeHintType.CHARACTER_SET, configuration.getCharset().name());
        hints.put(EncodeHintType.MARGIN, configuration.getMargin());
//        hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
        final int size = configuration.getSize();
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int dark = configuration.getRgbDark();
        int bright = configuration.getRgbBright();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? dark : bright);
            }
        }
        String logo = configuration.getLogo();
        if (logo == null || "".equals(logo)) {
            return image;
        }
        // 插入图片
        QRCodeHelper.insertImage(image, configuration);
        return image;
    }

    /**
     * 生成二维码
     * @param content      二维码内容
     * @return 图片
     * @throws IOException IO异常
     * @throws WriterException ZXing写异常
     */
    public static BufferedImage createImage(String content)
            throws IOException, WriterException {
        return createImage(content, DEFAULT_CONFIGURATION);
    }


    public static byte[] toByteArray(String content) throws IOException, WriterException {
        return toByteArray(content, DEFAULT_CONFIGURATION);
    }

    public static String toBase64Str(String content) throws IOException, WriterException {
        return toBase64Str(content, DEFAULT_CONFIGURATION);
    }

    public static byte[] toByteArray(String content, QrCodeProperties configuration)
            throws IOException, WriterException {
        BufferedImage image = createImage(content, configuration);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, configuration.getFormat(), outputStream);
        return outputStream.toByteArray();
    }

    public static String toBase64Str(String content, QrCodeProperties configuration)
            throws IOException, WriterException {
        return Base64.getEncoder().encodeToString(toByteArray(content, configuration));
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param configuration     配置信息
     * @throws IOException IO异常
     */
    private static void insertImage(BufferedImage source, QrCodeProperties configuration)
            throws IOException {
        InputStream inputStream = null;
        try {
            final String logo = configuration.getLogo();
            if (ImageUtils.isOuterUrl(logo)) {
                URL url = new URL(logo);
                inputStream = url.openStream();
            } else {
                inputStream = new FileInputStream(logo);
            }
            Image src = ImageIO.read(inputStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            final int logoSize = configuration.getLogoSize();
            if (configuration.isNeedCompress()) { // 压缩LOGO
                if (width > logoSize) {
                    width = logoSize;
                }
                if (height > logoSize) {
                    height = logoSize;
                }
                Image image = src.getScaledInstance(width, height, configuration.getAlgorithm());
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(image, 0, 0, null); // 绘制缩小后的图
                g.dispose();
                src = image;
            }
            final int size = configuration.getSize();
            // 插入LOGO
            Graphics2D graph = source.createGraphics();
            int x = (size - width) / 2;
            int y = (size - height) / 2;
            graph.drawImage(src, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
