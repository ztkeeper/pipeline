package com.ztk.pipeline.utils.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AbstractThreadPool implements ScheduleThreadPool {

    private final Class<?> clazz = this.getClass();

    protected final Logger  log = LoggerFactory.getLogger(clazz);

    protected ExecutorService THREAD_POOL = null;

    protected abstract boolean isSchedule();

    public final void shutdown() {
        THREAD_POOL.shutdown();
    }

    public final void execute(Runnable task) {
        THREAD_POOL.execute(task);
    }

    public final Future<?> submit(Runnable task) {
        return THREAD_POOL.submit(task);
    }

    public final <T> Future<T> submit(Callable<T> task) {
        return THREAD_POOL.submit(task);
    }

    public final <T> Future<T> submit(Runnable task, T result) {
        return THREAD_POOL.submit(task, result);
    }

    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return THREAD_POOL.invokeAll(tasks);
    }

    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                               long timeout,
                                               TimeUnit unit) throws InterruptedException {
        return THREAD_POOL.invokeAll(tasks, timeout, unit);
    }

    protected final ThreadFactory defaultThreadFactory() {
        return new DefaultThreadFactory();
    }

    protected final DefaultPolicy defaultPolicy() {
        return new DefaultPolicy();
    }

    protected final CallerRunsPolicy runImmediately() {
        return new CallerRunsPolicy();
    }

    public final void sleep(long millisTimeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisTimeout);
        } catch (InterruptedException e) {
             log.error("error when sleep thread! thread:{}", Thread.currentThread());
            e.printStackTrace();
        }
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (isSchedule()) {
            if (THREAD_POOL instanceof ScheduledExecutorService) {
                ScheduledExecutorService service = (ScheduledExecutorService) THREAD_POOL;
                return service.schedule(command, delay, unit);
            }
        }
        throw new RuntimeException("error when trans class "
                + THREAD_POOL.getClass().getSimpleName()
                + " to ScheduledExecutorService",
                new IllegalAccessException());
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (isSchedule()) {
            if (THREAD_POOL instanceof ScheduledExecutorService) {
                ScheduledExecutorService service = (ScheduledExecutorService) THREAD_POOL;
                return service.schedule(callable, delay, unit);
            }
        }
        throw new RuntimeException("error when trans class "
                + THREAD_POOL.getClass().getSimpleName()
                + " to ScheduledExecutorService",
                new IllegalAccessException());
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (isSchedule()) {
            if (THREAD_POOL instanceof ScheduledExecutorService) {
                ScheduledExecutorService service = (ScheduledExecutorService) THREAD_POOL;
                return service.scheduleAtFixedRate(command, initialDelay, period, unit);
            }
        }
        throw new RuntimeException("error when trans class "
                + THREAD_POOL.getClass().getSimpleName()
                + " to ScheduledExecutorService",
                new IllegalAccessException());
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (isSchedule()) {
            if (THREAD_POOL instanceof ScheduledExecutorService) {
                ScheduledExecutorService service = (ScheduledExecutorService) THREAD_POOL;
                return service.scheduleWithFixedDelay(command, initialDelay, delay, unit);
            }
        }
        throw new RuntimeException("error when trans class "
                + THREAD_POOL.getClass().getSimpleName()
                + " to ScheduledExecutorService",
                new IllegalAccessException());
    }

    private final class DefaultThreadFactory implements ThreadFactory {
        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = clazz.getSimpleName() + "-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            thread.setUncaughtExceptionHandler((t, e) -> {
                StackTraceElement[] stackTraces = e.getStackTrace();
                if (null != stackTraces && stackTraces.length > 0) {
                    StackTraceElement stack = stackTraces[0];
                    // 方法名 lambda$aaa$0
                    String methodName = stack.getMethodName();
                    // 行号 123
                    int methodNameLine = stack.getLineNumber();
                    // 调用类全类名 com.**.**
                    String className = stack.getClassName();
                     log.error("error when run thread {} [class: {}, method: {}, line:{}].",
                            t.getName(),
                            className,
                            methodName,
                            methodNameLine,
                            e);
                    return;
                }
                 log.error("error when run thread {}, error:", t.getName(), e);
            });
            return thread;
        }
    }

    private final class DefaultPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());
        }

    }

    private final class CallerRunsPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (!executor.isShutdown()) {
                 log.warn("Task " + r.toString() + " execute immediately cause thread pool is full!" + executor.toString());
                r.run();
            }
        }
    }
}
