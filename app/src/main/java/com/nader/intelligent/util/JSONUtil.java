package com.nader.intelligent.util;

import android.util.Log;

import com.nader.intelligent.api.Constants;
import com.nader.intelligent.entity.Scene;
import com.nader.intelligent.entity.Triggers;
import com.nader.intelligent.entity.Week;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * 场景JSON工具类
 * author:zhangpeng
 * date: 2019/11/28
 */
public class JSONUtil {

    /**
     * 获取场景状态
     * @param json
     * @param identifierByDevice
     * @return
     */
    public static JSONObject getSceneState(String json,String identifierByDevice){
        JSONObject jsonObject = null;
        JSONObject result = null;
        try {
            jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("properties");
            if (jsonArray!=null&&jsonArray.length()>0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.optJSONObject(i);
                    String identifier = item.getString("identifier");
                    if (identifier.equals(identifierByDevice)){
                        JSONObject dataType = item.getJSONObject("dataType");
                        result = dataType.getJSONObject("specs");

                            break;
                    }

                }
                return result;
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 点击事件解析
     * @param uri
     * @param triggers
     * @return
     */
    public static Scene getSceneInfoByProperty(String uri,Triggers triggers){
        Scene scene = null;
        if (uri.contains("roperty")){
            scene = new Scene();
            Week week = new Week();
            week.setIotId(triggers.getParams().getIotId());
            week.setKey(String.valueOf(triggers.getParams().getCompareValue()));
            week.setProductKey(triggers.getParams().getProductKey());
            String name = getName(triggers);
            scene.setName(name);
            scene.setWeek(week);
        }else if(uri.contains("time")){
            scene = new Scene();
            scene.setName("时间");
        }else if(uri.contains("Service")){
            scene = new Scene();
            Week week = new Week();
            week.setIotId(triggers.getParams().getIotId());
            week.setKey(String.valueOf(triggers.getParams().getCompareValue()));
            week.setProductKey(triggers.getParams().getProductKey());
            String name = getName(triggers);
            scene.setName(name);
            scene.setWeek(week);
        }
        return scene;
    }



    private static String getName(Triggers triggers){
        String name = "";
        if (triggers.getParams().getProductKey().equals(Constants.ONE_SWITCH)){
            name = "一键智能开关";
        }else if(triggers.getParams().getProductKey().equals(Constants.TWO_SWITCH)){
            name = "二键智能开关";
        }else if(triggers.getParams().getProductKey().equals(Constants.THREE_SWITCH)){
            name = "三键智能开关";
        }else if(triggers.getParams().getProductKey().equals(Constants.OUTLET)){
            name = "五孔智能插座";
        }else if(triggers.getParams().getProductKey().equals(Constants.SENSOR_PM)){
            name = "PM2.5传感器";
        }else if(triggers.getParams().getProductKey().equals(Constants.TEMPERATURE_HUMIDITY)){
            name = "温湿度传感器";
        }else if(triggers.getParams().getProductKey().equals(Constants.HUMAN_BODY_SENSOR)){
            name = "人体传感器";
        }else if(triggers.getParams().getProductKey().equals(Constants.INFRARED_CURTAIN)){
            name = "紧急按钮";
        }else if(triggers.getParams().getProductKey().equals(Constants.CENTER_CONTROL)){
            name = "智能中控";
        }else if(triggers.getParams().getProductKey().equals(Constants.DOORBELL)){
            name = "音乐门铃";
        }
        return name;
    }
}
