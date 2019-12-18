package com.nader.intelligent.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 设备详情
 * author:zhangpeng
 * date: 2019/12/10
 */
public class SceneDetail implements Serializable {
    private boolean enable;
    private String icon;
    private String name;
    private String sceneId;
    private String description;
    private List<Triggers> triggers;
    private List<Triggers> conditions;
    private List<Triggers> actions;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Triggers> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Triggers> triggers) {
        this.triggers = triggers;
    }

    public List<Triggers> getConditions() {
        return conditions;
    }

    public void setConditions(List<Triggers> conditions) {
        this.conditions = conditions;
    }

    public List<Triggers> getActions() {
        return actions;
    }

    public void setActions(List<Triggers> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "SceneDetail{" +
                "enable=" + enable +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", description='" + description + '\'' +
                ", triggers=" + triggers +
                ", conditions=" + conditions +
                ", actions=" + actions +
                '}';
    }
}
