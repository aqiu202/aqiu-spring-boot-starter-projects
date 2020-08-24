package com.github.aqiu202.wechat.wxcodec.config;

import com.github.aqiu202.wechat.wxcodec.bean.WxCodecProperty;
import com.github.aqiu202.wechat.wxcodec.service.DecryptService;
import com.github.aqiu202.wechat.wxcodec.service.DecryptServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WxCodecProperty.class)
public class WxCodecAutoConfiguration {

    @Bean
    public DecryptService decryptService(WxCodecProperty wxCodecProperty) {
        return new DecryptServiceImpl(wxCodecProperty);
    }
}
