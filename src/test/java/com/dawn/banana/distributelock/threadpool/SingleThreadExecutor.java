package com.dawn.banana.distributelock.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dawn on 2018/7/22.
 * 线程中只有一个线程在执行
 * 适用于有明确执行顺序但是不影响主线程的任务，压入池中的任务会按照队列顺序执行
 * 适用无界的LinkedBlockingQueue，要综合考虑生成与消费能力，生成过剩，可能导致堆内存溢出
 */
public class SingleThreadExecutor {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i =0;i<50;i++){
            pool.submit(new ThreadRunner(i+1));
        }
        pool.shutdown();
    }


}
