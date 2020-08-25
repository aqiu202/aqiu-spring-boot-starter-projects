package com.github.aqiu202.captcha.exp;

/**
 * <b>验证码图片流写入Servlet异常</b>
 * @author aqiu
 **/
public class CaptchaServletException extends RuntimeException {

    private String text;

    public String getText() {
        return text;
    }

    public CaptchaServletException(String msg) {
        super(msg);
    }

    public CaptchaServletException(String text, String msg) {
        super(msg);
        this.text = text;
    }

    public CaptchaServletException(String text, String msg, Throwable throwable) {
        super(msg, throwable);
        this.text = text;
    }

    public CaptchaServletException(Throwable throwable) {
        super(throwable);
    }

    public CaptchaServletException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
