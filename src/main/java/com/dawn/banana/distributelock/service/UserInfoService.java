package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.common.RabbitMQConfig;
import com.dawn.banana.distributelock.entity.UserInfo;
import com.dawn.banana.distributelock.mapper.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author  Dawn on 2018/7/17.
 */
@Service
@Transactional
public class UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoService.class);
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private UserService userService;


    public int insert(UserInfo userInfo){

        userInfoMapper.insert(userInfo);
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userInfo.getName(),userInfo.getId());
        return userInfo.getId();

    }

    /**
     * 消息队列同步积分流水信息
     */
    //@RabbitListener(queues = RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO)
    public void pointsFlowSync(String message){
        LOGGER.info("接收到积分流水同步消息，消息内容为"+message);
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(message);
        int exist = userInfoMapper.selectCount(userInfo);
        if (exist>1){
            throw new RuntimeException("用户明细数据重复！");
        }

        if (exist < 0){
             userService.createUserIfNotExist(message);
        }
        System.out.println("线程："+Thread.currentThread().getName()+"执行消息为："+message);
    }

    public void send(String phone){

        System.out.println(phone+"_"+new Date());

        rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO,phone);

        }

    public void sendD(String phone){

        System.out.println(phone+"_"+new Date());

        rabbitTemplate.convertAndSend(RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO_D,phone);

    }


    }


