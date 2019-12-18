package com.nader.intelligent.util.http;

/**
 *
 * 网络请求结果 基类
 * Created by zhouwei on 16/11/10.
 */

public class BaseResponse<T> {
    public String id;
    public int code;
    public Object message;

    public T data;

    public boolean isSuccess(){
        return code == 200;
    }
}
