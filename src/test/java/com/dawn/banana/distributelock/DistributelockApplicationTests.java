package com.dawn.banana.distributelock;

import com.dawn.banana.distributelock.redislock.RedisLockThread;
import com.dawn.banana.distributelock.service.RedisLockService;
import com.dawn.banana.distributelock.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributelockApplicationTests {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisLockService redisLockService ;


	@Test
	public void contextLoads() {

	}

	@Test
	public void testDistributeLock(){

	    for (int i=100;i<1000;i++){
	        int num = i/10;

	        String phone = "18756288" + num;

	        userInfoService.send(phone);

        }


	}


	@Test
	public void testDistributeLockTemplate(){

		for (int i=100;i<1000;i++){
			int num = i/10;

			String phone = "150553542" + num;

			userInfoService.sendD(phone);

		}


	}


	@Test
	public void testRedisLock(){

		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			pool.submit(new RedisLockThread(redisLockService));
			System.out.println("i的值为"+i);
		}
		pool.shutdown();
	}


	}



