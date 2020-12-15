# id-generator-spring-boot-starter

## 分布式ID生成Starter，支持雪花算法、Redis和UUID等各种策略
基于 [id-generator-core](https://github.com/aqiu202/aqiu-spring-boot-starter-projects/tree/master/core/id-generator-core) 和Spring Boot集成，已经发布到maven中央仓库
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>id-generator-spring-boot-starter</artifactId>
  <version>1.0.5</version>
</dependency>
```

### 配置
```yaml
#ID Generator配置
id-generator:
  type: auto #策略类型配置，默认auto 使用雪花算法
  snow-flake: #雪花算法配置
    data-center-id: 12 #dataCenterId
    worker-id: 10 #workerId
  redis: #Redis策略配置
    delta: 2 #ID自增步长
    init-value: 0 #初始值 默认值0（即第一个ID为 0+delta）
    key: _redis_dist_key #Redis存储的Key值
```

### 使用试例
```java
import com.github.aqiu202.id.IdGenerator;

@Validated
@RestController
public class IdGeneratorController {

    @Autowired
    private IdGenerator idGenerator;

    /**
     * 获取ID
     * @author aqiu
     **/
    @GetMapping("/id")
    public String doGet() throws IOException {
        return String.valueOf(this.idGenerator.nextId());
    }

}
```