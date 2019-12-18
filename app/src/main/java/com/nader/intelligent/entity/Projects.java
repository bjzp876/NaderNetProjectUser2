package com.nader.intelligent.entity;

import com.nader.intelligent.entity.vo.ProjectVo;

import java.io.Serializable;
import java.util.List;

/**
 * 项目列表
 * author:zhangpeng
 * date: 2019/11/12
 */
public class Projects<T> implements Serializable {
    //条目总数
    private int total = 0;
    //当前页数
    private int pageNo = 1;
    //分页个数
    private int pageSize = 100;
    //项目列表数据
    private List<T> data;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Projects{" +
                "total=" + total +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", data=" + data +
                '}';
    }
}
