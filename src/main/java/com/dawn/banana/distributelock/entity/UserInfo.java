package com.dawn.banana.distributelock.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Dawn on 2018/7/18.
 */

@Table(name = "user_info")
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",length = 50,nullable = false)
    private String name;

    @Column(name = "age",length = 4,nullable = false)
    private Integer age;

    @Column(name = "sex",length = 2,nullable = false)
    private Integer sex;

    @Column(name = "phone",length = 15,nullable = false)
    private String phone;

}
