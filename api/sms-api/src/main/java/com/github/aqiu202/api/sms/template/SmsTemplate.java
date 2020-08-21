package com.github.aqiu202.api.sms.template;


import com.github.aqiu202.api.sms.exp.SmsException;
import com.github.aqiu202.api.sms.param.SmsRequest;

/**
 * <pre>标准的SMS短信发送服务模版</pre>
 * <p>SMS短信发送服务，发送标准的SMS短信请求</p>
 * @author aqiu 2020/2/19 4:12 下午
**/
public interface SmsTemplate {

    void sendMsg(SmsRequest smsRequest) throws SmsException;
}
