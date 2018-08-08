package com.dawn.banana.distributelock.aop;

/**
 * @author  Dawn on 2018/7/20.
 */
public interface DistributedLockCallback<T> {

    /**
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
     * @return
     */

     T process();

    /**
     * 得到分布式锁的名称
     * @return
     */
    String getLockName();

}
