package com.trq.xtdemo.common.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 自定义线程池管理器
 * Runnable task = () -> myTask();
 * ThreadPoolManager.execute(task);
 */
@Slf4j
public class ThreadPoolManager {
    private static ThreadPoolExecutor executor;

    public synchronized static void execute(Runnable task) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                        10,
                        30,
                        60,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(2000),
                        new RejectedExecutionHandler() {
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                                executor.submit(r);
                            }
                        });
        } else {
            executor.submit(task);
        }
    }

    public synchronized static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(
                    10,
                    30,
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(2000),
                    new RejectedExecutionHandler() {
                        @Override
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
//                            executor.submit(r);
                        }
                    });
            return executor;
        } else {
            return executor;
        }
    }
}
