package com.github.aqiu202.aurora.jpush.service;


import com.github.aqiu202.aurora.jpush.param.JParam;
import com.github.aqiu202.aurora.jpush.result.JCidPool;
import com.github.aqiu202.aurora.jpush.result.JResult;
import java.io.IOException;

public interface JPushService {

    JResult send(JParam jParam) throws IOException;

    JCidPool getCidPool() throws IOException;

    JCidPool getCidPool(int count) throws IOException;

}
