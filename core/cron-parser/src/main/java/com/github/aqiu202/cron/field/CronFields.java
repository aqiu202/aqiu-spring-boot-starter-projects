package com.github.aqiu202.cron.field;

import java.util.ArrayList;

public abstract class CronFields<T extends CronField> extends ArrayList<T> {

    public abstract CronField merge();
}
