package com.github.aqiu202.aurora.jpush.result;

import java.util.List;
import org.apache.http.HttpStatus;

public class JCidPool {

    private List<String> cidlist;

    private int code;

    private JError error;

    public List<String> getCidlist() {
        return cidlist;
    }

    public void setCidlist(List<String> cidlist) {
        this.cidlist = cidlist;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.code == HttpStatus.SC_OK;
    }

    public JError getError() {
        return error;
    }

    public void setError(JError error) {
        this.error = error;
    }

}
