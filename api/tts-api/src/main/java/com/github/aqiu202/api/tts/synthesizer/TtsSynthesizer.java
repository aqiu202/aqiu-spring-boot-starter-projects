package com.github.aqiu202.api.tts.synthesizer;


import com.github.aqiu202.api.tts.config.TtsConfiguration;
import com.github.aqiu202.api.tts.listener.TtsListener;

/**
 * <b>TTS语音合成器</b>
 * <p>完成语音合成操作</p>
 * @author aqiu 2020/2/19 10:19 上午
 **/
public interface TtsSynthesizer {

    void process(String text, TtsListener listener);

    void process(TtsConfiguration configuration,
            TtsListener listener);
}
