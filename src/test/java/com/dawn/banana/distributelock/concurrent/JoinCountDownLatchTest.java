package com.dawn.banana.distributelock.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dawn on 2018/7/22.
 * 注意：1，批次请求之间不能有执行顺序的要求，否则多个线程并发处理无法保证请求的执行顺序
 * 2.各线程操作的结果列表必须是线程安全的
 * 3.各子线程的countDown操作需要在finally中执行，确保一定可以执行
 * 4.主线程的await操作需要设置超时时间，避免因子线程处理异常而长时间一直等待，如果中断需要抛出异常或返回错误结果
 * 4.如果一个批次请求数很多，会瞬间占用服务器大量的线程，此时必须使用线程池，并限定最大可处理的线程数量，否则服务器不稳定性会大幅提升
 * 5.主线程和子线程的数据传输变得困难，稍不注意会造成线程不安全的问题，且代码可读性有一定下降
 */
public class JoinCountDownLatchTest {

    public static void main(String[] args) throws InterruptedException{
        Thread parser1 = new Thread(()-> System.out.println("parser1 finish"));

        Thread parser2 = new Thread(()-> System.out.println("parser2 finish"));
        parser1.start();
        parser2.start();
        parser1.join();
        parser2.join();
        System.out.println("all parser finish");

    }

    public List<Integer> countDownDeal(List<Integer> batchInteger){

        //定义线程安全的处理结果列表
        List<Integer> countDownResultList = Collections.synchronizedList(new ArrayList<Integer>());
        if (batchInteger != null){
            //定义countDownLatch线程数，有多少个请求，我们就定义多少个
            CountDownLatch runningThreadNum = new CountDownLatch(batchInteger.size());
            for (Integer num :batchInteger){
                //循环便利请求，并实例化线程(构造函数传入CountDownLatch类型的runningThreadNum)，立刻启动
                DealWorker dealWorker = new DealWorker(num,runningThreadNum,countDownResultList);
                new Thread(dealWorker).start();

            }
            try {
                //调用countDownLatch的await方法等待一分钟，如果countDownLatch没有清零，则认为超时，返回false，可以根据实际情况选择处理
                runningThreadNum.await(1, TimeUnit.MINUTES);
            }catch (InterruptedException e){

                return null;
            }

        }
        return countDownResultList;

    }

    private class DealWorker implements Runnable{
        /**
         * 正在运行的线程数
         */
        private CountDownLatch runningThreadNum;

        /**
         * 待处理请求
         */
        private Integer num;

        /**
         * 待返回结果列表
         */
        private List<Integer> countDownResultList;

        private DealWorker(Integer num,CountDownLatch runningThreadNum,List<Integer> countDownResultList){
            this.countDownResultList = countDownResultList;
            this.num = num;
            this.runningThreadNum = runningThreadNum;

        }


        @Override
        public void run() {
            try {
                this.countDownResultList.add(num);
            }finally {
                //当前线程处理完成，runningThreadNum线程数减1，此操作必须在finally中完成，
                // 避免处理异常后造成runningThread线程数无法清零
                this.runningThreadNum.countDown();;
            }
        }
    }

}
