package com.github.aqiu202.captcha.text.impl;

import com.github.aqiu202.captcha.props.CaptchaProperties.WordProperties;
import com.github.aqiu202.captcha.text.TextProducer;
import com.github.aqiu202.util.RandomUtils;
import javax.annotation.Nonnull;

/**
 * {@link DefaultTextCreator} creates random text from an array of characters
 * with specified length.
 */
public class DefaultTextCreator implements TextProducer {

    private static final int DEFAULT_LENGTH = 4;
    private static final char[] DEFAULT_WORD_CHARS = new char[]{'1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'};
    private int length = DEFAULT_LENGTH;
    private char[] words = DEFAULT_WORD_CHARS;

    public DefaultTextCreator(WordProperties properties) {
        String words = properties.getWords();
        if (words != null && words.length() > 0) {
            this.words = words.toCharArray();
        }
        Integer length = properties.getLength();
        if (length != null) {
            this.length = length;
        }
    }

    public DefaultTextCreator(@Nonnull String words) {
        this.words = words.toCharArray();
    }

    public DefaultTextCreator() {
    }

    public char[] getWords() {
        return words;
    }

    public void setWords(char[] words) {
        this.words = words;
    }

    public void setWords(String words) {
        this.words = words.toCharArray();
    }

    /**
     * @return the random text
     */
    public String getText(int length) {
        StringBuilder text = new StringBuilder();
        int l = words.length;
        for (int i = 0; i < length; i++) {
            text.append(words[RandomUtils.nextInt(l)]);
        }
        return text.toString();
    }

    public String getText() {
        return this.getText(this.length);
    }
}
