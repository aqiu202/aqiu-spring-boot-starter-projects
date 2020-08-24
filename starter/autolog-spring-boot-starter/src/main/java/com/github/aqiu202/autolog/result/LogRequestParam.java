package com.github.aqiu202.autolog.result;

/**
 * <pre>AutoLog日志封装结果类</pre>
 * @author aqiu 2018年10月24日 下午3:57:10
 */
public interface LogRequestParam {

    String getIp();

    String getUri();

    String getMethod();

    String getDesc();

    Boolean getResult();

    void setIp(String ip);

    void setMethod(String method);

    void setUri(String uri);

    void setDesc(String desc);

    void setResult(Boolean result);

    class DefaultLogRequestParam implements LogRequestParam {

        private String ip;
        private String uri;
        private String method;
        private String desc;
        private boolean result;

        public DefaultLogRequestParam() {

        }

        @Override
        public void setIp(String ip) {
            this.ip = ip;
        }

        @Override
        public void setUri(String uri) {
            this.uri = uri;
        }

        @Override
        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public void setResult(Boolean result) {
            this.result = result;
        }

        @Override
        public String getIp() {
            return ip;
        }

        @Override
        public String getUri() {
            return uri;
        }

        @Override
        public String getMethod() {
            return method;
        }

        @Override
        public String getDesc() {
            return desc;
        }

        @Override
        public Boolean getResult() {
            return result;
        }

    }
}
