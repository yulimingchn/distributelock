package com.dawn.banana.distributelock.common;

import com.dawn.banana.distributelock.aop.DistributedLock;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Created by Dawn on 2018/7/22.
 */
@Component
public class DistributedLockManager {

    @DistributedLock(argNum = 1, lockNamePost = ".lock")
    public Integer aspect(String lockName, Worker1 worker1) {
        return worker1.aspectBusiness(lockName);
    }

    @DistributedLock(lockName = "lock", lockNamePost = ".lock")
    public int aspect(Supplier<Integer> supplier) {
        return supplier.get();
    }


}
