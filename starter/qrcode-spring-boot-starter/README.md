# qrcode-spring-boot-starter
### 描述
对生成二维码进行的简单的封装。

### 引用
maven坐标：
```xml
<dependency>
    <groupId>com.github.aqiu202</groupId>
    <artifactId>qrcode-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 用法
##### 配置
```yaml
qr-code:
  conf:
    size: 300 #输出二维码图片尺寸
    algorithm: 4 #logo图片生成算法
    charset: UTF-8 #二维码内容编码
    correction-level: h #容错级别，m,l,h,q 默认是h
    format: JPEG #输出二维码的编码格式
    rgb-bright: 0xffffff #二维码亮色RGB值 默认白色
    rgb-dark: 0x000000 #二维码暗色RGB值 默认黑色
    margin: 1 #二维码内容距离边框距离
    need-compress: true #是否需要压缩logo图片
    logo: http://www.aqiuqiu.com.cn:8090/pages/admin/static/images/admin.jpeg #logo图片地址
    logo-size: 45 #logo图片尺寸
``` 

##### 使用
```java
@RestController
@RequestMapping("qr")
public class TestQRCode {
    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("out")
    public void out(HttpServletResponse response, String content, QRCodeConfiguration configuration)
            throws IOException, WriterException {
        try (OutputStream out = response.getOutputStream()) {
            out.write(qrCodeService.toByteArray(content, configuration));
        }
    }
}
```
