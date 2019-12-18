package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Projects;
import com.nader.intelligent.entity.RoomDevice;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 场景属性列表
 * author:zhangpeng
 * date: 2019/9/12
 */
public class SceneListActivity extends BaseActivity implements View.OnClickListener {
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
                    intent = new Intent(mContext,SceneDeviceInfoList.class);
                    intent.putExtra("nickName",devices.get(position).getNickName());
                    intent.putExtra("title",devices.get(position).getProductName());
                    intent.putExtra("productKey",devices.get(position).getProductKey());
                    intent.putExtra("state",state+1);
                    intent.putExtra("iotId",devices.get(position).getIotId());
//                    List<String> datas = new ArrayList<>();
//                    datas.add("一位开关_1");
//                    datas.add("一位开关_2");
//                    intent.putExtra("data",gson.toJson(datas));
//                }else{
//                    intent = new Intent(mContext,SceneDeviceStateActivity.class);
//                    intent.putExtra("nickName",devices.get(position));
//                    intent.putExtra("title",devices.get(position));
//                }
                    intent.putExtra("stateNum",getIntent().getIntExtra("stateNum",1));
                startActivity(intent);
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
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)){
            tv_title.setText("选择一个设备");
        }else{
            tv_title.setText(title);
        }
//        data = getIntent().getStringExtra("data");
//        if (TextUtils.isEmpty(data)){
//            list.add("一位智能开关");
//            list.add("二位智能开关");
//            list.add("三位智能开关");
//        }else{
//            list = gson.fromJson(data,new TypeToken<List<String>>(){}.getType());
//        }
//        commonAdapter.notifyDataSetChanged(list);
            getDeviceData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取设备列表
     */
    private void getDeviceData(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map = new HashMap<>();
                map.put("targetId","57b917c100f911ea915600163e0406b6");
                map.put("flowType",stateNum);
                apiConfig.getSceneDeviceList(SettingUtils.get(mContext,"token"),map).subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        devices.clear();
                        devices = gson.fromJson(gson.toJson(baseResponse.data), new TypeToken<List<RoomDevice>>(){}.getType());
                        if (devices!=null){
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((HttpException)throwable).response().errorBody();
                        try {
                            linkToast(mContext,responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();

    }


}
