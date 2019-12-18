package com.nader.intelligent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 项目
 * author:zhangpeng
 * date: 2019/8/16
 */
public class Project implements Serializable {
    /**
     * id : 18b146fdc7ae4ea5bfb320720d234205
     * name : 万科国际花苑
     * address : 上海市静安区
     * houses : [{"id":"53asfasdf2asdfasdf","name":"东方情操"}]
     */

    private String id;
    private String name;
    private String address;
    private List<HousesBean> houses;

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

    public List<HousesBean> getHouses() {
        return houses;
    }

    public void setHouses(List<HousesBean> houses) {
        this.houses = houses;
    }

    public static class HousesBean implements Serializable {
        /**
         * id : 53asfasdf2asdfasdf
         * name : 东方情操
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
    }
}
