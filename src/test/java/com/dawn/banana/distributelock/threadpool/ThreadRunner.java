package com.dawn.banana.distributelock.threadpool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dawn on 2018/7/22.
 */
public class ThreadRunner implements Runnable{

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
    /**
     * 线程私有属性，创建线程时创建
     */
    private Integer num;

    public ThreadRunner(Integer num){

        this.num = num;
    }

    @Override
    public void run() {
        System.out.println("thread:"+Thread.currentThread().getName()+",time:"+format.format(new Date())+",num:"+num);
        try {
            //使线程睡眠，模拟线程阻塞情况
            TimeUnit.SECONDS.sleep(1);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
