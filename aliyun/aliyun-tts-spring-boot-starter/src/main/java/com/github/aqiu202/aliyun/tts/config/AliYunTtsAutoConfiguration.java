package com.github.aqiu202.aliyun.tts.config;


import com.github.aqiu202.aliyun.tts.service.AliYunTtsSynthesizer;
import com.github.aqiu202.aliyun.tts.store.SimpleTokenStore;
import com.github.aqiu202.aliyun.tts.store.TokenStore;
import com.github.aqiu202.aliyun.tts.store.client.ClientStore;
import com.github.aqiu202.aliyun.tts.store.client.DefaultClientStore;
import com.github.aqiu202.aliyun.tts.store.generator.DefaultKeyGenerator;
import com.github.aqiu202.aliyun.tts.store.generator.KeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({TtsToken.class})
public class AliYunTtsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore tokenStore() {
        return new SimpleTokenStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientStore clientStore(TokenStore tokenStore) {
        return new DefaultClientStore(tokenStore);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliYunTtsSynthesizer aliyunTtsSynthesizer(TtsToken ttsToken,
            ClientStore clientStore) {
        return new AliYunTtsSynthesizer(ttsToken, clientStore);
    }
}
