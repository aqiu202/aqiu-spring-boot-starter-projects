# hw-sms-spring-boot-starter
### 描述
&emsp;&emsp;华为云短信服务的封装 

### 引用
&emsp;&emsp;maven坐标
```xml
<dependency>
    <groupId>com.github.aqiu202.huawei</groupId>
    <artifactId>huawei-sms-spring-boot-starter</artifactId>
    <version>1.0.5</version>
</dependency>
```

### 用法
##### 配置
```yaml
#华为云短信服务配置
huawei:
  sms:
    app-key: xxx #appKey
    app-secret: xxx #appSecret
    signature: 红柿子软件 #签名
    templates: #模板配置 （key：模板类型, value：模板ID）
      register: xxx
```
##### 使用
```java
public class TestHwSms {
    
    //注入服务
    @Autowired
    private SmsTemplate smsTemplate;
    
    public void test(){
        //构造短信请求
        SmsRequest smsRequest = SmsRequest.of("register") //设置华为云短信服务模板ID
                    .setPhoneNumbers("手机号码") //设置发送手机号
                    .addParams("value"); //填充模板的变量
        
        try {
            //发送请求
            smsTemplate.send(smsRequest);
        } catch (SmsException e) {
            log.error("短信发送失败", e);
        }
    }
}
```