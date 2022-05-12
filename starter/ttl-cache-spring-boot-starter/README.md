# ttl-cache-spring-boot-starter
## ttl缓存Starter，支持Guava、Caffeine和Redis
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>ttl-cache-spring-boot-starter</artifactId>
  <version>1.2.0</version>
</dependency>
```
### 配置
```yaml
@EnableTtlCaching(
  value = CacheMode.caffeine,//设置缓存为caffeine
  timeout = 3600, //设置超时时间为3600秒
  timeUnit = TimeUnit.SECONDS
  )
  @SpringBootApplication
  public class Main {
      public static void main(String[] args){
        SpringApplication.run(Main.class, args);
      }
}
```