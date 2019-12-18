package com.nader.intelligent.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 场景
 * author:zhangpeng
 * date: 2019/11/25
 */
public class Triggers implements Serializable {

    /**
     * uri : trigger/device/property
     * params : {"iotId":"jtaqN2sGeS8haKpniCo3000100","propertyName":"KeyValue","compareType":"==","compareValue":1}
     */

    private String uri;
    private ParamsBean params;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }


    public static class ParamsBean<T> implements Serializable{
        /**
         * iotId : jtaqN2sGeS8haKpniCo3000100
         * propertyName : KeyValue
         * compareType : ==
         * compareValue : 1
         */

        private String iotId;
        private String propertyName;
        private String compareType;
        private String compareValue;
        private String productKey;
        private String deviceName;
        private String cron;
        private String cronType;
        private String format;
        private String beginDate;
        private String endDate;
        private String repeat;
        private String timeZoneID;
        private T propertyItems;
        private T propertyNamesItems;

        public ParamsBean(T propertyItems,T propertyNamesItems){
            this.propertyItems = propertyItems;
            this.propertyNamesItems = propertyNamesItems;
        }

        public ParamsBean(){

        }

        public String getIotId() {
            return iotId;
        }

        public void setIotId(String iotId) {
            this.iotId = iotId;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getCompareType() {
            return compareType;
        }

        public void setCompareType(String compareType) {
            this.compareType = compareType;
        }

        public String getCompareValue() {
            return compareValue;
        }

        public void setCompareValue(String compareValue) {
            this.compareValue = compareValue;
        }

        public T getPropertyItems() {
            return propertyItems;
        }

        public void setPropertyItems(T propertyItems) {
            this.propertyItems = propertyItems;
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public String getCronType() {
            return cronType;
        }

        public void setCronType(String cronType) {
            this.cronType = cronType;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getRepeat() {
            return repeat;
        }

        public void setRepeat(String repeat) {
            this.repeat = repeat;
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

        public T getPropertyNamesItems() {
            return propertyNamesItems;
        }

        public void setPropertyNamesItems(T propertyNamesItems) {
            this.propertyNamesItems = propertyNamesItems;
        }

        public String getTimeZoneID() {
            return timeZoneID;
        }

        public void setTimeZoneID(String timeZoneID) {
            this.timeZoneID = timeZoneID;
        }

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "iotId='" + iotId + '\'' +
                    ", propertyName='" + propertyName + '\'' +
                    ", compareType='" + compareType + '\'' +
                    ", compareValue='" + compareValue + '\'' +
                    ", productKey='" + productKey + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", cron='" + cron + '\'' +
                    ", cronType='" + cronType + '\'' +
                    ", format='" + format + '\'' +
                    ", beginDate='" + beginDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", repeat='" + repeat + '\'' +
                    ", timezoneID='" + timeZoneID + '\'' +
                    ", propertyItems=" + propertyItems +
                    ", propertyNamesItems=" + propertyNamesItems +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Triggers{" +
                "uri='" + uri + '\'' +
                ", params=" + params.toString() +
                '}';
    }
}
