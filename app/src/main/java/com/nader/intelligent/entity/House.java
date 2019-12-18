package com.nader.intelligent.entity;

import java.io.Serializable;

/**
 * 房屋
 * author:zhangpeng
 * date: 2019/8/12
 */
public class House implements Serializable {
    private boolean hasDeviceChild;
    private boolean hasChild;
    private String id;
    private String name;
    private String typeName;
    private String description;
    private String parentId;
    private String typeCode;

    public boolean isHasDeviceChild() {
        return hasDeviceChild;
    }

    public void setHasDeviceChild(boolean hasDeviceChild) {
        this.hasDeviceChild = hasDeviceChild;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public String toString() {
        return "House{" +
                "hasDeviceChild=" + hasDeviceChild +
                ", hasChild=" + hasChild +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                ", parentId='" + parentId + '\'' +
                ", typeCode='" + typeCode + '\'' +
                '}';
    }
}
