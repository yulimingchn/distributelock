package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.common.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

/**
 * @author dawn
 * redis
 * 模拟分布式锁操作
 */
@Service
public class RedisLockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockService.class);

    @Autowired
    private JedisPool jedisPool;

    public void seckill() {

        String requestId = Thread.currentThread().getName();
        Jedis jedis = jedisPool.getResource();
        try {
            boolean flag = RedisTool.tryGetDistributedLock(jedis, "resource", requestId, 1000);
            LOGGER.info("获取锁的结果" + flag);
            if (flag) {
                String count = jedis.hget("countMap", "count");
                if (StringUtils.isEmpty(count)) {
                    jedis.hset("countMap", "count", "100");
                } else {
                    Integer countInt = Integer.parseInt(count);
                    LOGGER.info(Thread.currentThread().getName() + "获得了锁");
                    LOGGER.info("count="+--countInt);
                    jedis.hset("countMap", "count", countInt + "");
                }
            }

        } finally {
            boolean flag = RedisTool.releaseDistributedLock(jedis, "resource", requestId);
            LOGGER.info("释放锁的结果" + flag);
        }

    }

}
