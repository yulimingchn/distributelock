package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.common.RabbitMQConfig;
import com.dawn.banana.distributelock.entity.UserInfo;
import com.dawn.banana.distributelock.mapper.UserInfoMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Dawn on 2018/7/17.
 */
@Service
@Transactional
public class UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;


    public int insert(UserInfo userInfo){

        userInfoMapper.insert(userInfo);
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userInfo.getName(),userInfo.getId());
        return userInfo.getId();

    }

    public void send(String phone){

        System.out.println(phone+"_"+new Date());

        rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO,phone);

        }


    }


