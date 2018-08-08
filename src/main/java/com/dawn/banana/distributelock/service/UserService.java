package com.dawn.banana.distributelock.service;

import com.dawn.banana.distributelock.aop.DistributedLock;
import org.springframework.stereotype.Service;


/**
 * @author dawn
 */
@Service
public class UserService {

    @DistributedLock(lockName="userInsertLock", waitTime=5, leaseTime=2,tryLock = true,fairLock = true)
    public Integer createUserIfNotExist(String phone){

        return null;
    }




}
