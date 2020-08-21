package com.github.aqiu202.aurora.jpush.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aqiu202.aurora.jpush.bean.JPushBean;
import com.github.aqiu202.aurora.jpush.param.JParam;
import com.github.aqiu202.aurora.jpush.result.JResult;
import com.github.aqiu202.aurora.jpush.util.HttpUtil;
import com.github.aqiu202.aurora.jpush.result.JCidPool;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class JPushServiceImpl implements JPushService {

    private final JPushBean jPushBean;

    private final ObjectMapper mapper;

    private final String token;

    public JPushServiceImpl(JPushBean jPushBean) {
        this.jPushBean = jPushBean;
        this.token = this.getToken();
        this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private String getToken() {
        return Base64.getEncoder().encodeToString(
                jPushBean.getAppKey().concat(":").concat(jPushBean.getMasterSecret())
                        .getBytes(Charsets.UTF_8));
    }

    @Override
    public JResult send(JParam jParam) throws IOException {
        JResult result;
        Map<String, Object> options = jParam.getOptions();
        if (options == null) {
            options = new HashMap<>();
        }
        options.put("apns_production", jPushBean.isProd());
        options.put("time_to_live", jPushBean.getTimeToLive());
        jParam.setOptions(options);
        String json = mapper.writeValueAsString(jParam);
        try (CloseableHttpResponse res = HttpUtil.postJson(this.jPushBean.getPushUrl(),
                Collections
                        .singletonList(new BasicHeader("Authorization", "Basic " + this.token)),
                json, this.jPushBean.getTimeout())) {
            int code = res.getStatusLine().getStatusCode();
            HttpEntity entity = res.getEntity();
            String str = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            result = mapper.readValue(str, JResult.class);
            result.setCode(code);
            return result;
        }
    }

    @Override
    public JCidPool getCidPool(int count) throws IOException {
        JCidPool pool;
        try (CloseableHttpResponse res = HttpUtil
                .get(this.jPushBean.getCidUrl().concat("?count=") + count,
                        Collections
                                .singletonList(
                                        new BasicHeader("Authorization", "Basic " + this.token)),
                        this.jPushBean.getTimeout())) {
            int code = res.getStatusLine().getStatusCode();
            HttpEntity entity = res.getEntity();
            String str = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            pool = mapper.readValue(str, JCidPool.class);
            pool.setCode(code);
            return pool;
        }
    }

    @Override
    public JCidPool getCidPool() throws IOException {
        return getCidPool(1);
    }

}
