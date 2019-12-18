package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.RoomDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果选择
 * author:zhangpeng
 * date: 2019/12/10
 */
public class SceneActionSelectActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;

    private ListView lv_scene_list;
    private CommonAdapter commonAdapter;
    private List<String> list = new ArrayList<>();
    private String data;
    private int state = 0;
    private int stateNum = 0;
    private List<RoomDevice> devices = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    commonAdapter.notifyDataSetChanged(devices);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scene_list);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        lv_scene_list = findViewById(R.id.lv_scene_list);

        commonAdapter = new CommonAdapter(mContext,devices,R.layout.item_scene_device) {
            @Override
            protected void convertView(View item, Object o) {
                RoomDevice roomDevice = (RoomDevice) o;
                TextView textView = item.findViewById(R.id.tv_scene_device);
                textView.setText(roomDevice.getNickName());
            }
        };
        lv_scene_list.setAdapter(commonAdapter);

        lv_scene_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    intent = new Intent(mContext,SceneListActivity.class);
                    intent.putExtra("stateNum",getIntent().getIntExtra("stateNum",1));
                    startActivity(intent);
                }else{
                    linkToast(mContext,"该功能暂未开放");
                }

            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        state = getIntent().getIntExtra("state",0);
        stateNum = getIntent().getIntExtra("stateNum",1);
        tv_title.setText("执行任务");

        RoomDevice roomDevice = new RoomDevice();
        roomDevice.setNickName("设备动作");
        RoomDevice roomDevice1 = new RoomDevice();
        roomDevice1.setNickName("执行场景");
        RoomDevice roomDevice2 = new RoomDevice();
        roomDevice2.setNickName("向手机发送通知");
        devices.add(roomDevice);
        devices.add(roomDevice1);
        devices.add(roomDevice2);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
