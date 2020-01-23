package com.kamingpan.infrastructure.core.util;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadMdcUtil
 *
 * @author kamingpan
 * @since 2020-01-10
 */
public class ThreadMdc {

    /**
     * 追踪id键
     */
    public final static String TRACE_KEY = "traceId";

    public static String getTraceId() {
        return " " + UUID.randomUUID().toString().replace("-", "") + " -";
    }

    public static void setTraceIdIfAbsent() {
        if (MDC.get(ThreadMdc.TRACE_KEY) == null) {
            MDC.put(ThreadMdc.TRACE_KEY, ThreadMdc.getTraceId());
        }
    }

    public static void setTraceId() {
        MDC.put(ThreadMdc.TRACE_KEY, ThreadMdc.getTraceId());
    }

    public static void setTraceId(String traceId) {
        MDC.put(ThreadMdc.TRACE_KEY, traceId);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static class ThreadPoolTaskExecutorMdcWrapper extends ThreadPoolTaskExecutor {
        @Override
        public void execute(Runnable runnable) {
            super.execute(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()));
        }

        @Override
        public void execute(Runnable runnable, long startTimeout) {
            super.execute(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()), startTimeout);
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            return super.submit(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            return super.submit(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()));
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable runnable) {
            return super.submitListenable(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> callable) {
            return super.submitListenable(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
        }
    }

    public static class ThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {
        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                            RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public void execute(Runnable task) {
            super.execute(ThreadMdc.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> Future<T> submit(Runnable runnable, T result) {
            return super.submit(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()), result);
        }

        @Override
        public <T> Future<T> submit(Callable<T> callable) {
            return super.submit(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
        }

        @Override
        public Future<?> submit(Runnable runnable) {
            return super.submit(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()));
        }
    }

    public static class ForkJoinPoolMdcWrapper extends ForkJoinPool {
        public ForkJoinPoolMdcWrapper() {
            super();
        }

        public ForkJoinPoolMdcWrapper(int parallelism) {
            super(parallelism);
        }

        public ForkJoinPoolMdcWrapper(int parallelism, ForkJoinWorkerThreadFactory factory,
                                      Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
            super(parallelism, factory, handler, asyncMode);
        }

        @Override
        public void execute(Runnable runnable) {
            super.execute(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ForkJoinTask<T> submit(Runnable runnable, T result) {
            return super.submit(ThreadMdc.wrap(runnable, MDC.getCopyOfContextMap()), result);
        }

        @Override
        public <T> ForkJoinTask<T> submit(Callable<T> callable) {
            return super.submit(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
        }
    }
}
