package com.github.aqiu202.aliyun.tts.store.client;

import com.alibaba.nls.client.protocol.NlsClient;
import com.github.aqiu202.aliyun.tts.config.TtsToken;
import com.github.aqiu202.aliyun.tts.store.SimpleTokenStore;
import com.github.aqiu202.aliyun.tts.store.TokenStore;
import com.github.aqiu202.util.StringUtils;
import java.util.Objects;

public class DefaultClientStore implements ClientStore {

    private final TokenStore tokenStore;

    private NlsClient client;

    public DefaultClientStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public DefaultClientStore() {
        this.tokenStore = new SimpleTokenStore();
    }

    @Override
    public NlsClient getClient(TtsToken ttsToken) {
        String accessKeyId = ttsToken.getAccessKeyId();
        String accessKeySecret = ttsToken.getAccessKeySecret();
        String url = ttsToken.getUrl();
        if (StringUtils.hasText(url)) {
            if (tokenStore.isExpired(accessKeyId)) {
                this.shutdown();
                this.newClient(accessKeyId, accessKeySecret, url);
            } else {
                this.cacheClient(accessKeyId, accessKeySecret, url);
            }
        } else {
            if (tokenStore.isExpired(accessKeyId)) {
                this.shutdown();
                this.newClient(accessKeyId, accessKeySecret);
            } else {
                this.cacheClient(accessKeyId, accessKeySecret);
            }
        }
        return this.client;
    }


    private void cacheClient(String accessKeyId, String accessKeySecret) {
        if (Objects.isNull(client)) {
            this.newClient(accessKeyId, accessKeySecret);
        }
    }

    private void newClient(String accessKeyId, String accessKeySecret) {
        client = new NlsClient(tokenStore.getToken(accessKeyId, accessKeySecret));
    }

    private void newClient(String accessKeyId, String accessKeySecret, String url) {
        client = new NlsClient(url, tokenStore.getToken(accessKeyId, accessKeySecret));
    }

    private void cacheClient(String accessKeyId, String accessKeySecret, String url) {
        if (Objects.isNull(client)) {
            this.newClient(accessKeyId, accessKeySecret, url);
        }
    }

    private void shutdown() {
        if (Objects.nonNull(client)) {
            client.shutdown();
        }
    }

    @Override
    public TokenStore getTokenStore() {
        return tokenStore;
    }
}
