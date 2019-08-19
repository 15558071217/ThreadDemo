package com.ethan.executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:Guanyp@hiwitech.com">Guanyp</a>
 * @date 2019/8/15
 */
public class ExecutorUtils {
    
    public static ExecutorService getExcutorService(int corePoolSize, int maxPoolSize){
         return new ThreadPoolExecutor(corePoolSize,
                 maxPoolSize,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                new DefaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
    
    static class DefaultThreadFactory implements ThreadFactory{

        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(3,
                5,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                new DefaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try {
            for (int i = 0; i < 9; i++) {
                threadPool.execute(() -> System.out.println(Thread.currentThread().getName() + "执行任务"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }

    }
    
}
