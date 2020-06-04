package com.realmax.base.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ayuan
 */
public class CustomerThread {
    public static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("CustomerThreadPool");
            /*thread.start();*/
            return thread;
        }
    }, new ThreadPoolExecutor.AbortPolicy());
}
