package com.nader.intelligent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 设备属性
 * author:zhangpeng
 * date: 2019/11/19
 */
public class RoomDevice implements Serializable {
    private String netType;
    private String spaceNamePath;
    private String categoryKey;
    private boolean isEdgeGateway;
    private String nodeType;
    private String productKey;
    private String categoryName;
    private String deviceName;
    private String productName;
    private String iotId;
    private String productImage;
    private String lastOnlineTime;
    private String thingType;
    private int categoryId;
    private int status;
    private String nickName;
    private List<Attribute> attributeList;

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getSpaceNamePath() {
        return spaceNamePath;
    }

    public void setSpaceNamePath(String spaceNamePath) {
        this.spaceNamePath = spaceNamePath;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public boolean isEdgeGateway() {
        return isEdgeGateway;
    }

    public void setEdgeGateway(boolean edgeGateway) {
        isEdgeGateway = edgeGateway;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getThingType() {
        return thingType;
    }

    public void setThingType(String thingType) {
        this.thingType = thingType;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "RoomDevice{" +
                "netType='" + netType + '\'' +
                ", spaceNamePath='" + spaceNamePath + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", isEdgeGateway=" + isEdgeGateway +
                ", nodeType='" + nodeType + '\'' +
                ", productKey='" + productKey + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", productName='" + productName + '\'' +
                ", iotId='" + iotId + '\'' +
                ", productImage='" + productImage + '\'' +
                ", lastOnlineTime='" + lastOnlineTime + '\'' +
                ", thingType='" + thingType + '\'' +
                ", categoryId=" + categoryId +
                ", status=" + status +
                ", nickName='" + nickName + '\'' +
                ", attributeList=" + attributeList +
                '}';
    }
}
