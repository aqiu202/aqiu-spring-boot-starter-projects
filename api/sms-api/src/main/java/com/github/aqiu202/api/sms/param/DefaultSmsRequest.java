package com.github.aqiu202.api.sms.param;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>标准的SMS短信服务请求信息（默认实现）</pre>
 * <p>标准的SMS短信服务请求包含的所有信息（短信签名、模版类型/模版ID、手机号码、填充参数等）</p>
 * @author aqiu 2020/2/19 4:11 下午
**/
public class DefaultSmsRequest implements SmsRequest, Serializable {

    DefaultSmsRequest(String templateType) {
        this.templateType = templateType;
    }

    DefaultSmsRequest(String templateType, List<String> phoneNumbers) {
        this.templateType = templateType;
        this.phoneNumbers = phoneNumbers;
    }

    DefaultSmsRequest(String templateType, String... phoneNumbers) {
        this.templateType = templateType;
        this.phoneNumbers = Arrays.asList(phoneNumbers);
    }

    private List<String> phoneNumbers;

    private String templateType;

    private String signName;

    private final Map<String, String> params = new LinkedHashMap<>();

    @Override
    public String getSignName() {
        return signName;
    }

    @Override
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public String getTemplateType() {
        return templateType;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public DefaultSmsRequest setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    @Override
    public DefaultSmsRequest setTemplateType(String templateType) {
        this.templateType = templateType;
        return this;
    }

    @Override
    public DefaultSmsRequest setSignName(String signName) {
        this.signName = signName;
        return this;
    }

    @Override
    public DefaultSmsRequest setParams(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    @Override
    public DefaultSmsRequest setParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    @Override
    public SmsRequest addParams(String... params) {
        int index = this.params.size();
        for (String param : params) {
            this.params.put(String.valueOf(index++), param);
        }
        return this;
    }
}
