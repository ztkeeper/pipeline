package com.ztk.pipeline;

public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();

    boolean isAutoStartup();

}
