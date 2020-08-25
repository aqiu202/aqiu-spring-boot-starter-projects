package com.github.aqiu202.api.tts.config;

/**
 * <b>TTS语音合成通用配置</b>
 * <p>保存所有TTS语音合成的属性配置选项</p>
 * @author aqiu 2020/2/19 10:42 上午
 **/
public class TtsConfiguration {

    protected String format = "MP3";
    protected String voice = "xiaoyun";
    protected int sampleRate = 8000;
    protected int pitchRate = 0;
    protected int speechRate = 0;
    protected String text = "你好";

    public TtsConfiguration setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getVoice() {
        return voice;
    }

    public TtsConfiguration setVoice(String voice) {
        this.voice = voice;
        return this;
    }

    public TtsConfiguration setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public int getPitchRate() {
        return pitchRate;
    }

    public TtsConfiguration setPitchRate(int pitchRate) {
        this.pitchRate = pitchRate;
        return this;
    }

    public int getSpeechRate() {
        return speechRate;
    }

    public TtsConfiguration setSpeechRate(int speechRate) {
        this.speechRate = speechRate;
        return this;
    }

    public String getText() {
        return text;
    }

    public TtsConfiguration setText(String text) {
        this.text = text;
        return this;
    }
}
