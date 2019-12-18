package com.nader.intelligent.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nader.intelligent.R;
import com.nader.intelligent.entity.Device;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author:zhangpeng
 * date: 2019/11/14
 */
public class AddRoomSelectEquipmentRevAdapter extends BaseQuickAdapter<DeviceInfo,AddRoomSelectEquipmentRevAdapter.ViewHolder> {
    private Map<String, DeviceInfo> equipmentBeanMap=new LinkedHashMap<>();

    public AddRoomSelectEquipmentRevAdapter(@Nullable List<DeviceInfo> data) {
        super(R.layout.item_add_device_list,data);
    }

    public Map<String, DeviceInfo> getEquipmentBeanMap() {
        return equipmentBeanMap;
    }

    public void setEquipmentBeanMap(Map<String, DeviceInfo> equipmentBeanMap) {
        this.equipmentBeanMap = equipmentBeanMap;
    }

    @Override
    protected void convert(ViewHolder helper, DeviceInfo item) {
//        if(!TextUtils.isEmpty(item.getCategoryImage())){
//            helper.add_room_select_equipment_sv.setImageURI(Uri.parse(item.getCategoryImage()));
//        }else {
//            helper.add_room_select_equipment_sv.setImageURI(Uri.parse(""));
//        }
        if(TextUtils.isEmpty(item.deviceName)){
            if(!TextUtils.isEmpty(item.deviceName)){
                helper.setText(R.id.add_room_select_equipment_namew_tv,item.deviceName);
            }else {
                helper.setText(R.id.add_room_select_equipment_namew_tv,"");
            }
        }else {
            helper.setText(R.id.add_room_select_equipment_namew_tv,item.deviceName);
        }
        if(item.productKey!=null){
            helper.setText(R.id.add_room_select_equipment_room_tv,item.productKey);
        }else {
            helper.setText(R.id.add_room_select_equipment_room_tv,"");

        }

        if (equipmentBeanMap.containsKey(item.id)){
            helper.add_room_select_equipment_check.setSelected(true);
        }else {
            helper.add_room_select_equipment_check.setSelected(false);
        }
        helper.addOnClickListener(R.id.add_room_select_equipment_check);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position);
            if (position-getHeaderLayoutCount()==(getData().size()-1)){
                holder.gonLin();
            }else {
                holder.shoLin();
            }
        }else {
            holder.upCheck(position-getHeaderLayoutCount());
        }

    }


    public class ViewHolder extends BaseViewHolder{
        public SimpleDraweeView add_room_select_equipment_sv;
        public ImageView add_room_select_equipment_check;
        public View add_room_select_equipment_lin;

        public ViewHolder(Context context, View view) {
            super(view);
            add_room_select_equipment_sv=view.findViewById(R.id.add_room_select_equipment_sv);
            add_room_select_equipment_check=view.findViewById(R.id.add_room_select_equipment_check);
            add_room_select_equipment_lin=view.findViewById(R.id.add_room_select_equipment_lin);
        }


        public void upCheck(int position){
            if (equipmentBeanMap.containsKey(getData().get(position).id)){
                add_room_select_equipment_check.setSelected(true);
            }else {
                add_room_select_equipment_check.setSelected(false);
            }
        }

        public void shoLin(){
            if(add_room_select_equipment_lin!=null)
                add_room_select_equipment_lin.setVisibility(View.VISIBLE);
        }

        public void gonLin(){
            if(add_room_select_equipment_lin!=null)
                add_room_select_equipment_lin.setVisibility(View.GONE);
        }
    }
}
