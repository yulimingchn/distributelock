package com.dawn.banana.distributelock.aop;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dawn on 2018/7/20.
 */
public class SingleDistributedLockTemplate implements DistributedLockTemplate {

    private RedissonClient redisson;

    public SingleDistributedLockTemplate(){}

    public SingleDistributedLockTemplate(RedissonClient redisson){

        this.redisson = redisson;
    }


    @Override
    public Object lock(DistributedLockCallback callback, boolean fairLock) {
        return lock(callback,DEFAULT_TIMW_OUT,DEFAULT_TIME_UNIT,fairLock);
    }

    @Override
    public  Object lock(DistributedLockCallback callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(),fairLock);
        try {
            lock.lock(leaseTime,timeUnit);
            return callback.process();
        }finally {
            if (lock != null && lock.isLocked()){
                lock.unlock();
            }
        }

    }

    @Override
    public Object tryLock(DistributedLockCallback callback, boolean fairLock) {
        return tryLock(callback,DEFAULT_WAIT_TIME,DEFAULT_TIMW_OUT,DEFAULT_TIME_UNIT,fairLock);

    }

    @Override
    public Object tryLock(DistributedLockCallback callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(),fairLock);
        try {
            if (lock.tryLock(waitTime,leaseTime,timeUnit)){
                return callback.process();
            }
        }catch (InterruptedException e){

        }finally {
            if (lock != null && lock.isLocked()){
                lock.unlock();
            }
        }
        return null;
    }

    private RLock getLock(String lockName,boolean fairLock){
        RLock rLock;
        if (fairLock){
            rLock = redisson.getFairLock(lockName);
        }else {
            rLock = redisson.getLock(lockName);
        }
        return rLock;
    }

    public void setRedisson(RedissonClient redisson){

        this.redisson = redisson;
    }

}
