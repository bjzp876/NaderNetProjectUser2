package com.nader.intelligent.util;

import com.nader.intelligent.api.Constants;

/**
 * 设备类型工具类
 * author:zhangpeng
 * date: 2019/12/4
 */
public class ProductSwitchUtil {

    /**
     * 获取设备访问状态参数
     * @param productKey
     * @return
     */
    public static String getProductStateStr(String productKey){
        String state = "PROPERTY";
        if (productKey.equals(Constants.DOORBELL)||productKey.equals(Constants.EMERGENCY_BUTTON)){
            state = "EVENT";
        }
        return state;
    }
}
