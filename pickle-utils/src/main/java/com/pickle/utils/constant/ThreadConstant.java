package com.pickle.utils.constant;

import java.util.concurrent.*;

public interface ThreadConstant {

    // 线程池配置：CPU核心数 + 1
    int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    // 阻塞队列大小，用于平衡生产者和消费者速度
    int QUEUE_SIZE = 10;
    // 最大错误记录数
    int MAX_ERROR_RECORDS = 1000;

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            4,
            8,
            60L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

}
