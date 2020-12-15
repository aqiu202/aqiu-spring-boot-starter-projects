# jpush-rest-api-spring-boot-starter
### 描述
&emsp;&emsp;极光推送RESTFul API的封装 
### 引用
&emsp;&emsp;maven坐标 
```xml
<dependency>
    <groupId>com.github.aqiu202.aurora</groupId>
    <artifactId>jpush-rest-api-spring-boot-starter</artifactId>
    <version>1.0.5</version>
</dependency>
```

### 用法
##### 配置
```yaml
# 极光推送配置
jpush:
  config:
    app-key: xxx #极光的应用AppKey
    master-secret: xxx #极光的应用秘钥
    time-to-live: 86400 #消息的离线时长（秒）默认一天，最长10天
    prod: false  #针对ios的配置
```
##### 使用
```java
@Service
public class TestJPushService {

    //注入JPushService服务
    @Autowired
    private JPushService jPushService;
    
    public void send() {
        //构造推送消息体
        JParam jparam = null;
        //广播
        jparam = JParam.newInstance()
                    .audience(AudienceForAll.DEFAULT) //设置消息为广播
                    .setTitle("标题") //设置消息标题
                    .setContent("内容"); //设置消息内容
                    
        //推送给特定用户群组
        jparam = JParam.newInstance()
                    .addTag("标签") //标签用来标识一组（一个或者多个）用户
                    .addAlias("别名") //别名用来标识唯一的一个用户
                    .setTitle("标题")
                    .setContent("内容");
                    
        //限制客户端类型
        jparam = JParam.of(Platform.Android) //只针对Android客户端
                    .setTitle("标题")
                    .setContent("内容");
                    
        //设置其他参数
        jparam = JParam.newInstance()
                    .addExtra("key", new Object())
                    .addExtras(new HashMap<>())
                    .setTitle("标题")
                    .setContent("内容");
        
        //推送消息
        try {
            jPushService.send(jparam);
        } catch (IOException e) {
            log.error("极光推送失败：", e);
        }
    }
}
```