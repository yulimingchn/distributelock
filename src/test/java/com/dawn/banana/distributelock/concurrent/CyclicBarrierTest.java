package com.dawn.banana.distributelock.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Dawn on 2018/7/22.
 */
public class CyclicBarrierTest {

    public static void main(String [] args){
        int N = 4;
        //当所有线程都到达barrier状态后，会从所有线程中选择一个线程去执行runnable
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N,()-> System.out.println("当前线程"+Thread.currentThread().getName()));
        for (int i=0;i<N;i++){
            //最后一个线程延迟启动，前三个线程达到barrier之后，等待了指定的时间发现第四个线程还没有达到barrier，就抛出异常并继续执行后面的任务
            if (i <N-1){
                new Writer(cyclicBarrier).start();
            }else {
                try {
                    //超时就设置5000ms
                    Thread.sleep(1000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                new Writer(cyclicBarrier).start();
            }

        }

        //主线程睡眠25秒，等待重用cyclicBarrier重用
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("cyclicBarrier重用");
        for (int i=0;i<N;i++){
            new Writer(cyclicBarrier).start();
        }

    }
    static class Writer extends Thread{

        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据。。。");
            try {
                //以线程睡眠来模拟写入数据操作
                Thread.sleep(5000);
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                try {
                    //await方法指定时间
                    cyclicBarrier.await(2000, TimeUnit.MILLISECONDS);
                }catch (TimeoutException e){
                    //Todo Auto-generated catch block
                    e.printStackTrace();
                }

            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务。。。");
        }
    }


}
