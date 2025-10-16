# aliyun-sms-spring-boot-starter
### 描述
&emsp;&emsp;阿里云短信服务的封装 

### 引用
&emsp;&emsp;maven坐标
```xml
<dependency>
    <groupId>com.github.aqiu202.aliyun</groupId>
    <artifactId>aliyun-sms-spring-boot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

### 用法
##### 配置
```yaml
#试例只是最简配置，更多配置，请查看元信息提示或者查看配置类源码
aliyun:
  mns:
    sign-name: XXX #阿里云短信服务签名
    access-key-id: xxx #阿里云短信服务应用accessKeyId
    access-key-secret: xxx #阿里云短信服务accessKeySecret
    templates: #模板配置 （key：模板类型, value：模板ID）
      register: xxx
```
##### 使用
```java
public class TestAliyunSms {
    
    //注入服务
    @Autowired
    private SmsTemplate smsTemplate;
    
    public void test(){
        //构造短信请求
        SmsRequest smsRequest = SmsRequest.of("register") //设置阿里云短信服务模板ID
                    .setPhoneNumbers("手机号码") //设置发送手机号
                    .setParam("key", "value"); //填充模板的变量
        try {
            //发送请求
            smsTemplate.send(smsRequest);
        } catch (SmsException e) {
            log.error("短信发送失败", e);
        }
    }
}
```
