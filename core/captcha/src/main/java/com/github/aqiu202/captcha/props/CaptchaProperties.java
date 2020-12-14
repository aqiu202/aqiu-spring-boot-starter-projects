package com.github.aqiu202.captcha.props;

import com.github.aqiu202.captcha.noise.impl.NoiseStyle;
import com.github.aqiu202.captcha.text.WordRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaptchaProperties {

    /**
     * 验证码图片宽度，默认200
     */
    private int width = WordRenderer.DEFAULT_WIDTH;
    /**
     * 验证码图片高度，默认80
     */
    private int height = WordRenderer.DEFAULT_HEIGHT;

    private TextProperties text = new TextProperties();

    private BackgroundProperties background = new BackgroundProperties();

    private boolean hasBorder;

    private BorderProperties border = new BorderProperties();

    private NoiseProperties noise = new NoiseProperties();

    public int getWidth() {
        return width;
    }

    public CaptchaProperties setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public CaptchaProperties setHeight(int height) {
        this.height = height;
        return this;
    }

    public TextProperties getText() {
        return text;
    }

    public CaptchaProperties setText(
            TextProperties text) {
        this.text = text;
        return this;
    }

    public BackgroundProperties getBackground() {
        return background;
    }

    public CaptchaProperties setBackground(
            BackgroundProperties background) {
        this.background = background;
        return this;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public CaptchaProperties setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return this;
    }

    public BorderProperties getBorder() {
        return border;
    }

    public CaptchaProperties setBorder(
            BorderProperties border) {
        this.border = border;
        return this;
    }

    public NoiseProperties getNoise() {
        return noise;
    }

    public CaptchaProperties setNoise(
            NoiseProperties noise) {
        this.noise = noise;
        return this;
    }

    public static class TextProperties {

        /**
         * 验证码字符集合及产生规则配置
         */
        private WordProperties word = new WordProperties();
        /**
         * 验证码图片渲染配置
         */
        private RenderProperties render = new RenderProperties();

        public WordProperties getWord() {
            return word;
        }

        public TextProperties setWord(WordProperties word) {
            this.word = word;
            return this;
        }

        public RenderProperties getRender() {
            return render;
        }

        public TextProperties setRender(
                RenderProperties render) {
            this.render = render;
            return this;
        }
    }

    public static class WordProperties {

        /**
         * 验证码词汇集合
         */
        private String words;
        /**
         * 验证码长度，默认4
         */
        private Integer length;

        public String getWords() {
            return words;
        }

        public WordProperties setWords(String words) {
            this.words = words;
            return this;
        }

        public Integer getLength() {
            return length;
        }

        public WordProperties setLength(Integer length) {
            this.length = length;
            return this;
        }
    }

    public static class RenderProperties {

        /**
         * 字体集合，默认 ["Arial", "Courier"]
         */
        private List<String> fontFamilies = new ArrayList<>();
        /**
         * 验证码的颜色（RGB值），默认根据start和end的值随机产生
         */
        private Integer color;
        /**
         * 色光的三原色的数值范围（0-255），默认0
         */
        private Integer rgbStart;
        /**
         * 色光的三原色的数值范围（0-255），默认85
         */
        private Integer rgbEnd;
        /**
         * 验证码两边的空白宽度（宽度百分比）默认0.15f
         */
        private Float marginPercentsWidth;
        /**
         * 验证码字符的大小（高度百分比）默认0.65f
         */
        private Float fontPercentsSize;

        public List<String> getFontFamilies() {
            return fontFamilies;
        }

        public RenderProperties setFontFamilies(List<String> fontFamilies) {
            this.fontFamilies = fontFamilies;
            return this;
        }

        public Integer getColor() {
            return color;
        }

        public RenderProperties setColor(Integer color) {
            this.color = color;
            return this;
        }

        public Integer getRgbStart() {
            return rgbStart;
        }

        public RenderProperties setRgbStart(Integer rgbStart) {
            this.rgbStart = rgbStart;
            return this;
        }

        public Integer getRgbEnd() {
            return rgbEnd;
        }

        public RenderProperties setRgbEnd(Integer rgbEnd) {
            this.rgbEnd = rgbEnd;
            return this;
        }

        public Float getMarginPercentsWidth() {
            return marginPercentsWidth;
        }

        public RenderProperties setMarginPercentsWidth(Float marginPercentsWidth) {
            this.marginPercentsWidth = marginPercentsWidth;
            return this;
        }

        public Float getFontPercentsSize() {
            return fontPercentsSize;
        }

        public RenderProperties setFontPercentsSize(Float fontPercentsSize) {
            this.fontPercentsSize = fontPercentsSize;
            return this;
        }
    }

    public static class BackgroundProperties {

        /**
         * 背景颜色渐变-开始（RGB值），默认根据start和end的值随机产生
         */
        private Integer colorFrom;
        /**
         * 背景颜色渐变-结束（RGB值），默认根据start和end的值随机产生
         */
        private Integer colorTo;
        /**
         * 色光的三原色的数值范围（0-255），默认171
         */
        private Integer rgbStart;
        /**
         * 色光的三原色的数值范围（0-255），默认255
         */
        private Integer rgbEnd;

        public Integer getColorFrom() {
            return colorFrom;
        }

        public BackgroundProperties setColorFrom(Integer colorFrom) {
            this.colorFrom = colorFrom;
            return this;
        }

        public Integer getColorTo() {
            return colorTo;
        }

        public BackgroundProperties setColorTo(Integer colorTo) {
            this.colorTo = colorTo;
            return this;
        }

        public Integer getRgbStart() {
            return rgbStart;
        }

        public BackgroundProperties setRgbStart(Integer rgbStart) {
            this.rgbStart = rgbStart;
            return this;
        }

        public Integer getRgbEnd() {
            return rgbEnd;
        }

        public BackgroundProperties setRgbEnd(Integer rgbEnd) {
            this.rgbEnd = rgbEnd;
            return this;
        }
    }

    public static class BorderProperties {

        /**
         * 边框颜色（RGB值），默认黑色
         */
        private Integer color;
        /**
         * 边框厚度，默认1
         */
        private Integer thickness;

        public Integer getColor() {
            return color;
        }

        public BorderProperties setColor(Integer color) {
            this.color = color;
            return this;
        }

        public Integer getThickness() {
            return thickness;
        }

        public BorderProperties setThickness(Integer thickness) {
            this.thickness = thickness;
            return this;
        }
    }

    public static class NoiseProperties {

        /**
         * 噪化方式配置（多种方式可组合，默认[LINE,SHEAR,SHADOW]）
         */
        private List<NoiseStyle> styles = Arrays.asList(
                NoiseStyle.LINE,
                NoiseStyle.SHEAR,
                NoiseStyle.SHADOW
        );
        /**
         * 噪化配置
         */
        private NoiseConfiguration configurations = new NoiseConfiguration();

        public List<NoiseStyle> getStyles() {
            return styles;
        }

        public NoiseProperties setStyles(
                List<NoiseStyle> styles) {
            this.styles = styles;
            return this;
        }

        public NoiseConfiguration getConfigurations() {
            return configurations;
        }

        public NoiseProperties setConfigurations(
                NoiseConfiguration configurations) {
            this.configurations = configurations;
            return this;
        }
    }

    public static class NoiseConfiguration {

        /**
         * 波纹配置
         */
        private RippleProperties ripple = new RippleProperties();
        /**
         * 水波纹配置
         */
        private WaterProperties water = new WaterProperties();
        /**
         * 阴影配置
         */
        private ShadowProperties shadow = new ShadowProperties();
        /**
         * 扭曲/剪切配置
         */
        private ShearProperties shear = new ShearProperties();
        /**
         * 点配置
         */
        private PointProperties point = new PointProperties();
        /**
         * 线配置
         */
        private LineProperties line = new LineProperties();

        public RippleProperties getRipple() {
            return ripple;
        }

        public NoiseConfiguration setRipple(
                RippleProperties ripple) {
            this.ripple = ripple;
            return this;
        }

        public WaterProperties getWater() {
            return water;
        }

        public NoiseConfiguration setWater(
                WaterProperties water) {
            this.water = water;
            return this;
        }

        public ShadowProperties getShadow() {
            return shadow;
        }

        public NoiseConfiguration setShadow(
                ShadowProperties shadow) {
            this.shadow = shadow;
            return this;
        }

        public ShearProperties getShear() {
            return shear;
        }

        public NoiseConfiguration setShear(
                ShearProperties shear) {
            this.shear = shear;
            return this;
        }

        public PointProperties getPoint() {
            return point;
        }

        public NoiseConfiguration setPoint(
                PointProperties point) {
            this.point = point;
            return this;
        }

        public LineProperties getLine() {
            return line;
        }

        public NoiseConfiguration setLine(
                LineProperties line) {
            this.line = line;
            return this;
        }
    }

    public static class RippleProperties extends TransformProperties {

        /**
         * 波类型（0 sine,1 sawtooth, 2 triangle, 3 noise）
         */
        private int waveType = 0;
        /**
         * X轴振幅，默认5.0f
         */
        private float xAmplitude = 5.0f;
        /**
         * Y轴振幅，默认5.0f
         */
        private float yAmplitude = 0.0f;
        /**
         * X轴波长，默认16.0f
         */
        private float xWavelength = 16.0f;
        /**
         * Y轴波长，默认16.0f
         */
        private float yWavelength = 16.0f;

        public int getWaveType() {
            return waveType;
        }

        public RippleProperties setWaveType(int waveType) {
            this.waveType = waveType;
            return this;
        }

        public float getXAmplitude() {
            return xAmplitude;
        }

        public RippleProperties setXAmplitude(float xAmplitude) {
            this.xAmplitude = xAmplitude;
            return this;
        }

        public float getYAmplitude() {
            return yAmplitude;
        }

        public RippleProperties setYAmplitude(float yAmplitude) {
            this.yAmplitude = yAmplitude;
            return this;
        }

        public float getXWavelength() {
            return xWavelength;
        }

        public RippleProperties setXWavelength(float xWavelength) {
            this.xWavelength = xWavelength;
            return this;
        }

        public float getYWavelength() {
            return yWavelength;
        }

        public RippleProperties setYWavelength(float yWavelength) {
            this.yWavelength = yWavelength;
            return this;
        }
    }

    public static class ShearProperties extends TransformProperties {

        /**
         * 扭曲的X角度，默认10.0f
         */
        private float xAngle = 10.0f;
        /**
         * 扭曲的Y角度，默认4.0f
         */
        private float yAngle = 4.0f;
        /**
         * 是否可以调整大小
         */
        private boolean resize = true;

        /**
         * X轴角度是否正负转换
         */
        private boolean xAngleToggle = true;
        /**
         * Y轴角度是否正负转换
         */
        private boolean yAngleToggle = true;

        public float getXAngle() {
            return xAngle;
        }

        public ShearProperties setXAngle(float xAngle) {
            this.xAngle = xAngle;
            return this;
        }

        public float getYAngle() {
            return yAngle;
        }

        public ShearProperties setYAngle(float yAngle) {
            this.yAngle = yAngle;
            return this;
        }

        public boolean isResize() {
            return resize;
        }

        public ShearProperties setResize(boolean resize) {
            this.resize = resize;
            return this;
        }

        public boolean isXAngleToggle() {
            return xAngleToggle;
        }

        public ShearProperties setXAngleToggle(boolean xAngleToggle) {
            this.xAngleToggle = xAngleToggle;
            return this;
        }

        public boolean isYAngleToggle() {
            return yAngleToggle;
        }

        public ShearProperties setYAngleToggle(boolean yAngleToggle) {
            this.yAngleToggle = yAngleToggle;
            return this;
        }
    }

    public static class ShadowProperties {

        /**
         * 阴影半径，默认10
         */
        private float radius = 10;
        /**
         * 阴影角度，默认270f度
         */
        private float angle = 270f;
        /**
         * 阴影角度是否随机，默认true
         */
        private boolean angleRandom = true;
        /**
         * 阴影距离，默认5
         */
        private float distance = 5;
        /**
         * 阴影透明度，默认0.5f
         */
        private float opacity = .5f;
        /**
         * 是否添加边距，默认false
         */
        private boolean addMargins = false;
        /**
         * 是否只显示阴影，默认false
         */
        private boolean shadowOnly = false;
        /**
         * 阴影颜色（RGB值），默认黑色
         */
        private int shadowColor = 0xff000000;

        public float getRadius() {
            return radius;
        }

        public ShadowProperties setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public float getAngle() {
            return angle;
        }

        public ShadowProperties setAngle(float angle) {
            this.angle = angle;
            return this;
        }

        public float getDistance() {
            return distance;
        }

        public ShadowProperties setDistance(float distance) {
            this.distance = distance;
            return this;
        }

        public float getOpacity() {
            return opacity;
        }

        public ShadowProperties setOpacity(float opacity) {
            this.opacity = opacity;
            return this;
        }

        public boolean isAddMargins() {
            return addMargins;
        }

        public ShadowProperties setAddMargins(boolean addMargins) {
            this.addMargins = addMargins;
            return this;
        }

        public boolean isShadowOnly() {
            return shadowOnly;
        }

        public ShadowProperties setShadowOnly(boolean shadowOnly) {
            this.shadowOnly = shadowOnly;
            return this;
        }

        public int getShadowColor() {
            return shadowColor;
        }

        public ShadowProperties setShadowColor(int shadowColor) {
            this.shadowColor = shadowColor;
            return this;
        }

        public boolean isAngleRandom() {
            return angleRandom;
        }

        public ShadowProperties setAngleRandom(boolean angleRandom) {
            this.angleRandom = angleRandom;
            return this;
        }
    }

    public static class WaterProperties extends TransformProperties {

        /**
         * 波长，默认2
         */
        private float wavelength = 2;
        /**
         * 波的振幅，默认1.5f
         */
        private float amplitude = 1.5f;
        /**
         * 相位，默认0
         */
        private float phase = 0;
        /**
         * 水波中心的X坐标（宽度百分比），默认0.5f（中心）
         */
        private float centreX = 0.5f;
        /**
         * 水波中心的Y坐标（高度百分比），默认0.5f（中心）
         */
        private float centreY = 0.5f;
        /**
         * 半径，默认50
         */
        private float radius = 50;

        public float getWavelength() {
            return wavelength;
        }

        public WaterProperties setWavelength(float wavelength) {
            this.wavelength = wavelength;
            return this;
        }

        public float getAmplitude() {
            return amplitude;
        }

        public WaterProperties setAmplitude(float amplitude) {
            this.amplitude = amplitude;
            return this;
        }

        public float getPhase() {
            return phase;
        }

        public WaterProperties setPhase(float phase) {
            this.phase = phase;
            return this;
        }

        public float getCentreX() {
            return centreX;
        }

        public WaterProperties setCentreX(float centreX) {
            this.centreX = centreX;
            return this;
        }

        public float getCentreY() {
            return centreY;
        }

        public WaterProperties setCentreY(float centreY) {
            this.centreY = centreY;
            return this;
        }

        public float getRadius() {
            return radius;
        }

        public WaterProperties setRadius(float radius) {
            this.radius = radius;
            return this;
        }
    }

    public static class PointProperties {

        /**
         * 噪点率，默认0.025f
         */
        private Float pointRate;
        /**
         * 色光的三原色的数值范围（0-255），默认0
         */
        private Integer rgbStart;
        /**
         * 色光的三原色的数值范围（0-255），默认255
         */
        private Integer rgbEnd;
        /**
         * 点颜色（RGB值），默认根据start和end的值随机产生
         */
        private Integer color;

        public Float getPointRate() {
            return pointRate;
        }

        public PointProperties setPointRate(Float pointRate) {
            this.pointRate = pointRate;
            return this;
        }

        public Integer getRgbStart() {
            return rgbStart;
        }

        public PointProperties setRgbStart(Integer rgbStart) {
            this.rgbStart = rgbStart;
            return this;
        }

        public Integer getRgbEnd() {
            return rgbEnd;
        }

        public PointProperties setRgbEnd(Integer rgbEnd) {
            this.rgbEnd = rgbEnd;
            return this;
        }

        public Integer getColor() {
            return color;
        }

        public PointProperties setColor(Integer color) {
            this.color = color;
            return this;
        }
    }

    public static class LineProperties {

        /**
         * 线条颜色（RGB值），默认根据start和end的值随机产生
         */
        private Integer color;
        /**
         * 色光的三原色的数值范围（0-255），默认86
         */
        private Integer rgbStart;
        /**
         * 色光的三原色的数值范围（0-255），默认170
         */
        private Integer rgbEnd;
        /**
         * 线条的数量，默认2
         */
        private int count = 2;

        public Integer getColor() {
            return color;
        }

        public LineProperties setColor(Integer color) {
            this.color = color;
            return this;
        }

        public Integer getRgbStart() {
            return rgbStart;
        }

        public LineProperties setRgbStart(Integer rgbStart) {
            this.rgbStart = rgbStart;
            return this;
        }

        public Integer getRgbEnd() {
            return rgbEnd;
        }

        public LineProperties setRgbEnd(Integer rgbEnd) {
            this.rgbEnd = rgbEnd;
            return this;
        }

        public int getCount() {
            return count;
        }

        public LineProperties setCount(int count) {
            this.count = count;
            return this;
        }
    }

    public static class TransformProperties {

        /**
         * 插值方式（0 NEAREST_NEIGHBOUR, 1 BILINEAR）
         */
        private int interpolation = 1;
        /**
         * 边缘处理（0 ZERO, 1 CLAMP ,2 WRAP, 3 RGB_CLAMP）
         */
        private int edgeAction = 3;

        public int getInterpolation() {
            return interpolation;
        }

        public TransformProperties setInterpolation(int interpolation) {
            this.interpolation = interpolation;
            return this;
        }

        public int getEdgeAction() {
            return edgeAction;
        }

        public TransformProperties setEdgeAction(int edgeAction) {
            this.edgeAction = edgeAction;
            return this;
        }
    }
}
