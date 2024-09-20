# nls-tts-spring-boot-starter
### 描述
&emsp;&emsp;阿里云TTS语音合成功能封装

### 引用
&emsp;&emsp;使用公司maven私服，通过maven坐标引入依赖 
```xml
<dependency>
    <groupId>com.github.aqiu202.aliyun</groupId>
    <artifactId>aliyun-tts-spring-boot-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```
### 用法
##### 配置
```yaml
aliyun:
  tts:
    app-key: xxx #TTS appKey
    access-key-id: xxx #TTS accessKeyId
    access-key-secret: xxx #TTS accessKeySecret
```
调用阿里云服务接口的时候需要使用accessKeyId和accessKeySecret去换取token，
token是可以重复使用的但是会失效，所以我们需要缓存token（默认是不缓存token的），配置的方式如下：
```java
@Configuration
public class TokenStoreConfig {

    //配置缓存方式 >> Guava
    public StringTtlCache stringGuavaCache() {
        return new StringGuavaCache();
    }
    //配置缓存方式 >> Caffeine
    public StringTtlCache stringCaffeineCache() {
        return new StringCaffeineCache();
    }
    //配置缓存方式 >> Redis
    public StringTtlCache stringRedisCache(StringRedisTemplate stringRedisTemplate) {
        return new StringRedisCache(stringRedisTemplate);
    }

    //基于缓存，配置可缓存的TokenStore
    @Bean
    public TokenStore cacheableTokenStore(StringTtlCache cache) {
        return new SimpleCacheableTokenStore(cache);
    }

}
```
【注意】：配置缓存时，默认只能使用一种。
### 使用
```java
public class TestNlsTts {
    //注入
    @Autowired
    private TtsSynthesizer ttsSynthesizer;
    
    public void test() {
        //简单用法
        String path = "/tmp/test.mp3";
        String text = "你好";
        ttsSynthesizer.process(text, new TtsListener(path));
        
        //复杂的用法
        String path2 = "/tmp/test.wav";
        //配置语言参数
        TtsConfiguration config = AliyunSynthesizerConfig.newInstance()
				.setFormat(OutputFormatEnum.WAV) //设置输出格式
				.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K) //设置采样率
				.setText(text) //设置语言内容
				.setSpeechRate(0) //设置语速
				.setPitchRate(0) //设置振幅
				.setVoice("xiaoyun"); //设置发音训练模型（默认"xiaoyun"）
	    ttsSynthesizer.process(config, new TtsListener(path2));
    }
}
```
【注意】：输出的目录必须存在否则会报FileNotFoundException