# captcha-spring-boot-starter - A captcha generation engine with spring boot.
## Spring Boot集成验证码的一个既简单又强大的starter，且配置灵活！
基于 [captcha](https://github.com/aqiu202/captcha) 和Spring Boot集成，已经发布到maven中央仓库
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>captcha-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```
项目开源地址：[https://github.com/aqiu202/captcha-spring-boot-starter](https://github.com/aqiu202/captcha-spring-boot-starter)

## 配置
```yaml
captcha:
  height: 80 #图片高度
  width: 200 #图片宽度
  has-border: true #设置图片为有边框
  noise: #噪化方式先阴影，再添加曲线，再扭曲变形最后添加噪点
    styles:
      - SHADOW
      - LINE
      - SHEAR
      - POINT
```

## 使用试例
```java
@Validated
@RestController
public class VerifyCodeController {

    @Autowired
    private CaptchaProducer producer;

    /**
     * 获取验证码
     * @author aqiu
     **/
    @GetMapping("/verify-code/code")
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String text = this.producer.createText();
        req.getSession().setAttribute("CODE", text);
        this.producer.writeToResponse(text, resp);
    }

}
```