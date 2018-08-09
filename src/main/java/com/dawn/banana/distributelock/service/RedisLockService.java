package com.dawn.banana.distributelock.service;
import com.dawn.banana.distributelock.common.RedisTool;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

/**
 * @author dawn
 * 模拟分布式锁操作
 */
public class RedisLockService {

    private static JedisPool pool ;


    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "192.168.4.111", 6379, 3000);
    }


    public void seckill() {

        String requestId = Thread.currentThread().getName();
        Jedis jedis = pool.getResource();
        try {
            boolean flag = RedisTool.tryGetDistributedLock(jedis, "resource", requestId, 1000);
            System.out.println("获取锁的结果" + flag);
            if (flag) {
                String count = jedis.hget("countMap", "count");
                if (StringUtils.isEmpty(count)) {
                    jedis.hset("countMap", "count", "100");
                } else {
                    Integer countInt = Integer.parseInt(count);
                    System.out.println(Thread.currentThread().getName() + "获得了锁");
                    System.out.println(--countInt);
                    jedis.hset("countMap", "count", countInt + "");
                }
            }

        } finally {
            boolean flag = RedisTool.releaseDistributedLock(jedis, "resource", requestId);
            System.out.println("释放锁的结果" + flag);
        }

    }

}
