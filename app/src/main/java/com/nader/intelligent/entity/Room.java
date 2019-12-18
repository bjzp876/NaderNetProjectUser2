package com.nader.intelligent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 房间及设备
 * author:zhangpeng
 * date: 2019/8/16
 */
public class Room implements Serializable {

    /**
     * id : 004076f6-9a54-461b-963c-f890f2b13a45
     * serialNumber : 01
     * name : 客厅
     * devices : [{"id":"a34981cf-a149-4fda-9c40-30daaae0cd8b","roomId":"004076f6-9a54-461b-963c-f890f2b13a45","serialNumber":"04","name":"ELSOA1-中控主机","productKey":"b1eFvC6jou0","deviceName":null,"nickName":"中控主机","virtualName":"VD_xKcxCT2420","virtualIotId":"6UINndkvlEjWa6azzToi000100","iotId":null,"xstatus":"待组网"}]
     */

    private String id;
    private String serialNumber;
    private String name;
    private List<DevicesBean> devices;
    private boolean isExpanded = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DevicesBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DevicesBean> devices) {
        this.devices = devices;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public static class DevicesBean {
        /**
         * id : a34981cf-a149-4fda-9c40-30daaae0cd8b
         * roomId : 004076f6-9a54-461b-963c-f890f2b13a45
         * serialNumber : 04
         * name : ELSOA1-中控主机
         * productKey : b1eFvC6jou0
         * deviceName : null
         * nickName : 中控主机
         * virtualName : VD_xKcxCT2420
         * virtualIotId : 6UINndkvlEjWa6azzToi000100
         * iotId : null
         * xstatus : 待组网
         */

        private String id;
        private String roomId;
        private String serialNumber;
        private String name;
        private String productKey;
        private String deviceName;
        private String nickName;
        private String virtualName;
        private String virtualIotId;
        private String iotId;
        private String xstatus;
        private boolean isEnd = false;
        private boolean isDms = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getVirtualName() {
            return virtualName;
        }

        public void setVirtualName(String virtualName) {
            this.virtualName = virtualName;
        }

        public String getVirtualIotId() {
            return virtualIotId;
        }

        public void setVirtualIotId(String virtualIotId) {
            this.virtualIotId = virtualIotId;
        }

        public String getIotId() {
            return iotId;
        }

        public void setIotId(String iotId) {
            this.iotId = iotId;
        }

        public String getXstatus() {
            return xstatus;
        }

        public void setXstatus(String xstatus) {
            this.xstatus = xstatus;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public boolean isDms() {
            return isDms;
        }

        public void setDms(boolean dms) {
            isDms = dms;
        }
    }
}
