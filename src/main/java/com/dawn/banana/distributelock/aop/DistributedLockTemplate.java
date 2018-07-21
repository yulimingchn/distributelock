package com.dawn.banana.distributelock.aop;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dawn on 2018/7/20.
 * 分布式锁模板
 */
public interface DistributedLockTemplate<T> {

    long DEFAULT_WAIT_TIME = 30;

    long DEFAULT_TIMW_OUT = 5;

    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 使用分布式锁，使用锁默认超时时间
     * @param callback
     * @param fairLock 是否使用公平锁
     */
     T lock(DistributedLockCallback<T> callback,boolean fairLock);


    /**
     * 使用分布式锁，自定义锁的超时时间
     * @param callback
     * @param leaseTime 锁超时时间，超时后自动释放锁
     * @param timeUnit
     * @param fairLock 是否使用公平锁
     * @param <T>
     * @return
     */
     T lock(DistributedLockCallback<T> callback,long leaseTime,TimeUnit timeUnit,boolean fairLock);

    /**
     * 尝试分布式锁，使用默认的等待时间，超时时间
     * @param callback
     * @param fairLock
     * @param <T>
     * @return
     */
    T tryLock(DistributedLockCallback<T> callback,boolean fairLock);


    /**
     * 尝试分布式锁，自定义等待时间，超时时间
     * @param callback
     * @param waitTime 获取锁的最长等待时间
     * @param leaseTime 锁超时时间，超时后自动释放
     * @param timeUnit
     * @param fairLock 是否使用公平锁
     * @param <T>
     * @return
     */
     T tryLock(DistributedLockCallback<T> callback,long waitTime,long leaseTime,TimeUnit timeUnit,boolean fairLock);

}
