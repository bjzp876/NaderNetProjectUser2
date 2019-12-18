package com.nader.intelligent.util;

import android.content.Context;
import android.content.Intent;

import com.nader.intelligent.api.Constants;

/**
 * author:zhangpeng
 * date: 2019/8/22
 */
public class SwitchActivity {

    /**
     * 跳转页面
     * @param context
     * @param productKey
     * @param deviceName
     * @param iotId
     */
    public static void switchDevice(Intent intent, Context context, String productKey, String deviceName, String iotId,String name){
        //一位智能开关
        if (productKey.equals(Constants.ONE_SWITCH)){
//            intent = new Intent(context, OneSwitchActivity.class);
        }
        //二位智能开关
       if (productKey.equals(Constants.TWO_SWITCH)){
//           intent = new Intent(context, TwoSwitchActivity.class);
       }
        //三位智能开关
        if (productKey.equals(Constants.THREE_SWITCH)){
//            intent = new Intent(context, ThreeSwitchActivity.class);
        }
       intent.putExtra("productKey",productKey);
       intent.putExtra("deviceName",deviceName);
       intent.putExtra("iotId",iotId);
       intent.putExtra("name",name);
       context.startActivity(intent);
    }
}
