package com.github.aqiu202.api.tts.param;

/**
 * <b>TTS语音合成过程中信号状态信息体</b>
 * <p>存储TTS语音合成过程中产生的所有状态描述信息</p>
 * @author aqiu 2020/2/19 10:48 上午
**/
public interface TtsMessage {

    String getTaskId();

    int getStatus();

    String getStatusText();

    String getMessage();

}
