package com.dawn.banana.distributelock.aop;

/**
 * Created by Dawn on 2018/7/22.
 */
@FunctionalInterface
public interface Action<T> {

     T action();
}
