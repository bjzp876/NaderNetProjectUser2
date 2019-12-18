package com.nader.intelligent.entity;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;

import java.io.Serializable;

/**
 * 设备信息
 * author:zhangpeng
 * date: 2019/8/20
 */
public class Device implements Serializable {
    private boolean isCms = false;
    private DeviceInfo deviceInfo;
    private String roomId;
    String roomName;

    public boolean isCms() {
        return isCms;
    }

    public void setCms(boolean cms) {
        isCms = cms;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Device{" +
                "isCms=" + isCms +
                ", deviceInfo=" + deviceInfo +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}
