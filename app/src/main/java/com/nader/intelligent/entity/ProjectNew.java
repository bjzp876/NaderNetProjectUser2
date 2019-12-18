package com.nader.intelligent.entity;

import java.io.Serializable;

/**
 * 项目列表
 * author:zhangpeng
 * date: 2019/8/20
 */
public class ProjectNew implements Serializable {

    /**
     * id : f0296bc8-c310-11e9-bb61-00163e0406b6
     * name : 良信电器总部
     * address : 上海市浦东新区申江南路2000号
     */

    private String id;
    private String name;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ProjectNew{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
