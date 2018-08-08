package com.dawn.banana.distributelock.redislock;

import com.dawn.banana.distributelock.service.RedisLockService;

public class RedisLockTest  {

    public static void main(String[] args){
        RedisLockService redisLockService = new RedisLockService();
        for (int i= 0;i<50;i++){
            RedisLockThread redisLockThread = new RedisLockThread(redisLockService);
            redisLockThread.start();
        }

    }

}
