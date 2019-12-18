package com.nader.intelligent.entity;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Item {

    private final String name;
    private final int id;
    private boolean isEnd = false;

    public Item(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
