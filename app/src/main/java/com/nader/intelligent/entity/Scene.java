package com.nader.intelligent.entity;



import java.io.Serializable;

/**
 * 场景
 * @author sinyuk
 * @date 2018/6/12
 */
public class Scene implements Serializable{
    public String description = "";
    public boolean enable;
    public String icon;
    public String id;
    public String name;
    public int status;
    public String type;
    public boolean isStart = false;
    public String sceneId;
    public String iotId;
    public String cron;
    public Week week;
    public DeviceInfo deviceInfo;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    private int drawableResId = 1;//本地图片

//    public int getDrawableResId() {
//        if (!TextUtils.isEmpty(icon)) {
//            if (icon.contains("all")) {
//                setDrawableResId(R.drawable.all);
//            } else if (icon.contains("auto")) {
//                setDrawableResId(R.drawable.auto);
//            } else if (icon.contains("children")) {
//                setDrawableResId(R.drawable.children);
//            } else if (icon.contains("coffee")) {
//                setDrawableResId(R.drawable.coffee);
//            } else if (icon.contains("cooking")) {
//                setDrawableResId(R.drawable.cooking);
//            } else if (icon.contains("data")) {
//                setDrawableResId(R.drawable.data);
//            } else if (icon.contains("scene_icon")) {
//                setDrawableResId(R.drawable.adefault);
//            } else if (icon.contains("device")) {
//                setDrawableResId(R.drawable.device);
//            } else if (icon.contains("family")) {
//                setDrawableResId(R.drawable.family);
//            } else if (icon.contains("gohome")) {
//                setDrawableResId(R.drawable.gohome);
//            } else if (icon.contains("holiday")) {
//                setDrawableResId(R.drawable.holiday);
//            } else if (icon.contains("home")) {
//                setDrawableResId(R.drawable.home);
//            } else if (icon.contains("leavehome")) {
//                setDrawableResId(R.drawable.leavehome);
//            } else if (icon.contains("light.")) {
//                setDrawableResId(R.drawable.light);
//            } else if (icon.contains("manual")) {
//                setDrawableResId(R.drawable.manual);
//            } else if (icon.contains("morning")) {
//                setDrawableResId(R.drawable.morning);
//            } else if (icon.contains("music")) {
//                setDrawableResId(R.drawable.music);
//            } else if (icon.contains("night")) {
//                setDrawableResId(R.drawable.night);
//            } else if (icon.contains("phone")) {
//                setDrawableResId(R.drawable.phone);
//            } else if (icon.contains("sensor")) {
//                setDrawableResId(R.drawable.sensor);
//            } else if (icon.contains("time")) {
//                setDrawableResId(R.drawable.time);
//            } else {
//                return R.drawable.adefault;
//            }
//        }
//
//        return drawableResId;
//    }

//    public void setDrawableResId(int drawableResId) {
//        this.drawableResId = drawableResId;
//    }


    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
