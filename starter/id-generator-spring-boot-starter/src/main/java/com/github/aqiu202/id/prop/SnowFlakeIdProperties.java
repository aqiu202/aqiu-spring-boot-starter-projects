package com.github.aqiu202.id.prop;

import java.security.SecureRandom;

/**
 * <pre>SnowFlake（雪花算法）配置</pre>
 *
 * @author aqiu 2020/12/7 8:40
 **/
public class SnowFlakeIdProperties {

    private long workerId = randomNumber();

    private long dataCenterId = randomNumber();

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    private static long randomNumber() {
        return new SecureRandom().nextInt(31);
    }

}
