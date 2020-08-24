package com.github.aqiu202.wechat.wxpay.sdk;

import java.util.Map;

public interface WxPayNotifyHandler {

    void handle(Map<String, String> data);
}
