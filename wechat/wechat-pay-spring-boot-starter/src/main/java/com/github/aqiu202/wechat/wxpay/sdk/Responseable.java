package com.github.aqiu202.wechat.wxpay.sdk;


import com.github.aqiu202.wechat.wxpay.sdk.WXPayUtil.ResponseWriter;

public class Responseable {

    public ResponseWriter response() {
        return new ResponseWriter();
    }
}
