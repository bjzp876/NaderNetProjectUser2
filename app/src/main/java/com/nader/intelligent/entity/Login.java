package com.nader.intelligent.entity;

/**
 * author:zhangpeng
 * date: 2019/8/6
 */
public class Login {

       /**
     * code : 200
     * msg : 登录成功
     * data : {"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUwNzkyOTQsInVzZXJuYW1lIjoiMjA3MzUifQ.xxK8NIlRpBBt_1I5ANUYEVkC9WGbCSt39RusemYgubU"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjUwNzkyOTQsInVzZXJuYW1lIjoiMjA3MzUifQ.xxK8NIlRpBBt_1I5ANUYEVkC9WGbCSt39RusemYgubU
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
