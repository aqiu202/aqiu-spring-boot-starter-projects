package com.github.aqiu202.api.sms.exp;

/**
 * <pre>短信操作异常</pre>
 * @author aqiu 2020/8/19 6:02 下午
**/
public class SmsException extends RuntimeException {

    private String errCode;
    private String requestId;

    public SmsException() {
        super();
    }

    public SmsException(String msg) {
        super(msg);
    }
    public SmsException(String errCode, String errMsg, String requestId) {
        super(errMsg);
        this.errCode = errCode;
        this.requestId = requestId;
    }

    public SmsException(Throwable throwable) {
        super(throwable);
    }

    public SmsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public String getErrCode() {
        return errCode;
    }

    public String getRequestId() {
        return requestId;
    }
}
