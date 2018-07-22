package com.dawn.banana.distributelock.common;

import com.dawn.banana.distributelock.service.DistributionService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Dawn on 2018/7/22.
 */
public class Worker implements Runnable {

    private final CountDownLatch startSignal;

    private final CountDownLatch doneSignal;

    private final DistributionService distributionService;

    private RedissonClient redissonClient;

    public Worker(CountDownLatch startSignal,CountDownLatch doneSignal,DistributionService service,RedissonClient redissonClient){
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.distributionService = service;
        this.redissonClient = redissonClient;

    }

    @Override
    public void run() {
        try {
            startSignal.await();
            System.out.println(Thread.currentThread().getName()+"start");
            Integer count = distributionService.aspect(() -> {
                RMap<String,Integer> map = redissonClient.getMap("distributionTest");
                Integer count1=map.get("count");
                if (count1 > 0){
                    count1 = count1 -1 ;
                    map.put("count",count1);
                }
                return count1;
            });
            System.out.println(Thread.currentThread().getName()+": count = " + count);
            doneSignal.countDown();
        }catch (InterruptedException ex){

            System.out.println(ex);
        }

    }
}
