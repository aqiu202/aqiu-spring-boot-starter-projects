package com.github.aqiu202.captcha.autoconfig;


import com.github.aqiu202.captcha.background.BackgroundProducer;
import com.github.aqiu202.captcha.border.BorderProducer;
import com.github.aqiu202.captcha.captcha.CaptchaProducer;
import com.github.aqiu202.captcha.captcha.impl.DefaultCaptchaProducer;
import com.github.aqiu202.captcha.noise.NoiseProducer;
import com.github.aqiu202.captcha.noise.OrderedNoiseProducer;
import com.github.aqiu202.captcha.noise.impl.CombiningNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties;
import com.github.aqiu202.captcha.text.TextProducer;
import com.github.aqiu202.captcha.text.WordRenderer;
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
