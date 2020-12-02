package com.github.aqiu202.aop.util;

import com.github.aqiu202.aop.keygen.KeyGenerator;
import com.github.aqiu202.aop.keygen.impl.SimpleKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * <pre>KeyGeneratorUtils</pre>
 *
 * @author aqiu 2020/11/29 1:40
 **/
public abstract class KeyGeneratorUtils {

    private static final Logger logger = LoggerFactory.getLogger(KeyGeneratorUtils.class);

    private static final KeyGenerator defaultKeyGenerator = new SimpleKeyGenerator();

    @NonNull
    public static KeyGenerator getKeyGenerator(String keyGeneratorName,
            ApplicationContext applicationContext) {
        if (StringUtils.isEmpty(keyGeneratorName)) {
            return defaultKeyGenerator;
        } else {
            KeyGenerator keyGenerator;
            try {
                keyGenerator = applicationContext
                        .getBean(keyGeneratorName, KeyGenerator.class);
            } catch (BeansException e) {
                logger.error("KeyGenerator 配置错误，没有找到名称为" + keyGeneratorName + "的KeyGenerator：",
                        e);
                keyGenerator = defaultKeyGenerator;
            }
            return keyGenerator;
        }
    }
}
