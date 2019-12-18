package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.DeviceInfo;
import com.nader.intelligent.entity.Week;
import com.nader.intelligent.util.JSONUtil;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 设备状态选择
 * author:zhangpeng
 * date: 2019/9/17
 */
public class SceneDeviceStateActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right;
    private FrameLayout fl_right;

    private ListView lv_scene_list;
    private CommonAdapter commonAdapter;
    private List<Week> weeks = new ArrayList<>();
    private String nickName;
    private String title;
    private String iotId;
    private DeviceInfo deviceInfo;
    private String deviceName;
    private String productKey;
    private int position = -1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    commonAdapter.notifyDataSetChanged(weeks);
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
        tv_right = findViewById(R.id.tv_right);
        fl_right = findViewById(R.id.fl_right);
        lv_scene_list = findViewById(R.id.lv_scene_list);

        commonAdapter = new CommonAdapter(mContext, weeks, R.layout.item_scene_week) {
            @Override
            protected void convertView(View item, Object o) {
                TextView date = item.findViewById(R.id.tv_secene_week);
                CheckBox cb = item.findViewById(R.id.cb_scene_week);
                Week week = (Week) o;
                date.setText(week.getDate());
                cb.setChecked(week.isFlag());
            }
        };
        lv_scene_list.setAdapter(commonAdapter);
        lv_scene_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Week week = weeks.get(position);
                boolean isCheck = false;
                if (week.isFlag()) {
                    isCheck = false;
                } else {
                    isCheck = true;
                }
                for (int i = 0; i < weeks.size(); i++) {
                    if (i == position) {
                        weeks.get(i).setFlag(true);
                    } else {
                        weeks.get(i).setFlag(false);
                    }
                }
                commonAdapter.notifyDataSetChanged(weeks);
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        deviceName = getIntent().getStringExtra("deviceName");
        deviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");
        productKey = getIntent().getStringExtra("productKey");
        position = getIntent().getIntExtra("position",-1);
        if (deviceInfo == null) {
            finish();
            return;
        }
        iotId = getIntent().getStringExtra("iotId");
        title = getIntent().getStringExtra("title");
        nickName = getIntent().getStringExtra("nickName");
        tv_right.setText("保存");
        tv_right.setTextColor(getResources().getColor(R.color.text_green));
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText(deviceInfo.getName());
        fl_right.setVisibility(View.VISIBLE);
        commonAdapter.notifyDataSetChanged(weeks);
        getDeviceState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if (getCheckItem() == null) {
                    linkToast(mContext, "保存失败");
                    return;
                }
                linkToast(mContext, "保存成功");
                String data = deviceInfo.getName() + getCheckItem().getDate();

//                Intent broadcastReciver = new Intent("if_device");
//                broadcastReciver.putExtra("data",data);
//                broadcastReciver.putExtra("nickName",nickName);
//                broadcastReciver.setAction("if_device");
//                sendBroadcast(broadcastReciver);
                intent = new Intent(mContext, SceneDetailActivity.class);
                intent.putExtra("data", data);
                intent.putExtra("nickName", deviceName);
                intent.putExtra("stateNum", getIntent().getIntExtra("stateNum", 1));
                intent.putExtra("iotId", iotId);
                intent.putExtra("week", gson.toJson(getCheckItem()));
                intent.putExtra("productKey", productKey);
                intent.putExtra("identifier", deviceInfo.getIdentifier());
                intent.putExtra("position",position);
                intent.putExtra("deviceInfo",deviceInfo);
                startActivity(intent);
                finish();
                break;
        }
    }


    /**
     * 获取设备属性状态
     */
    private void getDeviceState() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<>();
                map.put("iotId", iotId);
                apiConfig.getDeviceStateByScene(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        JSONObject result = null;
                        try {
                            result = JSONUtil.getSceneState(responseBody.string(), deviceInfo.getIdentifier());
                            if (result != null) {
                                weeks.clear();
                                Iterator it = result.keys();
                                while (it.hasNext()) {
                                    String key = String.valueOf(it.next().toString());
                                    String value = (String) result.get(key).toString();
                                    Week week = new Week();
                                    week.setDate(value);
                                    week.setKey(key);
                                    week.setIotId(iotId);
                                    week.setIdentifier(deviceInfo.getIdentifier());
                                    weeks.add(week);
                                }
                                int num = 0;
                                if (deviceInfo.getCompareValue() != -1){
                                    num = deviceInfo.getCompareValue();
                                }
                                weeks.get(num).setFlag(true);
                                mHandler.sendEmptyMessage(0);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                            linkToast(mContext, (String) baseResponse.message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();

    }

    private Week getCheckItem() {
        Week week = null;
        for (int i = 0; i < weeks.size(); i++) {
            if (weeks.get(i).isFlag()) {
                week = weeks.get(i);
                break;
            }
        }
        return week;
    }


}
