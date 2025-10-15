# zk-curator-spring-boot-starter

## curator自动配置Starter
```xml
<dependency>
  <groupId>com.github.aqiu202</groupId>
  <artifactId>zk-curator-spring-boot-starter</artifactId>
  <version>${revision}</version>
</dependency>
```

### 配置
```yaml
#curator自动配置
curator:
  connect-string: localhost:2181 #连接
  connection-timeout: 5000 #连接超时时间
  session-timeout: 3000 #会话超时时间
  max-retries: 30 #最大重试次数
  base-sleep-time: 1000 #基础睡眠时长
  max-sleep-time: 5000 #最大睡眠时长
  auth:
    - schema: digest #权限模式
      auth: xxx  #密码
```

### 使用
- 直接使用 @Autowired注入CuratorFramework即可使用