package com.nader.intelligent.entity.vo;

import java.io.Serializable;

/**
 * 项目详情信息
 * author:zhangpeng
 * date: 2019/11/13
 */
public class ProjectVo implements Serializable {
    private String id;
    private boolean hasDeviceChild;
    private boolean hasChild;
    private String name;
    private String typeName;
    private String description;
    private String typeCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
