package com.dawn.banana.distributelock.redislock;

import com.dawn.banana.distributelock.service.RedisLockService;

public class RedisLockThread extends Thread{

    private RedisLockService redisLockService;

    public RedisLockThread(RedisLockService redisLockService){
        this.redisLockService = redisLockService;
    }
    public void run(){
        redisLockService.seckill();
    }
}
