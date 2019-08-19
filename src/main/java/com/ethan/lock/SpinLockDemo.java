package com.ethan.lock;

import com.ethan.executor.ExecutorUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:Guanyp@hiwitech.com">Guanyp</a>
 * @date 2019/8/19
 */
public class SpinLockDemo {

    private AtomicReference<Thread> atomicReference = new AtomicReference<>();

    private ExecutorService executorService;

    public SpinLockDemo() {
        executorService = ExecutorUtils.getExcutorService(3, 5);
    }

    public void getSpinLock() {
        System.out.println(Thread.currentThread().getName() + "\t 尝试获取自旋锁");
        while (!atomicReference.compareAndSet(null, Thread.currentThread())) {}
        System.out.println(Thread.currentThread().getName() + "\t 获得自旋锁");
    }

    public void releaseSpinLock() {
        atomicReference.compareAndSet(Thread.currentThread(), null);
        System.out.println(Thread.currentThread().getName() + "\t 释放自旋锁");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLock = new SpinLockDemo();
        ExecutorService executorService = spinLock.executorService;
        executorService.execute(() -> {
            spinLock.getSpinLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLock.releaseSpinLock();
        });
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.execute(() -> {
            spinLock.getSpinLock();
            spinLock.releaseSpinLock();
        });
    }
}
