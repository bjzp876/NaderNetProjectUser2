package com.nader.intelligent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhangpeng
 * date: 2019/11/8
 */
public class Rooms implements Serializable {

    /**
     * pageSize : 20
     * data : [{"roomId":"h4864ft3PugZrfDhpRsv000095fe00","deviceCount":5,"houseId":"OIQr8nAlg0P6vaUePEiC00002f2b00","roomName":"客厅","roomOrder":0,"roomPicture":""}]
     * total : 1
     * pageNo : 1
     */

    private int pageSize;
    private int total;
    private int pageNo;
    private List<DataBean> data;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * roomId : h4864ft3PugZrfDhpRsv000095fe00
         * deviceCount : 5
         * houseId : OIQr8nAlg0P6vaUePEiC00002f2b00
         * roomName : 客厅
         * roomOrder : 0
         * roomPicture :
         */

        private String roomId;
        private int deviceCount;
        private String houseId;
        private String roomName;
        private int roomOrder;
        private String roomPicture;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public int getDeviceCount() {
            return deviceCount;
        }

        public void setDeviceCount(int deviceCount) {
            this.deviceCount = deviceCount;
        }

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getRoomOrder() {
            return roomOrder;
        }

        public void setRoomOrder(int roomOrder) {
            this.roomOrder = roomOrder;
        }

        public String getRoomPicture() {
            return roomPicture;
        }

        public void setRoomPicture(String roomPicture) {
            this.roomPicture = roomPicture;
        }
    }
}
