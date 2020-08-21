package com.github.aqiu202.captcha.text;


/**
 * {@link TextProducer} is responsible for creating text.
 */
public interface TextProducer {

    String getText();

    String getText(int length);
}