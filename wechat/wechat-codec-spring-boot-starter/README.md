# wx-codec-spring-boot-starter
### 描述
&emsp;&emsp;微信小程序、微信支付相关解密相关功能封装

### 引用
&emsp;&emsp;maven坐标： 
```xml
<dependency>
    <groupId>com.github.aqiu202.wechat</groupId>
    <artifactId>wechat-codec-spring-boot-starter</artifactId>
    <version>1.0.2</version>
</dependency>
```
### 用法
##### 配置
```yaml
wx:
  codec:
    app-id: xxx #小程序appId
    secret: xxx #小程序secret
```
### 使用
```java
public class TestCodec {

    //注入解密服务
    @Autowired
    private DecryptService decryptService;
    
    public void test() {
        String code = "xxx";
        //小程序使用code换取session
        JsonNode session = decryptService.session(code);
        String sessionKey = session.get("session_key").asText(); //获取sessionKey
        String openid = session.get("openid").asText(); //获取openid
        
        //解密小程序用户隐私信息
        String encryptedData = "xxx"; //密文
        String iv = "xxx"; //偏移向量
        JsonNode userInfo = 
            decryptService.decrypt(encryptedData, sessionKey, iv); //userInfo就是用户明文信息
        
        //如果仅用作解密小程序用户隐私信息，可以使用一下方法
        ObjectNode user = decryptService.decodeUser(encryptedData, iv, code);
        openid = user.get("openid").asText();
        userInfo = user.get("data");
        
        //解密微信支付退款信息
        String content = "xxx"; //退款信息密文
        String apiKey = "商户api秘钥";
        String xml = decryptService.decodeRefundInfo(content, apiKey); //XML格式的退款信息明文
        
    }
}
```
