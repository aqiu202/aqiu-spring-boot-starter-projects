package com.github.aqiu202.starters.jpa.config;


import com.github.aqiu202.starters.jpa.executor.JpaExecutor;

public abstract class JpaExecutorFacade {

    private static JpaExecutor executor;

    public static JpaExecutor getExecutor() {
        return executor;
    }

    static void setExecutor(JpaExecutor executor) {
        JpaExecutorFacade.executor = executor;
    }
}
