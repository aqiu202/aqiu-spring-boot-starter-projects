package com.github.aqiu202.qrcode.config;

import com.github.aqiu202.qrcode.param.QrCodeProperties;
import com.github.aqiu202.qrcode.service.QrCodeService;
import com.github.aqiu202.qrcode.service.impl.QrCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(QrCodeProperties.class)
public class QrCodeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QrCodeService qrCodeService(QrCodeProperties configuration) {
        return new QrCodeServiceImpl(configuration);
    }
}
