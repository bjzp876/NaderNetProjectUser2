package com.nader.intelligent.entity;

import java.io.Serializable;

/**
 * 设备属性
 * author:zhangpeng
 * date: 2019/11/19
 */
public class Attribute<T> implements Serializable {
    private String iotId;
    private long gmtModified;
    private String attribute;
    private String batchId;
    private T value;

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
