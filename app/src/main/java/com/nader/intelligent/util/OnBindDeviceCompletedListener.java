package com.nader.intelligent.util;

/**
 * author:zhangpeng
 * date: 2019/8/21
 */
public interface OnBindDeviceCompletedListener {
    void onSuccess(String iotId);

    void onFailed(Exception e);

    void onFailed(int code, String message, String localizedMsg);
}
