package com.dawn.banana.distributelock.redislock;

import com.dawn.banana.distributelock.service.RedisLockService;

/**
 * redis锁多线程并发测试对象
 */
public class RedisLockThread extends Thread{

    private RedisLockService redisLockService;

    public RedisLockThread(RedisLockService redisLockService){
        this.redisLockService = redisLockService;
    }

    public void run(){
        redisLockService.seckill();
    }
}
