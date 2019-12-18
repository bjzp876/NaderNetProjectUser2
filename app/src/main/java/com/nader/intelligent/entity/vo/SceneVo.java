package com.nader.intelligent.entity.vo;

import com.nader.intelligent.entity.Scene;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhangpeng
 * date: 2019/9/26
 */
public class SceneVo implements Serializable {


    /**
     * id : fbf2077e-60a2-45c2-a92d-22a148249ba2
     * code : 200
     * msg : null
     * data : {"total":5,"data":[{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"16f211faf6a749da9d9d766d74cbf8a2","name":"回家模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"4da5444ba0584f679dc926bd0696920e","name":"布防模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"5540c36aea3a42f69fd5edc7aa52e63d","name":"睡眠模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"c1f2f9a34373470c9d57b3087804aa25","name":"就餐模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"ebe3c5f72823499d98e397c312c43349","name":"离家模式"}],"pageNo":1,"pageSize":20}
     */

    private String id;
    private int code;
    private String msg;
    private DataBeanX data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * total : 5
         * data : [{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"16f211faf6a749da9d9d766d74cbf8a2","name":"回家模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"4da5444ba0584f679dc926bd0696920e","name":"布防模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"5540c36aea3a42f69fd5edc7aa52e63d","name":"睡眠模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"c1f2f9a34373470c9d57b3087804aa25","name":"就餐模式"},{"enable":true,"icon":"https://g.aliplus.com/scene_icons/default.png","sceneId":"ebe3c5f72823499d98e397c312c43349","name":"离家模式"}]
         * pageNo : 1
         * pageSize : 20
         */

        private int total;
        private int pageNo;
        private int pageSize;
        private List<Scene> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<Scene> getData() {
            return data;
        }

        public void setData(List<Scene> data) {
            this.data = data;
        }


    }
}
