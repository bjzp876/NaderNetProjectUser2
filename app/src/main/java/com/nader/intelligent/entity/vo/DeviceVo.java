package com.nader.intelligent.entity.vo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 数据库devices
 * author:zhangpeng
 * date: 2019/8/22
 */
@Entity
public class DeviceVo  {
    /**
     * id : 16e3aae0-c312-11e9-bb61-00163e0406b6
     * serialNumber : 01
     * name : E1SC10-智能中控主机
     * productKey : b1eFvC6jou0
     */
    @Id(autoincrement = true)
    @Property(nameInDb = "tableId")
    private Long tableId;
    @Property(nameInDb = "id")
    private String id;
    @Property(nameInDb = "serialNumber")
    private String serialNumber;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "productKey")
    private String productKey;
    @Property(nameInDb = "iotId")
    @Index
    private String iotId;
    @Property(nameInDb = "deviceName")
    private String deviceName;
    @Transient
    private boolean isEnd = false;
    @Transient
    private boolean isDms = false;

    @Generated(hash = 1977485789)
    public DeviceVo(Long tableId, String id, String serialNumber, String name,
            String productKey, String iotId, String deviceName) {
        this.tableId = tableId;
        this.id = id;
        this.serialNumber = serialNumber;
        this.name = name;
        this.productKey = productKey;
        this.iotId = iotId;
        this.deviceName = deviceName;
    }

    @Generated(hash = 1818535292)
    public DeviceVo() {
    }

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

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
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

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "DeviceVo{" +
                "tableId=" + tableId +
                ", id='" + id + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", productKey='" + productKey + '\'' +
                ", iotId='" + iotId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", isEnd=" + isEnd +
                ", isDms=" + isDms +
                '}';
    }
}
