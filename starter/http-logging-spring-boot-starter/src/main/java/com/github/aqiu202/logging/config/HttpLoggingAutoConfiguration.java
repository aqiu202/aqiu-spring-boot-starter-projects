package com.github.aqiu202.logging.config;

import com.github.aqiu202.logging.filter.LoggerTracingFilter;
import com.github.aqiu202.logging.filter.HttpRequestLoggingFilter;
import com.github.aqiu202.logging.filter.HttpResponseLoggingFilter;
import com.github.aqiu202.logging.prop.PangeaLoggingProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PangeaLoggingProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "http.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HttpLoggingAutoConfiguration {

    @Bean
    public FilterRegistrationBean<HttpRequestLoggingFilter> requestLoggingFilterRegistration(PangeaLoggingProperties properties) {
        FilterRegistrationBean<HttpRequestLoggingFilter> bean = new FilterRegistrationBean<>();
        HttpRequestLoggingFilter loggingFilter = new HttpRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(properties.isIncludeClientInfo());
        loggingFilter.setIncludeHeaders(properties.isIncludeHeaders());
        loggingFilter.setIncludeQueryString(properties.isIncludeQueryString());
        loggingFilter.setIncludePayload(properties.isIncludePayload());
        loggingFilter.setMaxPayloadLength(properties.getMaxPayloadSize());
        bean.setFilter(loggingFilter);
        bean.setOrder(Integer.MAX_VALUE);
        return bean;
    }
    /**
     * 注册日志TracingFilter
     */
    @Bean
    public FilterRegistrationBean<LoggerTracingFilter> loggingFilterBean() {
        FilterRegistrationBean<LoggerTracingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setFilter(new LoggerTracingFilter());
        filterBean.addUrlPatterns("/*");
        filterBean.setOrder(Integer.MIN_VALUE);
        return filterBean;
    }


    @Bean
    public FilterRegistrationBean<HttpResponseLoggingFilter> responseWrapperFilterRegistration() {
        FilterRegistrationBean<HttpResponseLoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new HttpResponseLoggingFilter());
        bean.setOrder(Integer.MIN_VALUE + 1);
        return bean;
    }
}
