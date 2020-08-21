package com.github.aqiu202.qrcode.config;

import com.github.aqiu202.qrcode.param.QRCodeConfiguration;
import com.github.aqiu202.qrcode.service.QRCodeService;
import com.github.aqiu202.qrcode.service.impl.QRCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(QRCodeConfiguration.class)
public class QRCodeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QRCodeService qrCodeService(QRCodeConfiguration configuration) {
        return new QRCodeServiceImpl(configuration);
    }
}
