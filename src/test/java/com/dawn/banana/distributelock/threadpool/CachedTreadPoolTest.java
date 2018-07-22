package com.dawn.banana.distributelock.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dawn on 2018/7/22.
 * 池中的线程随着处理数据的增加而增加
 * 线程数并不是一直增加，如果有新任务需要执行时，首先查询池中是否有空闲线程并且还未到空闲截止时间
 * 吐过有则使用空闲线程，如果没有，则创建新线程放入其中
 * 用于执行一些生存期很短的异步型人物，不适用于IO等长延时操作，因为这可能会创建大量的线程，导致系统奔溃
 * 使用SynchronousQueue作为阻塞队列，如果有新任务进入队列，必须队列中数据被其他线程处理，否则会等待
 */
public class CachedTreadPoolTest {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i=0;i<50;i++){
            pool.submit(new ThreadRunner(i+1));
        }
        pool.shutdown();
    }

}
