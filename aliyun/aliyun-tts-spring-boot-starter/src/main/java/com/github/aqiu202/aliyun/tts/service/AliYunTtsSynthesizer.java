package com.github.aqiu202.aliyun.tts.service;


import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.github.aqiu202.aliyun.tts.config.TtsToken;
import com.github.aqiu202.aliyun.tts.listener.AliYunTssListener;
import com.github.aqiu202.aliyun.tts.store.SimpleTokenStore;
import com.github.aqiu202.aliyun.tts.store.client.ClientStore;
import com.github.aqiu202.aliyun.tts.store.client.DefaultClientStore;
import com.github.aqiu202.api.tts.config.TtsConfiguration;
import com.github.aqiu202.api.tts.listener.TtsListener;
import com.github.aqiu202.api.tts.synthesizer.TtsSynthesizer;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>阿里云TTS语音合成器</b>
 * <p>调用阿里云TTS服务合成语音</p>
 * @author aqiu 2020/2/19 10:41 上午
 **/
public final class AliYunTtsSynthesizer implements TtsSynthesizer {

    private final Logger log = LoggerFactory.getLogger(AliYunTtsSynthesizer.class);

    public AliYunTtsSynthesizer(TtsToken ttsToken) {
        this(ttsToken, new DefaultClientStore());
    }

    public AliYunTtsSynthesizer(TtsToken ttsToken, ClientStore clientStore) {
        this.ttsToken = ttsToken;
        this.clientStore = clientStore;
    }

    private final TtsToken ttsToken;
    private final ClientStore clientStore;

    public void process(final String text,
            TtsListener listener) {
        this.process(AliYunSynthesizerConfig.newInstance().setText(text), listener);
    }

    public void process(final TtsConfiguration configuration,
            TtsListener listener) {
        if (configuration instanceof AliYunSynthesizerConfig) {
            this.process((AliYunSynthesizerConfig) configuration, listener);
        } else {
            log.error("语音合成配置错误");
        }
    }

    public void process(final AliYunSynthesizerConfig configuration,
            TtsListener listener) {
        AliYunTssListener aliyunTssListener = new AliYunTssListener(listener);
        SpeechSynthesizer synthesizer = null;
        NlsClient client = clientStore.getClient(this.ttsToken);
        try {
            //创建实例,建立连接
            synthesizer = new SpeechSynthesizer(client, aliyunTssListener);
            synthesizer.setAppKey(this.ttsToken.getAppKey());
            //设置返回音频的编码格式
            synthesizer.setFormat(configuration.getFormat());
            //设置返回音频的采样率
            synthesizer.setSampleRate(configuration.getSampleRate());
            //发音人
            synthesizer.setVoice(configuration.getVoice());
            //语调，范围是-500~500，可选，默认是0
            synthesizer.setPitchRate(configuration.getPitchRate());
            //语速，范围是-500~500，默认是0
            synthesizer.setSpeechRate(configuration.getSpeechRate());
            //设置用于语音合成的文本
            synthesizer.setText(configuration.getText());
            //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            synthesizer.start();
            //等待语音合成结束
            synthesizer.waitForComplete();
            if (this.clientStore.getTokenStore() instanceof SimpleTokenStore) {
                client.shutdown();
            }
        } catch (Exception e) {
            log.error("合成语音失败：", e);
        } finally {
            //关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
    }

    public void shutdown() {
        if (Objects.nonNull(this.clientStore)) {
            this.clientStore.getClient(ttsToken).shutdown();
        }
    }
}
