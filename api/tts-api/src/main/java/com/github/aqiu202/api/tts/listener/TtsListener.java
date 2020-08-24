package com.github.aqiu202.api.tts.listener;

import com.github.aqiu202.api.tts.param.TtsMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>通用语音合成的监听器</b>
 * <p>监听TTS服务在语音合成阶段的各个事件，并作出相应处理</p>
 * @author aqiu 2020/2/19 10:46 上午
**/
public class TtsListener {

    private final Logger logger = LoggerFactory.getLogger(TtsListener.class);

    private Consumer<ByteBuffer> handler;
    private File file;
    private FileOutputStream fout;

    public TtsListener(String filePath) {
        this.file = new File(filePath);
    }

    public TtsListener(File file) {
        this.file = file;
    }

    public TtsListener(FileOutputStream fout) {
        this.fout = fout;
    }

    public TtsListener(Consumer<ByteBuffer> handler) {
        this.handler = handler;
    }

    public TtsListener(Consumer<ByteBuffer> handler, String filePath) {
        this.handler = handler;
        this.file = new File(filePath);
    }

    public TtsListener(Consumer<ByteBuffer> handler, File file) {
        this.handler = handler;
        this.file = file;
    }

    public TtsListener(Consumer<ByteBuffer> handler, FileOutputStream fout) {
        this.handler = handler;
        this.fout = fout;
    }

    public void onComplete(TtsMessage message) {
        logger.debug("task_id: {}, status: {}, status_text: {}", message.getTaskId(),
                message.getStatus(), message.getStatusText());
        if (Objects.nonNull(this.file)) {
            logger.info("语音合成完毕：{}", this.file.getAbsolutePath());
        } else {
            logger.info("语音合成完毕");
        }
        this.closeStream();

    }

    public void onFail(TtsMessage message) {
        logger.error("task_id: {}, status: {}, status_text: {}", message.getTaskId(),
                message.getStatus(), message.getStatusText());
        this.closeStream();

    }

    public void onMessage(ByteBuffer message) {
        try {
            if (Objects.nonNull(this.file)) {
                if (Objects.isNull(this.fout)) {
                    this.fout = new FileOutputStream(this.file);
                }
            }
            if (Objects.nonNull(this.fout)) {
                byte[] bytesArray = new byte[message.remaining()];
                message.get(bytesArray, 0, bytesArray.length);
                this.fout.write(bytesArray);
            }
            if (Objects.nonNull(this.handler)) {
                this.handler.accept(message);
            }
        } catch (IOException e) {
            logger.error("接收文件输出流失败：{}", e.getMessage());
        }
    }

    public void closeStream() {
        try {
            if (null != this.fout) {
                this.fout.close();
            }
        } catch (IOException e1) {
            logger.error("关闭文件输出流失败：{}", e1.getMessage());
        }
    }
}
