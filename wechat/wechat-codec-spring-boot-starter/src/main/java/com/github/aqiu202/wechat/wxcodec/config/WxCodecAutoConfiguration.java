package com.github.aqiu202.wechat.wxcodec.config;

import com.github.aqiu202.wechat.wxcodec.bean.WxCodecProperty;
import com.github.aqiu202.wechat.wxcodec.service.WxCodecService;
import com.github.aqiu202.wechat.wxcodec.service.WxCodecServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WxCodecProperty.class)
public class WxCodecAutoConfiguration {

    @Bean
    public WxCodecService decryptService(WxCodecProperty wxCodecProperty) {
        return new WxCodecServiceImpl(wxCodecProperty);
    }
}
