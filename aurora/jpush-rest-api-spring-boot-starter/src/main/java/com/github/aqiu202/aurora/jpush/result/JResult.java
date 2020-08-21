package com.github.aqiu202.aurora.jpush.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.http.HttpStatus;

public class JResult {

    @JsonProperty("msg_id")
    private String msgId;

    private JError error;

    private String sendno;

    private int code;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public JError getError() {
        return error;
    }

    public void setError(JError error) {
        this.error = error;
    }

    public String getSendno() {
        return sendno;
    }

    public void setSendno(String sendno) {
        this.sendno = sendno;
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
}
