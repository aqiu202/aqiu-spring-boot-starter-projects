package com.github.aqiu202.aurora.jpush.param;

public enum Style {

    Default(0), BigText(1), Inbox(2), BigPicture(3);

    private int value;

    Style(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
