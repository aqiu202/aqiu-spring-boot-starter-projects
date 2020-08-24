package com.github.aqiu202.aliyun.sms.template;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.utils.StringUtils;
import com.github.aqiu202.api.sms.exp.SmsException;
import com.github.aqiu202.api.sms.param.SmsRequest;
import com.google.gson.Gson;
import com.github.aqiu202.aliyun.sms.bean.AliYunSmsProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class DefaultAliYunSmsTemplate implements AliYunSmsTemplate {

    public DefaultAliYunSmsTemplate(AliYunSmsProperty aliYunSmsProperty) {
        this.aliYunSmsProperty = aliYunSmsProperty;
    }

    private final AliYunSmsProperty aliYunSmsProperty;

    private final Gson gson = new Gson();

    @Override
    public void sendMsg(SmsRequest mnsRequest) {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout",
                aliYunSmsProperty.getConnectTimeout());
        System.setProperty("sun.net.client.defaultReadTimeout", aliYunSmsProperty.getReadTimeout());
        // 初始化ascClient需要的几个参数
        final String product = aliYunSmsProperty.getProduct();// 短信API产品名称（短信产品名固定，无需修改）
        final String domain = aliYunSmsProperty.getDomain();// 短信API产品域名（接口地址固定，无需修改）
        // 替换成你的AK
        final String accessKeyId = aliYunSmsProperty.getAccessKeyId();// 你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = aliYunSmsProperty
                .getAccessKeySecret();
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile
                .getProfile(aliYunSmsProperty.getRegionId(), accessKeyId, accessKeySecret);
        DefaultProfile
                .addEndpoint(
                        aliYunSmsProperty.getRegionId(), product,
                        domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setSysMethod(MethodType.POST);
        // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        Collection<String> phoneNumbers = mnsRequest.getPhoneNumbers();
        StringJoiner joiner = new StringJoiner(",");
        Optional.ofNullable(phoneNumbers).orElse(new ArrayList<>()).forEach(joiner::add);
        request.setPhoneNumbers(joiner.toString());
        // 必填:短信签名-可在短信控制台中找到
        String signName = mnsRequest.getSignName();
        if (StringUtils.isEmpty(signName)) {
            request.setSignName(aliYunSmsProperty.getSignName());
        } else {
            request.setSignName(signName);
        }
        // 必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        String templateType = mnsRequest.getTemplateType();
        String templateCode = aliYunSmsProperty.getTemplates().get(templateType);
        //兼容之前的版本 templateType可以作为模板编号用
        if (StringUtils.isEmpty(templateCode)) {
            templateCode = templateType;
        }
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(gson.toJson(mnsRequest.getParams()));
        // 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        // request.setSmsUpExtendCode("90997");
        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        // request.setOutId("yourOutId");
        // 请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SmsException(e.getErrCode(), e.getErrMsg(), e.getRequestId());
        }
        if (sendSmsResponse.getCode() == null || !sendSmsResponse.getCode().equals("OK")) {
            throw new SmsException(sendSmsResponse.getCode(), sendSmsResponse.getMessage(),
                    sendSmsResponse.getRequestId());
        }
    }

    @Override
    public void sendMsg(String templateType, String phoneNo, Map<String, String> params)
            throws SmsException {
        this.sendMsg(SmsRequest.of(templateType, phoneNo).setParams(params));
    }

    @Override
    public void sendMsg(String templateType, Collection<String> phoneNoList,
            Map<String, String> params) throws SmsException {
        this.sendMsg(SmsRequest.of(templateType, phoneNoList).setParams(params));
    }

}
