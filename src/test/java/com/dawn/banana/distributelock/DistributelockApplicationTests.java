package com.dawn.banana.distributelock;

import com.dawn.banana.distributelock.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributelockApplicationTests {

    @Autowired
    private UserInfoService userInfoService;



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



}
