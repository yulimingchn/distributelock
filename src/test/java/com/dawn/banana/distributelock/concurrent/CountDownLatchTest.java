package com.dawn.banana.distributelock.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Dawn on 2018/7/22.
 */
public class CountDownLatchTest {

    public static void main(String [] args){

        final CountDownLatch latch = new CountDownLatch(2);

        getAnonymousThread(latch);


        getAnonymousThread(latch);

        try {
            System.out.println("等待两个子线程执行完毕。。。。");
            latch.await();
            System.out.println("两个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void getAnonymousThread(CountDownLatch latch) {
        new Thread(() -> {
            try {
                System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                Thread.sleep(3000);
                System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
