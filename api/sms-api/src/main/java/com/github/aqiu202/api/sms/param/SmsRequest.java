package com.github.aqiu202.api.sms.param;

import java.util.List;
import java.util.Map;

/**
 * <pre>标准的SMS短信服务请求信息</pre>
 * <p>标准的SMS短信服务请求包含的所有信息（短信签名、模版类型/模版ID、手机号码、填充参数等）</p>
 * @author aqiu 2020/2/19 4:09 下午
**/
public interface SmsRequest {

    static SmsRequest of(String templateType) {
        return new DefaultSmsRequest(templateType);
    }

    static SmsRequest of(String templateType, List<String> phoneNumbers) {
        return new DefaultSmsRequest(templateType, phoneNumbers);
    }

    static SmsRequest of(String templateType, String... phoneNumbers) {
        return new DefaultSmsRequest(templateType, phoneNumbers);
    }

    String getSignName();

    List<String> getPhoneNumbers();

    String getTemplateType();

    Map<String, String> getParams();

    SmsRequest setPhoneNumbers(List<String> phoneNumbers);

    SmsRequest setSignName(String signName);

    SmsRequest setTemplateType(String templateType);

    SmsRequest setParam(String key, String value);

    SmsRequest setParams(Map<String, String> params);

    SmsRequest addParams(String... params);

}
