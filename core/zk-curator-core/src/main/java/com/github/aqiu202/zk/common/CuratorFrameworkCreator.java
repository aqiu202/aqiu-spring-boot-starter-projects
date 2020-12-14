package com.github.aqiu202.zk.common;

import com.github.aqiu202.zk.prop.AuthInfoProperties;
import com.github.aqiu202.zk.prop.CuratorProperties;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * <pre>CuratorFrameworkCreator</pre>
 *
 * @author aqiu 2020/12/11 9:32
 **/
public abstract class CuratorFrameworkCreator {

    public static CuratorFramework createCuratorFramework(CuratorProperties properties,
            RetryPolicy retryPolicy) {
        final Builder builder = CuratorFrameworkFactory.builder()
                .retryPolicy(retryPolicy)
                .connectString(properties.getConnectString())
                .sessionTimeoutMs(properties.getSessionTimeout())
                .connectionTimeoutMs(properties.getConnectionTimeout());
        final List<AuthInfoProperties> auth = properties.getAuth();
        if (!auth.isEmpty()) {
            builder.authorization(
                    auth.stream().map(prop -> new AuthInfo(prop.getSchema(), prop.getAuth()))
                            .collect(Collectors.toList()));
        }
        final CuratorFramework client = builder.build();
        client.start();
        return client;
    }

    public static CuratorFramework createCuratorFramework(CuratorProperties properties) {
        return createCuratorFramework(properties,
                new ExponentialBackoffRetry(properties.getBaseSleepTime(),
                        properties.getMaxRetries(),
                        properties.getMaxSleepTime()));
    }

}
