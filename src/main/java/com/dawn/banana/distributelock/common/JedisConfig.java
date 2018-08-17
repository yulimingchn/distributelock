package com.dawn.banana.distributelock.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

/**
 * @author dawn
 */
@Configuration
public class JedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${redis.maxTotal}")
    private Integer maxTotal;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        //配置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        ArrayList<JedisShardInfo> arrayList = new ArrayList<>();
        arrayList.add(new JedisShardInfo(host, port));
        return  new ShardedJedisPool(jedisPoolConfig, arrayList);
    }
    @Bean
    public JedisPool getJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(maxTotal);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);
        JedisPool pool = new JedisPool(config, host, port, 3000);
        return pool;
    }

}
