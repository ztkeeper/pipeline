package com.ztk.pipeline.reader;

public class ReadTaskProperties {

    /**
     * 首次执行延时
     */
    protected long initialDelay;

    /**
     * 两次开始执行最小间隔时间
     */
    protected long period;

    public long getInitialDelay() {
        return initialDelay;
    }

    public ReadTaskProperties setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public long getPeriod() {
        return period;
    }

    public ReadTaskProperties setPeriod(long period) {
        this.period = period;
        return this;
    }
}
