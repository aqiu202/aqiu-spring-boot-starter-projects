package com.github.code.aqiu202.config;

import com.github.code.aqiu202.background.BackgroundProducer;
import com.github.code.aqiu202.border.BorderProducer;
import com.github.code.aqiu202.captcha.CaptchaProducer;
import com.github.code.aqiu202.captcha.impl.DefaultCaptchaProducer;
import com.github.code.aqiu202.noise.NoiseProducer;
import com.github.code.aqiu202.noise.OrderedNoiseProducer;
import com.github.code.aqiu202.noise.impl.CombiningNoiseProducer;
import com.github.code.aqiu202.props.CaptchaProperties;
import com.github.code.aqiu202.text.TextProducer;
import com.github.code.aqiu202.text.WordRenderer;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration(proxyBeanMethods = false)
public class CaptchaAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "captcha")
    public CaptchaProperties captchaProperties() {
        return new CaptchaProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public CaptchaProducer producer(CaptchaProperties captchaProperties,
            @Autowired(required = false) BorderProducer borderProducer,
            @Autowired(required = false) TextProducer textProducer,
            @Autowired(required = false) WordRenderer wordRenderer,
            @Autowired(required = false) BackgroundProducer backgroundProducer,
            @Autowired(required = false) OrderedNoiseProducer noiseProducer,
            @Autowired(required = false) List<NoiseProducer> noiseProducerList) {
        DefaultCaptchaProducer defaultCaptchaProducer = new DefaultCaptchaProducer(
                captchaProperties);
        if (Objects.nonNull(borderProducer)) {
            defaultCaptchaProducer.setBorderProducer(borderProducer);
        }
        if (Objects.nonNull(textProducer)) {
            defaultCaptchaProducer.setTextProducer(textProducer);
        }
        if (Objects.nonNull(wordRenderer)) {
            defaultCaptchaProducer.setWordRenderer(wordRenderer);
        }
        if (Objects.nonNull(backgroundProducer)) {
            defaultCaptchaProducer.setBackgroundProducer(backgroundProducer);
        }
        if (!CollectionUtils.isEmpty(noiseProducerList)) {
            defaultCaptchaProducer.setNoiseProducer(new CombiningNoiseProducer(noiseProducerList));
        }
        if (Objects.nonNull(noiseProducer)) {
            defaultCaptchaProducer.setNoiseProducer(noiseProducer);
        }
        return defaultCaptchaProducer;
    }
}
