package com.github.aqiu202.wechat.wxpay.config;


import com.github.aqiu202.wechat.wxpay.bean.WxPayProperty;
import com.github.aqiu202.wechat.wxpay.WXPayHelper;
import com.github.aqiu202.wechat.wxpay.bean.MyWxPayConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(WxPayProperty.class)
public class WxPayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MyWxPayConfig myWxPayConfig(WxPayProperty wxPayProperty) {
        return new MyWxPayConfig(wxPayProperty);
    }

    @Bean
    @ConditionalOnMissingBean
    public WXPayHelper wxPayHelper(MyWxPayConfig myWxPayConfig) {
        return new WXPayHelper(myWxPayConfig);
    }
}
