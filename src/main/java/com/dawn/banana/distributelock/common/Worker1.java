package com.dawn.banana.distributelock.common;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.concurrent.CountDownLatch;

/**
 * @author  Dawn on 2018/7/22.
 */
public class Worker1 implements Runnable {

    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private final DistributedLockManager distributedLockManager;
    private RedissonClient redissonClient;

    public Worker1(CountDownLatch startSignal, CountDownLatch doneSignal, DistributedLockManager distributedLockManager, RedissonClient redissonClient) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.distributedLockManager = distributedLockManager;
        this.redissonClient = redissonClient;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName() + " start");


            startSignal.await();

            Integer count = distributedLockManager.aspect(() -> aspect());

            System.out.println(Thread.currentThread().getName() + ": count = " + count);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            doneSignal.countDown();
        }
    }

    public int aspect(String lockName) {
        return distributedLockManager.aspect(lockName, this);
    }

    public Integer aspectBusiness(String lockName) {
        RMap<String, Integer> map = redissonClient.getMap("distributionTest");

        Integer count = map.get("count");

        if (count > 0) {
            count = count - 1;
            map.put("count", count);
        }

        return count;
    }

    private Integer aspect() {
        RMap<String, Integer> map = redissonClient.getMap("distributionTest");

        Integer count1 = map.get("count");
        if (count1 > 0) {
            count1 = count1 - 1;
            map.put("count", count1);
        }
        return count1;
    }


}
