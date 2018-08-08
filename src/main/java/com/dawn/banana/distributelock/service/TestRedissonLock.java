package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.aop.DistributedLockCallback;
import com.dawn.banana.distributelock.aop.SingleDistributedLockTemplate;
import com.dawn.banana.distributelock.common.RabbitMQConfig;
import com.dawn.banana.distributelock.entity.UserInfo;
import com.dawn.banana.distributelock.mapper.UserInfoMapper;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Dawn on 2018/7/19.
 */
@Service
public class TestRedissonLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedissonLock.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserInfoMapper userInfoMapper;




    @RabbitListener(queues = RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO)
    public void cameraCallback(String phone){

        LOGGER.info("接受到消息，消息内容为"+phone);
        //RLock rLock = redissonClient.getLock("redisson:lock:phone"+phone);
        //rLock.lock(20, TimeUnit.SECONDS);
        try{
            System.out.println("线程id为"+Thread.currentThread().getId()+"拿到锁休息5秒");
            //Thread.sleep(500);
            //System.out.println("线程id为"+Thread.currentThread().getId()+"醒来继续执行");
            UserInfo userInfo = new UserInfo();
            userInfo.setPhone(phone);
            UserInfo exist =userInfoMapper.selectOne(userInfo);

            if (exist == null){
                userInfo.setAge(18);
                userInfo.setName(phone+Thread.currentThread().getId());
                userInfo.setSex(1);
                userInfoMapper.insert(userInfo);
            }
        }catch (Exception e){

        }finally {
            //rLock.unlock();
        }

    }

    @RabbitListener(queues = RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO_D)
    public void useDistributeLockTemplate(String phone){

        LOGGER.info("接受到消息，消息内容为"+phone);
        SingleDistributedLockTemplate singleDistributedLockTemplate = new SingleDistributedLockTemplate(redissonClient);
        final String lockName = "userInfoLock";
        //lambda的依据是必须有相应的函数接口（函数接口是指内部只有一个抽象方法的接口）
        singleDistributedLockTemplate.tryLock(new DistributedLockCallback() {
            @Override
            public Object process() {
                System.out.println("线程id为"+Thread.currentThread().getId()+"拿到锁");
                UserInfo userInfo = new UserInfo();
                userInfo.setPhone(phone);
                UserInfo exist =userInfoMapper.selectOne(userInfo);
                if (exist == null){
                    userInfo.setName(phone+"thread"+Thread.currentThread().getId());
                    userInfo.setAge(28);
                    userInfo.setSex(1);
                    userInfoMapper.insert(userInfo);
                }
                return null;
            }
            @Override
            public String getLockName() {
                return lockName;
            }
        },true);
    }



}
