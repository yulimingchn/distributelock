package com.dawn.banana.distributelock.controller;

import com.dawn.banana.distributelock.entity.UserInfo;
import com.dawn.banana.distributelock.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Dawn on 2018/7/17.
 */
@RestController
@RequestMapping("user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public int insert(@RequestBody UserInfo userInfo){

        return userInfoService.insert(userInfo);

    }

}
