package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.aop.Action;
import com.dawn.banana.distributelock.aop.DistributedLock;
import com.dawn.banana.distributelock.entity.UserInfo;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author  Dawn on 2018/7/21.
 */
@Service
public class DistributionService {

    @Autowired
    private RedissonClient redissonClient;

    @DistributedLock(param = "id",lockNamePost = ".lock")
    public Integer aspect(UserInfo userInfo){

        RMap<String,Integer> map = redissonClient.getMap("distributionTest");
        Integer count = map.get("count");
        if (count > 0){
            count = count -1;
            map.put("count",count);
        }
        return count;
    }

    @DistributedLock(argNum = 1,lockNamePost = ".lock")
    public Integer aspect(String string){
        RMap<String,Integer> map = redissonClient.getMap("distributionTest");
        Integer count = map.get("count");
        if (count > 0){
            count = count -1;
            map.put("count",count);
        }
        return count;
    }

    @DistributedLock(lockName = "lock",lockNamePost = ".lock")
   public int aspect(Action<Integer> action){

        return action.action();

   }

}
