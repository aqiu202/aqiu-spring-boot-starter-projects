package com.github.aqiu202.id.prop;

/**
 * <pre>Redis分布式主键配置</pre>
 *
 * @author aqiu 2020/12/7 8:34
 **/
public class RedisIdProperties {

    private String key = "id_generator_key:";

    private long initValue = 0;

    private long delta = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getInitValue() {
        return initValue;
    }

    public void setInitValue(long initValue) {
        this.initValue = initValue;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }
}
