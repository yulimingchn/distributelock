package com.dawn.banana.distributelock.entity;

import javax.persistence.*;

/**
 * Created by Dawn on 2018/7/18.
 */

@Table(name = "user_info")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
