package com.github.aqiu202.aurora.jpush.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Audience {

    private List<String> alias;

    private List<String> tag;

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public void addAlias(String... alias) {
        if (this.alias == null) {
            this.alias = new ArrayList<String>();
        }
        this.alias.addAll(Arrays.asList(alias));
    }

    public void addTag(String... tag) {
        if (this.tag == null) {
            this.tag = new ArrayList<String>();
        }
        this.tag.addAll(Arrays.asList(tag));
    }

}
