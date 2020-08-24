package com.github.aqiu202.aliyun.tts.listener;

import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
import com.github.aqiu202.api.tts.listener.TtsListener;
import com.github.aqiu202.api.tts.param.DefaultTtsMessage;
import java.nio.ByteBuffer;

public class AliYunTssListener extends SpeechSynthesizerListener {

    private final TtsListener proxyListener;

    public AliYunTssListener(TtsListener proxyListener) {
        this.proxyListener = proxyListener;
    }

    @Override
    public void onComplete(SpeechSynthesizerResponse response) {
        proxyListener.onComplete(new DefaultTtsMessage(response.getTaskId(), response.getStatus(),
                response.getStatusText(), null));

    }

    @Override
    public void onFail(SpeechSynthesizerResponse response) {
        proxyListener.onFail(new DefaultTtsMessage(response.getTaskId(), response.getStatus(),
                response.getStatusText(), null));

    }

    @Override
    public void onMessage(ByteBuffer message) {
        proxyListener.onMessage(message);
    }

    public void closeStream() {
        proxyListener.closeStream();
    }
}
