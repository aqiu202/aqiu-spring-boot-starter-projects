# wx-pay-spring-boot-starter
### 描述
&emsp;&emsp;微信支付相关功能封装

### 引用
&emsp;&emsp;maven坐标： 
```xml
<dependency>
    <groupId>com.github.aqiu202.wechat</groupId>
    <artifactId>wechat-pay-spring-boot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```
### 用法
##### 配置
```yaml
wx:
  pay:
    props:
      app-i-d: xxx #微信appid
      mch-i-d: xxx #商户ID
      key: xxx #api秘钥
      cert: classpath:cert/apiclient_cert.p12 #证书地址
      sign-type: MD5 #加密方式 MD5或者hmacsha256 默认MD5
      trade-type: JSAPI #支付方式 JSAPI 小程序，NATIVE 扫码，APP APP支付，MWEB H5支付
      notify-url: https://xxx #支付成功回调地址
      refund-notify-url: https://xxx #退款成功回调地址
```
##### 使用

```java
@Service
public class TestWXPay {
    //注入方式 1
    @Autowired
    public MyWxPayConfig myWxPayConfig;
    
    //注入方式 2
    @Autowired
    public WXPayHelper wxPayHelper;
    
    public void test() {
        //构造参数
        Map<String, String> data = new HashMap<>();
        data.put("body", "正望餐厅午餐一份");
        data.put("out_trade_no", item.getOrderNo());
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", totalFee);// 分
        data.put("spbill_create_ip", this.getLocalHostAddress());
        data.put("product_id", String.valueOf(item.getId()));
        data.put("openid", openid);
        //第一种注入方式的使用
        new WXPay(myWxPayConfig).unifiedOrder(data);
        //第二种注入方式的使用
        wxPayHelper.unifiedOrder(data);
    }
}
```