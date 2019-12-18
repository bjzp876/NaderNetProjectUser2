package com.nader.intelligent.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:create by 6648 on 2018/9/3 14:21
 * 邮箱:6648@shuncom.com
 * 描述:所有的Adapter都需要继承此Adapter
 */
public class MyBaseAdapter extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater layoutInflater;

    protected List<Object> dataList = new ArrayList<>();

    protected Object houseBean;

    public MyBaseAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public MyBaseAdapter() {
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setDataList(List<? extends Object> dataList) {
        if (dataList!=null){
            this.dataList.clear();
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }
    public void setDataList(List<? extends Object> dataList,List<? extends Object> houseList) {
        if (dataList!=null){
            this.dataList.clear();
        }
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setDataList(List<? extends Object> dataList,Object houseBean) {
        if (dataList!=null){
            this.dataList.clear();
        }
        this.dataList.addAll(dataList);
        this.houseBean = houseBean;
        notifyDataSetChanged();
    }
    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }
}
