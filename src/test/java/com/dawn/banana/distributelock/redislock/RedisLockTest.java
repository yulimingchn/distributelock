package com.dawn.banana.distributelock.redislock;

import com.dawn.banana.distributelock.service.RedisLockService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试redis锁
 */
public class RedisLockTest {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        RedisLockService redisLockService = new RedisLockService();
        for (int i = 0; i < 50; i++) {
            pool.submit(new RedisLockThread(redisLockService));
        }
        pool.shutdown();
    }

}
