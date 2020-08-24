# captcha - A captcha generation engine.
## 一个简单、轻量、灵活且强大的JAVA验证码库！
基于 [kaptcha](https://github.com/penggle/kaptcha) 重构，已经发布到maven中央仓库
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>captcha</artifactId>
  <version>1.0.1</version>
</dependency>
```
项目开源地址：[https://github.com/aqiu202/captcha](https://github.com/aqiu202/captcha)

## 使用试例
```java
public class CaptchaTest {

    public static void main(String[] args) throws IOException {
        simpleTest();
        widthAndHeightTest(300, 100);
        configTest();
    }

    /**
     * <pre>简单的demo</pre>
     * @author aqiu
     **/
    private static void simpleTest() throws IOException {
        CaptchaProducer captchaProducer = new DefaultCaptchaProducer();
        String text = captchaProducer.createText();
        System.out.println("生成的验证码为：" + text);
        BufferedImage image = captchaProducer.createImage(text);
        saveToFile(image, "/tmp/simple.jpg");
    }

    /**
     * <pre>控制输出宽高的demo</pre>
     * @author aqiu
     * @param width 宽度
     * @param height 高度
     */
    private static void widthAndHeightTest(int width, int height) throws IOException {
        CaptchaProducer captchaProducer = new DefaultCaptchaProducer();
        String text = captchaProducer.createText();
        System.out.println("生成的验证码为：" + text);
        BufferedImage image = captchaProducer.createImage(text, width, height);
        saveToFile(image, "/tmp/wh.jpg");
    }

    /**
     * <pre>灵活的自定义配置</pre>
     * @author aqiu
     */
    private static void configTest() throws IOException {
        CaptchaProperties properties = new CaptchaProperties();
        properties.setWidth(250) //设置宽度
                .setHeight(80); //设置高度
        //边框配置
        properties.setHasBorder(true) //设置边框
                .setBorder(new BorderProperties().setColor(Color.GRAY.getRGB()) //设置边框颜色为灰色
                        .setThickness(2)); //设置边框厚度为2px
        //字符配置
        WordProperties wordProperties = new WordProperties()
                .setWords("CAPTCHA哼嘿哈嘻") //设置验证码字符集
                .setLength(3); //设置验证码长度
        //渲染配置
        RenderProperties renderProperties = new RenderProperties()
                .setRgbStart(0) //RGB的随机截止范围（color为null时生效），默认0
                .setRgbEnd(55) //RGB的随机截止范围（color为null时生效），默认85
//                .setColor(Color.CYAN.getRGB()) //设置字符的固定颜色
                .setFontFamilies(Arrays.asList("宋体", "楷体")) //随机字体集合
                .setFontPercentsSize(0.7f) //字体的大小（高度的比例，默认0.65f）
                .setMarginPercentsWidth(0.2f); //两边的空白宽度（宽度的百分比，默认0.15f）
        properties
                .setText(new TextProperties().setWord(wordProperties).setRender(renderProperties));
        //背景配置
        BackgroundProperties backgroundProperties = new BackgroundProperties()
                .setColorFrom(Color.WHITE.getRGB()) //背景颜色渐变-开始（RGB值），默认根据start和end的值随机产生
                .setColorTo(Color.PINK.getRGB()) //背景颜色渐变-结束（RGB值），默认根据start和end的值随机产生
//                .setRgbStart(200) //RGB的随机截止范围（color为null时生效），默认171
//                .setRgbEnd(255) //RGB的随机截止范围（color为null时生效），默认255
                ;
        properties.setBackground(backgroundProperties);
        //图像失真/噪化配置（暂时只支持点、线、普通波纹、水波纹、阴影、扭曲，配置好基础参数后可以组合使用）
        //线条的噪化方式配置
        LineProperties lineProperties = new LineProperties()
                .setColor(Color.ORANGE.getRGB()) //设置线条为固定颜色
                .setCount(1) //线条数量，默认2条
//                .setRgbStart(100) //RGB的随机起始范围（color为null时生效），默认86
//                .setRgbEnd(150) //RGB的随机截止范围（color为null时生效），默认170
                ;
        //噪点的噪化方式配置
        PointProperties pointProperties = new PointProperties()
                .setPointRate(0.03f) //噪点的覆盖率，默认是0.025
                .setColor(Color.CYAN.getRGB()) //设置固定颜色
//                .setRgbStart(0) //RGB的随机起始范围（color为null时生效），默认0
//                .setRgbEnd(255) //RGB的随机截止范围（color为null时生效），默认255
                ;
        //波纹的失真方式配置
        RippleProperties rippleProperties = new RippleProperties()
                .setWaveType(0) //波纹类型
                .setXAmplitude(5) //X轴振幅，默认5.0f
                .setYAmplitude(0) //Y轴振幅，默认0
                .setXWavelength(16) //X轴波长，默认16
                .setYWavelength(16); //Y轴波长，默认16
        //水波纹的失真方式配置
        WaterProperties waterProperties = new WaterProperties()
                .setAmplitude(1.5f) //波的振幅，默认1.5f
                .setCentreX(0.5f) //水波中心的X坐标（宽度百分比），默认0.5f（中心）
                .setCentreY(0.5f) //水波中心的Y坐标（高度百分比），默认0.5f（中心）
                .setPhase(0) //相位，默认0
                .setRadius(50) //半径，默认50
                .setWavelength(2); //波长，默认2
        //阴影的失真方式配置
        ShadowProperties shadowProperties = new ShadowProperties()
                .setShadowOnly(false) //是否只显示阴影
                .setAddMargins(false) //是否添加空隙
                .setShadowColor(Color.BLACK.getRGB()) //设置阴影为固定颜色
                .setAngle(270) //阴影的偏转角度，默认270度
                .setAngleRandom(false) //偏转角度是否随机（默认true,为true时设置的偏转角度不再生效）
                .setDistance(5) //阴影长度，默认5
                .setRadius(10) //阴影半径， 默认10
                .setOpacity(.8f); //阴影透明度，默认0.5
        //扭曲的失真方式配置
        ShearProperties shearProperties = new ShearProperties()
                .setResize(true) //是否可以调整大小
                .setXAngle(10) //扭曲的X角度，默认10.0f
                .setXAngleToggle(true) //X轴角度是否正负转换
                .setYAngle(2) //扭曲的Y角度，默认4.0f
                .setYAngleToggle(true); //Y轴角度是否正负转换
        //验证码要使用的失真方式配置（支持的所有失真/噪化方式可以自由组合）
        List<NoiseStyle> styles = Arrays.asList(
                NoiseStyle.POINT,
                NoiseStyle.LINE,
                NoiseStyle.RIPPLE,
                NoiseStyle.WATER,
                NoiseStyle.SHEAR,
                NoiseStyle.SHADOW
        );
        properties.setNoise(new NoiseProperties()
                .setConfigurations(new NoiseConfiguration().setLine(lineProperties)
                        .setPoint(pointProperties).setRipple(rippleProperties)
                        .setShadow(shadowProperties).setShear(shearProperties)
                        .setWater(waterProperties))
                .setStyles(styles));
        DefaultCaptchaProducer captchaProducer = new DefaultCaptchaProducer(properties);
        //自定义添加边框处理
//        captchaProducer.setBorderProducer(new DefaultBorderProducer());
        //自定义验证码字符生成方式
//        captchaProducer.setTextProducer(new DefaultTextCreator());
        //自定义验证码渲染方式
//        captchaProducer.setWordRenderer(new DefaultWordRenderer());
        //自定义背景
//        captchaProducer.setBackgroundProducer(new DefaultBackgroundProducer());
        //自定义图像失真处理
//        captchaProducer.setNoiseProducer(new CombiningNoiseProducer(
//                new DelegableFilterNoiseProducer(new SmartBlurFilter())));
        String text = captchaProducer.createText();
        System.out.println("生成的验证码为：" + text);
        BufferedImage image = captchaProducer.createImage(text);
        saveToFile(image, "/tmp/config.jpg");
    }

    private static void saveToFile(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "JPEG", new File(path));
    }
}
```