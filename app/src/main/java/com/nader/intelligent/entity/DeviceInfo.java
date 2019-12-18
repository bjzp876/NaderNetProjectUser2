package com.nader.intelligent.entity;

import java.io.Serializable;

/**
 * 设备属性
 * author:zhangpeng
 * date: 2019/11/26
 */
public class DeviceInfo implements Serializable {

    /**
     * categoryType : WallSwitch
     * identifier : PowerSwitch_1
     * rwType : READ_WRITE
     * abilityType : PROPERTY
     * isStd : true
     * name : 电源开关_1
     * abilityId : 91613
     * required : true
     */

    private String categoryType;
    private String identifier;
    private String rwType;
    private String abilityType;
    private boolean isStd;
    private String name;
    private int abilityId;
    private boolean required;
    private int compareValue = -1;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRwType() {
        return rwType;
    }

    public void setRwType(String rwType) {
        this.rwType = rwType;
    }

    public String getAbilityType() {
        return abilityType;
    }

    public void setAbilityType(String abilityType) {
        this.abilityType = abilityType;
    }

    public boolean isIsStd() {
        return isStd;
    }

    public void setIsStd(boolean isStd) {
        this.isStd = isStd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(int abilityId) {
        this.abilityId = abilityId;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isStd() {
        return isStd;
    }

    public void setStd(boolean std) {
        isStd = std;
    }

    public int getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(int compareValue) {
        this.compareValue = compareValue;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "categoryType='" + categoryType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", rwType='" + rwType + '\'' +
                ", abilityType='" + abilityType + '\'' +
                ", isStd=" + isStd +
                ", name='" + name + '\'' +
                ", abilityId=" + abilityId +
                ", required=" + required +
                ", compareValue=" + compareValue +
                '}';
    }
}
