package com.github.aqiu202.zk.prop;

import java.nio.charset.StandardCharsets;
import org.springframework.lang.NonNull;

/**
 * <pre>AuthInfoProperties</pre>
 *
 * @author aqiu 2020/12/10 16:28
 **/
public class AuthInfoProperties {

    private String schema;

    private byte[] auth;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public byte[] getAuth() {
        return auth;
    }

    public void setAuth(@NonNull String auth) {
        this.auth = auth.getBytes(StandardCharsets.UTF_8);
    }
}
