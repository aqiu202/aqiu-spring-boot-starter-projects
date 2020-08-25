package com.github.aqiu202.aliyun.tts.service;

import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.github.aqiu202.api.tts.config.TtsConfiguration;

/**
 * <b>阿里云TTS语音合成配置</b>
 * <p>保存阿里云TTS语音合成的属性配置选项</p>
 * @author aqiu 2020/2/19 10:42 上午
 **/
public class AliYunSynthesizerConfig extends TtsConfiguration {

    private OutputFormatEnum format = OutputFormatEnum.MP3;
    private SampleRateEnum sampleRate = SampleRateEnum.SAMPLE_RATE_16K;

    private AliYunSynthesizerConfig() {
    }

    public static AliYunSynthesizerConfig newInstance() {
        return new AliYunSynthesizerConfig();
    }

    public OutputFormatEnum getFormat() {
        return format;
    }

    public AliYunSynthesizerConfig setFormat(OutputFormatEnum format) {
        this.format = format;
        return this;
    }

    public SampleRateEnum getSampleRate() {
        return sampleRate;
    }

    public AliYunSynthesizerConfig setSampleRate(SampleRateEnum sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

}
