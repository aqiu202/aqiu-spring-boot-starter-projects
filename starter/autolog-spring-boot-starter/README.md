# autolog-spring-boot-starter
### 描述
&emsp;&emsp;自动捕获操作日志组件

### 引用
maven坐标
```xml
<dependency>
    <groupId>com.github.aqiu202</groupId>
    <artifactId>autolog-spring-boot-starter</artifactId>
    <version>1.0.3</version>
</dependency>
```
### 用法
##### 启用AutoLog
```java
@EnableAutoLog(
    enable = true,//开启AutoLog日志
    dubug = true //开启debug模式，会打印所有前端传输参数    
)
@SpringBootApplication
public class Main {
    public static void main(String[] args){
      SpringApplication.run(Main.class, args);
    }
}
```
##### 使用AutoLog
```java
@RestController
public class TestController {
    
    //简单使用
    @GetMapping("test")
    @AutoLog
    public JsonResult test() {
        return JsonResult.ok();
    }

    //使用无参数的日志
    @GetMapping("test1")
    @AutoLog(value = "测试日志")
    public JsonResult test1() {
        return JsonResult.ok();
    }

    //使用Spring SpEl表达式注入指定参数
    //参数方式注入日志方式1，通过名称 #name
    @GetMapping("test2")
    @AutoLog(value = "'测试参数，参数name:' + #map[name]")
    public JsonResult test2(@RequestBody Map<String,String> map) {
        return JsonResult.ok();
    }

    //参数方式注入日志方式2，通过索引，#a0或者#p0，索引从0开始
    @GetMapping("test3")
    @AutoLog(value="'测试参数，参数1:' + #a0")
    public JsonResult test3(String name) {
        return JsonResult.ok();
    }
    
}
```
##### 收集/处理捕获到的日志
```java
@Configuration
public class AutoLogConfig {

    //收集日志
    @Bean
    public LogCollector logCollector() {
        return (
                request,//request HTTP servlet request
                autoLog,//autoLog 注解信息
                throwable,//throwable 异常信息，如果没有异常为 null
                args //args 方法参数
        ) -> {
            DefaultLogRequestParam param = DefaultLogRequestParam.create();
            //填充日志参数，没设置的字段会默认自动填充 result不需要填充
            param.setDesc("description");
            return param;
        };
    }
    //处理日志
    @Bean
    public LogHandler logHandler() {
        return (requestParam) -> {
            //处理捕获到的日志
        };
    }
}
```
##### 自定义扩展日志参数
默认的日志采集参数只有 ip,uri,method,desc,result这几个字段，如果不满足需求的话，
我们可以扩展 DefaultLogRequestParam 类来满足我们的需求，试例如下：
```java
//自定义扩展类
public class MyLogRequestParam extends DefaultLogRequestParam {

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
//配置类
@Configuration
class AutoLogConfig {

    //收集日志
    @Bean
    public LogCollector logCollector() {
        return (
                request,//request HTTP servlet request
                autoLog,//autoLog 注解信息
                throwable,//throwable 异常信息，如果没有异常为 null
                args //args 方法参数
        ) -> {
            MyLogRequestParam param = new MyLogRequestParam();
            param.setSessionId(request.getSession().getId());
            return param;
        };
    }
    
    //处理日志
    @Bean
    public LogHandler logHandler() {
        return (requestParam) -> {
            //处理捕获到的日志
            MyLogRequestParam myLogRequestParam = (MyLogRequestParam) requestParam;
            System.out.println(myLogRequestParam.getSessionId());
        };
    }
    
}
```
