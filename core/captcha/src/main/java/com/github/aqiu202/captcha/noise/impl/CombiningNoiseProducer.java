package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.NoiseProducer;
import com.github.aqiu202.captcha.noise.abs.AbstractOrderedFilterableNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.NoiseConfiguration;
import com.github.aqiu202.captcha.props.CaptchaProperties.NoiseProperties;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CombiningNoiseProducer extends AbstractOrderedFilterableNoiseProducer {

    private final List<NoiseProducer> noiseProducerList;

    public CombiningNoiseProducer(List<NoiseProducer> noiseProducers) {
        this.noiseProducerList = noiseProducers;
    }

    public CombiningNoiseProducer(NoiseProducer... noiseProducers) {
        this.noiseProducerList = Arrays.asList(noiseProducers);
    }

    public CombiningNoiseProducer(NoiseProperties noise) {
        List<NoiseStyle> styles = noise.getStyles();
        NoiseConfiguration configuration = noise.getConfigurations();
        List<NoiseProducer> producers = new ArrayList<>();
        for (NoiseStyle style : styles) {
            switch (style) {
                case LINE:
                    producers.add(new LineNoise(configuration.getLine()));
                    break;
                case SHEAR:
                    producers.add(new ShearNoise(configuration.getShear()));
                    break;
                case SHADOW:
                    producers.add(new ShadowNoise(configuration.getShadow()));
                    break;
                case RIPPLE:
                    producers.add(new RippleNoise(configuration.getRipple()));
                    break;
                case WATER:
                    producers.add(new WaterNoise(configuration.getWater()));
                    break;
                case POINT:
                    producers.add(new PointNoise(configuration.getPoint()));
                    break;
                default:
                    producers.add(new NoNoise());
            }
        }
        this.noiseProducerList = producers;
    }

    @Override
    public BufferedImage filterBeforeAddBackground(BufferedImage image) {
        List<NoiseProducer> list = this.noiseProducerList.stream()
                .filter(noiseProducer -> NoiseOrder.BEFORE
                        .equals(noiseProducer.getNoiseStyle().getOrder()))
                .collect(Collectors.toList());
        return this.doFilter(image, list);
    }

    @Override
    public BufferedImage filterAfterAddBackground(BufferedImage image) {
        List<NoiseProducer> list = this.noiseProducerList.stream()
                .filter(noiseProducer -> NoiseOrder.AFTER
                        .equals(noiseProducer.getNoiseStyle().getOrder()))
                .collect(Collectors.toList());
        return this.doFilter(image, list);
    }

    private BufferedImage doFilter(BufferedImage image, Collection<NoiseProducer> producers) {
        for (NoiseProducer producer : producers) {
            image = producer.makeNoise(image);
        }
        return image;
    }
}
