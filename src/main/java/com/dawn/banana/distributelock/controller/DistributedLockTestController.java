package com.dawn.banana.distributelock.controller;

import com.dawn.banana.distributelock.common.DistributedLockManager;
import com.dawn.banana.distributelock.common.Worker1;
import com.dawn.banana.distributelock.service.DistributionService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Dawn on 2018/7/22.
 */
@RestController
@RequestMapping("distribute/lock")
public class DistributedLockTestController {
    private int count = 10;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DistributedLockManager distributedLockManager;

    @RequestMapping(method = RequestMethod.GET)
    public String distributedLockTest()throws Exception{
        RMap<String,Integer> map = redissonClient.getMap("distributionTest");
        map.put("count",8);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(count);
        for (int i=0;i< count;i++){
            new Thread(new Worker1(startSignal,doneSignal,distributedLockManager,redissonClient)).start();
        }
        //let all threads proceed,让所有线程通过
        startSignal.countDown();
        doneSignal.await();
        System.out.println("all processors done.ShutDown connection");
        return "finish";
    }


}
