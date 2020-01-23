package com.kamingpan.infrastructure.core.configuration.async;

import com.kamingpan.infrastructure.core.util.ThreadMdc;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * 线程池配置
 *
 * @author kamingpan
 * @since 2020-01-10
 */
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public <T> Future<T> submit(Callable<T> callable) {
                // 传入线程池之前先复制当前线程的MDC
                return super.submit(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
            }

            @Override
            public void execute(Runnable callable) {
                super.execute(ThreadMdc.wrap(callable, MDC.getCopyOfContextMap()));
            }
        };

        // 线程池配置
        /*// 核心线程数
        executor.setCorePoolSize(config.getPoolCoreSize());
        // 最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        // 队列大小
        executor.setQueueCapacity(config.getQueueCapacity());*/

        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁。
        // 同时，这里还设置了setAwaitTerminationSeconds(60)，
        // 该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

}
