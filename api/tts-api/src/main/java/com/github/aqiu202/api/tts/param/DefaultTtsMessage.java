package com.github.aqiu202.api.tts.param;

/**
 * <b>TTS语音合成过程中信号状态信息体（默认实现）</b>
 * <p>存储TTS语音合成过程中产生的所有状态描述信息</p>
 * @author aqiu 2020/2/19 4:07 下午
**/
public class DefaultTtsMessage implements TtsMessage {

    private String taskId;
    private int status;
    private String statusText;
    private String message;

    public DefaultTtsMessage() {

    }

    public DefaultTtsMessage(String taskId, int status, String statusText, String message) {
        this.taskId = taskId;
        this.status = status;
        this.statusText = statusText;
        this.message = message;
    }

    @Override
    public String getTaskId() {
        return this.taskId;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }
}
