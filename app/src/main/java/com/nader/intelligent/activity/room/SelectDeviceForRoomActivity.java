package com.nader.intelligent.activity.room;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.iot.homelink.si.component.TopBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.AddRoomSelectEquipmentRevAdapter;
import com.nader.intelligent.adapter.BaseRecyclerAdapter;
import com.nader.intelligent.adapter.BaseViewHolder;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Room;
import com.nader.intelligent.util.RecyclerViewSpacesItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择房间下属设备
 * author:zhangpeng
 * date: 2019/11/14
 */
public class SelectDeviceForRoomActivity extends BaseActivity implements View.OnClickListener{
    private TopBar add_room_select_equipment_topbar;
    private TextView add_room_select_equipment_count_tv;
    private BaseRecyclerAdapter adapter;
    private RecyclerView add_room_select_equipment_rev;
    private SwipeRefreshLayout add_room_select_equipment_sv;
    private String roomName;
    private List<Room.DevicesBean> deviceInfos = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_devices);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        add_room_select_equipment_rev=findViewById(R.id.add_room_select_equipment_rev);
        add_room_select_equipment_sv=findViewById(R.id.add_room_select_equipment_sv);
        add_room_select_equipment_topbar=findViewById(R.id.add_room_select_equipment_topbar);
        add_room_select_equipment_count_tv=findViewById(R.id.add_room_select_equipment_count_tv);
        add_room_select_equipment_topbar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        adapter = new BaseRecyclerAdapter<Room.DevicesBean>(mContext,R.layout.item_add_device_list,deviceInfos){

            @Override
            public void convert(BaseViewHolder holder, final Room.DevicesBean deviceInfo) {
                        holder.setText(R.id.add_room_select_equipment_namew_tv,deviceInfo.getDeviceName());
                        if (deviceInfo.isDms()){
                            holder.setImageResource(R.id.add_room_select_equipment_check,R.drawable.page_index_check_ico);
                        }else{
                            holder.setImageResource(R.id.add_room_select_equipment_check,R.drawable.page_index_un_check_ico);
                        }
                        holder.setOnClickListener(R.id.rl_device_list, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int checkNum = -1;
                                for (int i=0;i<deviceInfos.size();i++){
                                    if (deviceInfos.get(i).getId().equals(deviceInfo.getId())){
                                        checkNum = i;
                                        break;
                                    }
                                }
                                if (checkNum!=-1){
                                    if (deviceInfo.isDms()){
                                        deviceInfos.get(checkNum).setDms(false);
                                    }else{
                                        deviceInfos.get(checkNum).setDms(true);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });


            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        add_room_select_equipment_rev.setLayoutManager(manager);
        add_room_select_equipment_rev.setAdapter(adapter);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(20);
        add_room_select_equipment_rev.addItemDecoration(recyclerViewSpacesItemDecoration);
    }

    @Override
    public void initListener() {
        super.initListener();
        add_room_select_equipment_topbar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        roomName = getIntent().getStringExtra("roomName");
        add_room_select_equipment_sv.setColorSchemeColors(ContextCompat.getColor(mContext,R.color.page_index_main_color));
        for (int i=0;i<5;i++){
            Room.DevicesBean devicesBean = new Room.DevicesBean();
            devicesBean.setId(String.valueOf(i));
            devicesBean.setDeviceName("一位智能开关");
            devicesBean.setName("客厅");
            deviceInfos.add(devicesBean);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private void countUp(int count){
        add_room_select_equipment_count_tv.setText(String.format(mContext.getString(R.string.page_index_add_room_select_equipment_lable_1),count));
    }
}
