package com.github.aqiu202.util.wrap;


import javax.annotation.Nonnull;

public class BooleanWrapper implements Wrapper<Boolean> {

    private Boolean value = Boolean.FALSE;

    public BooleanWrapper() {

    }

    public BooleanWrapper(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public void set(@Nonnull Boolean value) {
        this.value = value;
    }

    public void not() {
        this.value = !this.value;
    }

}
