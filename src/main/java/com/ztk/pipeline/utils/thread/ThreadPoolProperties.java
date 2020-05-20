package com.ztk.pipeline.utils.thread;

public class ThreadPoolProperties {

    /**
     * 线程池中核心线程数的最大值
     */
    private int corePoolSize = 20;

    /**
     * 线程池中能拥有最多线程数
     */
    private int maximumPoolSize = 200;

    /**
     * 空闲线程存活时间
     */
    private long keepAliveTime = 0L;

    /**
     * 用于缓存任务的阻塞队列
     */
    private int queueSize = 1024;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ThreadPoolProperties setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public ThreadPoolProperties setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public ThreadPoolProperties setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public ThreadPoolProperties setQueueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }
}
