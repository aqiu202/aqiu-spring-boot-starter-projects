package com.github.aqiu202.qlock.aop;

import com.github.aqiu202.lock.base.LockValueHolder;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * <pre>QLockRequestListener</pre>
 *
 * @author aqiu 2020/12/10 11:08
 **/
public class QLockRequestListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        LockValueHolder.remove();
    }
}
