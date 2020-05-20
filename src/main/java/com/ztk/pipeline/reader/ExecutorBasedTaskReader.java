package com.ztk.pipeline.reader;

import com.ztk.pipeline.Lifecycle;
import com.ztk.pipeline.pipeline.Pipeline;
import com.ztk.pipeline.utils.thread.ThreadPool;
import com.ztk.pipeline.utils.thread.ThreadPoolProperties;

import java.util.concurrent.TimeUnit;

public abstract class ExecutorBasedTaskReader<T extends ExecutorBasedTaskReader> extends AbstractReader implements Lifecycle {

    private final long DEFAULT_INITIAL_DELAY = 1000;
    private final long DEFAULT_PERIOD = 1000;
    private final Integer SINGLE_SCHEDULE_THEAD_POOL = 1;
    protected ThreadPool threadPool;

    private ReadTaskProperties properties;

    public ExecutorBasedTaskReader(Pipeline pipeline) {
        super(pipeline);
    }

    public T properties(ReadTaskProperties properties) {
        this.properties = properties;
        return (T) this;
    }

    @Override
    public void submit(Object msg) {
        super.submit(msg);
    }

    public void asyncSubmit(Object msg) {
        threadPool.submit(() -> super.submit(msg));
    }

    public abstract void handle();

    protected void init() {
    }

    /**
     * org.springframework.context.SmartLifecycle.start()
     */
    @Override
    public final void start() {
        init();
        long initialDelay = (null == properties) ? DEFAULT_INITIAL_DELAY : properties.getInitialDelay();
        long period = (null == properties) ? DEFAULT_PERIOD : properties.getPeriod();
        ThreadPoolProperties threadPoolProperties = new ThreadPoolProperties()
                .setCorePoolSize(SINGLE_SCHEDULE_THEAD_POOL)
                .setMaximumPoolSize(Integer.MAX_VALUE);
        threadPool = new ThreadPool(threadPoolProperties).schedule();
        threadPool.start();
        threadPool.scheduleAtFixedRate(() -> handle(), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * org.springframework.context.SmartLifecycle.stop()
     */
    @Override
    public final void stop() {
        threadPool.shutdown();
    }

    /**
     * org.springframework.context.SmartLifecycle.isRunning()
     */
    @Override
    public final boolean isRunning() {
        return false;
    }

    /**
     * org.springframework.context.SmartLifecycle.isAutoStartup()
     */
    @Override
    public final boolean isAutoStartup() {
        return true;
    }


}
