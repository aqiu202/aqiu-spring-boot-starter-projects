package com.github.aqiu202.aliyun.tts.store.client;


import com.alibaba.nls.client.protocol.NlsClient;
import com.github.aqiu202.aliyun.tts.config.TtsToken;
import com.github.aqiu202.aliyun.tts.store.TokenStore;

public interface ClientStore {

    TokenStore getTokenStore();

    NlsClient getClient(TtsToken ttsToken);

}
