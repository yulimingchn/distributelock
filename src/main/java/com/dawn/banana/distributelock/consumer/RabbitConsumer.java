package com.dawn.banana.distributelock.consumer;

import com.dawn.banana.distributelock.common.RabbitMQConfig;
import com.dawn.banana.distributelock.common.RedisUtils;
import com.dawn.banana.distributelock.common.WebSocketServer;
import com.dawn.banana.distributelock.service.UserInfoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dawn on 2018/7/23.
 */
@Component
public class RabbitConsumer {
    @Autowired
    private UserInfoService userInfoService;

    private static RedisUtils redisUtils =new RedisUtils();

    /**
     * 监听seckill有消息就读取
     *
     */
    @RabbitListener(queues = RabbitMQConfig.RABBITMQ_QUEUE_DAWN_USER_INFO_D)
    public void recieveMessage(String message){
        //收到通道的消息之后执行秒杀操作
        String [] array = message.split(";");
        if (redisUtils.getValue(array[0])!= null){
            //执行业务，秒杀逻辑 Result result = seckillService.startSeckill(Long.parse(array[0]),Long.parse(array[1]))
            //判断结果
            if ("".equals("")){
                //推送给前台
                WebSocketServer.sendInfo("秒杀成功",array[0].toString());
            }else {
                WebSocketServer.sendInfo("秒杀失败",array[0].toString());
                //秒杀结束
                redisUtils.cacheValue(array[0],"ok");
            }
        }else {
            WebSocketServer.sendInfo("秒杀失败",array[0].toString());
        }
    }



}
