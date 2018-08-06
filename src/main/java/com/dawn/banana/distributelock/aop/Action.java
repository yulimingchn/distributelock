package com.dawn.banana.distributelock.aop;

/**
 * @author  Dawn on 2018/7/22.
 */
@FunctionalInterface
public interface Action<T> {

    /**
     * 函数接口
     * @return
     */
     T action();
}
