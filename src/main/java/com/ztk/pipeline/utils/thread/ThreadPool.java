/*
（1）如果没有空闲的线程执行该任务且当前运行的线程数少于corePoolSize，则添加新的线程执行该任务。

（2）如果没有空闲的线程执行该任务且当前的线程数等于corePoolSize同时阻塞队列未满，则将任务入队列，而不添加新的线程。

（3）如果没有空闲的线程执行该任务且阻塞队列已满同时池中的线程数小于maximumPoolSize，则创建新的线程执行任务。

（4）如果没有空闲的线程执行该任务且阻塞队列已满同时池中的线程数等于maximumPoolSize，则根据构造函数中的handler指定的策略来拒绝新的任务。
 */
package com.ztk.pipeline.utils.thread;

import java.util.concurrent.*;

public final class ThreadPool extends AbstractThreadPool {

    private ThreadPoolProperties properties;

    private boolean isRejected;

    private boolean isSchedule;

    public ThreadPool(ThreadPoolProperties properties) {
        this.properties = properties;
    }

    public ThreadPool rejectPolicy() {
        this.isRejected = true;
        return this;
    }

    public ThreadPool schedule() {
        this.isSchedule = true;
        return this;
    }

    @Override
    protected boolean isSchedule() {
        return this.isSchedule;
    }

    public void start() {
        if (this.isSchedule) {
            createSchedulePool();
        } else {
            createDefaultPool();
        }
    }

    private void createDefaultPool() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(properties.getQueueSize());
        THREAD_POOL = new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                workQueue,
                defaultThreadFactory(),
                isRejected ? defaultPolicy() : runImmediately());
    }

    private void createSchedulePool() {
        THREAD_POOL = Executors.newScheduledThreadPool(properties.getCorePoolSize(), defaultThreadFactory());
    }

    public static void localSleep(long millisTimeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void localSleep(Runnable task, long millisTimeout) {
        try {
            new Thread(task).start();
            TimeUnit.MILLISECONDS.sleep(millisTimeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
