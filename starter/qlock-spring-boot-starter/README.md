# qlock-spring-boot-starter
## 集中式、分布式锁实现Starter（支持Guava、Caffeine、Redis和Zookeeper等方式）
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>qlock-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```

### 配置
```java
@SpringBootApplication
@EnableQLock(
    value = LockMode.redis_r, //基于Redis的可重入分布式锁
    idType = IdType.SNOWFLAKE, //可重入锁的ID的生成策略设置为雪花算法(整合了id-generator组件)
    timeout = 3 //3秒锁自动释放（zookeeper获取锁的超时时间）
)
public class QLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(QLockApplication.class, args);
    }

}
```

### 使用试例
```java
@RestController
public class TestController{
    @GetMapping("lock-test")
    @QLock(
        value = "lock-test", //锁的Key为 lock-test,支持SpEL扩展
        timeout = 10 //10秒后锁自动释放
    )
    public JsonResult<Void> lockTest() {
        throw new IllegalArgumentException("retry");
    }
}
```