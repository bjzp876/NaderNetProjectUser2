package com.nader.intelligent.entity;

import java.io.Serializable;

/**
 * 房屋数据模型
 * author:zhangpeng
 * date: 2019/8/20
 */
public class HouseNew implements Serializable {

    /**
     * id : 2db7684b-c311-11e9-bb61-00163e0406b6
     * name : 1101
     */

    private String id;
    private String name;

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

    @Override
    public String toString() {
        return "HouseNew{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
