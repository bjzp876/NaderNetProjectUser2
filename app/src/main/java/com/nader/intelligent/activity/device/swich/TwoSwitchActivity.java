package com.nader.intelligent.activity.device.swich;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.device.DeviceInfoActivity;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Attribute;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.RoomDevice;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 两位智能开关
 * author:zhangpeng
 * date: 2019/8/22
 */
public class TwoSwitchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_switch_one;
    private ImageView iv_switch_two;
    private PanelDevice panelDevice;
    private boolean oneState = false;
    private boolean twoState = false;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_right;

    private RoomDevice roomDevice;
    private String productKey;
    private String iotId;
    private String deviceName;
    private String name;
    private String token;
    private String nickName;
    private House room;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    setImageState();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_switch);
        mContext = this;
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_switch_one = findViewById(R.id.iv_switch_one);
        iv_switch_two = findViewById(R.id.iv_switch_two);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_right = findViewById(R.id.iv_right);

    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        iv_switch_one.setOnClickListener(this);
        iv_switch_two.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        roomDevice = (RoomDevice) getIntent().getSerializableExtra("device");
        name = roomDevice.getDeviceName();
        productKey = roomDevice.getProductKey();
        iotId = roomDevice.getIotId();
        deviceName = roomDevice.getDeviceName();
        tv_title.setText(name);
        token = SettingUtils.get(mContext,"token");
        nickName = roomDevice.getNickName();
        if (TextUtils.isEmpty(nickName)){
            tv_title.setText(name);
        }else{
            tv_title.setText(nickName);
        }
        panelDevice = new PanelDevice(iotId);
        panelDevice.init(mContext, new IPanelCallback() {
            @Override
            public void onComplete(boolean b, Object o) {
//                getDeviceInfoLong();
                panelDevice.subAllEvents(new IPanelEventCallback() {
                    @Override
                    public void onNotify(String iotid,String topic, Object data) {
                        if (iotid.equals(iotId)){
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(data));
                                jsonObject = jsonObject.getJSONObject("items");
                                JSONObject switchJsonObject;
                                int state;
                                if (jsonObject.has("PowerSwitch_1")){
                                    switchJsonObject = jsonObject.getJSONObject("PowerSwitch_1");
                                    state = switchJsonObject.getInt("value");
                                    if (state == 1) {
                                        oneState = true;
                                    } else {
                                        oneState = false;
                                    }
                                }
                                if (jsonObject.has("PowerSwitch_2")){
                                    switchJsonObject = jsonObject.getJSONObject("PowerSwitch_2");
                                    state = switchJsonObject.getInt("value");
                                    if (state == 1) {
                                        twoState = true;
                                    } else {
                                        twoState = false;
                                    }
                                }
                                mHandler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },null);
            }
        });
        List<Attribute> attributes = new ArrayList<>();
        attributes = roomDevice.getAttributeList();
        if (attributes!=null&&attributes.size()>0){
            for (int i=0;i<attributes.size();i++){
                if (attributes.get(i).getAttribute().equals("PowerSwitch_1")){
                    double value = (double) attributes.get(i).getValue();
                    if (value == 1.0){
                        oneState = true;
                    }else{
                        oneState = false;
                    }
                }
                if (attributes.get(i).getAttribute().equals("PowerSwitch_2")){
                    double value = (double) attributes.get(i).getValue();
                    if (value == 1.0){
                        twoState = true;
                    }else{
                        twoState = false;
                    }
                }
            }
        }
        mHandler.sendEmptyMessage(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_switch_one:
                if (oneState){
                    controlDevice(1,0);
                }else{
                    controlDevice(1,1);
                }

                break;
            case R.id.iv_switch_two:
                if (twoState){
                    controlDevice(2,0);
                }else{
                    controlDevice(2,1);
                }

                break;
            case R.id.iv_right:
                intent = new Intent(mContext, DeviceInfoActivity.class);
                intent.putExtra("device",roomDevice);
                startActivityForResult(intent, Constants.DELETE_DEVICE);
                break;
        }
    }

    /*
     * 设备控制
     * 0 关，1开
     * */
    private void controlDevice(final int index, final int value) {
        try {
            JSONObject paramsJsonObject = new JSONObject();
            paramsJsonObject.put("targetId", iotId);
            JSONObject itemsJSonObject = new JSONObject();
            if (index == 0) {
                itemsJSonObject.put("PowerSwitch", value);

            } else if (index == -1) {
                itemsJSonObject.put("CurtainOperation", value);
            } else {
                itemsJSonObject.put("PowerSwitch_" + index, value);
            }
            paramsJsonObject.put("properties", itemsJSonObject);
            apiConfig.controlDevice(token,paramsJsonObject.toString()).subscribe(new Action1<ResponseBody>() {
                @Override
                public void call(ResponseBody responseBody) {
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            int code = jsonObject.getInt("code");
                            if (code!=200){
                                String msg = jsonObject.getString("msg");
                                linkToast(mContext,msg);
                                return;
                            }
                            if (index == 1){
                                if (oneState){
                                    oneState = false;
                                }else{
                                    oneState = true;
                                }
                            }else{
                                if (twoState){
                                    twoState = false;
                                }else{
                                    twoState = true;
                                }
                            }
                            setImageState();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    linkToast(mContext,throwable.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取设备详情信息
     */
    private void getDeviceInfo(){
        apiConfig.getDeviceInfo(SettingUtils.get(mContext,"token"),iotId).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.d(TAG,result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        jsonObject = dataArray.getJSONObject(0);
                        JSONArray jsonArray = jsonObject.getJSONArray("attributes");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject attribute = jsonArray.getJSONObject(i);
                            int state;
                            if (attribute.getString("attribute").equals("PowerSwitch_1")){
                                    state = attribute.getInt("value");
                                    if (state == 0){
                                        oneState = false;
                                    }else{
                                        oneState = true;
                                    }
                            }
                            if (attribute.getString("attribute").equals("PowerSwitch_2")){
                                state = attribute.getInt("value");
                                if (state == 0){
                                    twoState = false;
                                }else{
                                    twoState = true;
                                }

                            }
                        }
                        mHandler.sendEmptyMessage(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                try {
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(),BaseResponse.class);
                    linkToast(mContext, (String) baseResponse.message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setImageState(){
        if (oneState){
            iv_switch_one.setImageResource(R.drawable.light_on);
        }else{
            iv_switch_one.setImageResource(R.drawable.light_off);
        }

        if (twoState){
            iv_switch_two.setImageResource(R.drawable.light_on);
        }else{
            iv_switch_two.setImageResource(R.drawable.light_off);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        panelDevice.uninit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case 1006:
                    if (resultCode == Constants.SUCCESS){
                        intent = new Intent();
                        intent.putExtra("success",data.getStringExtra("success"));
                        intent.putExtra("roomId",room.getId());
                        setResult(Constants.SUCCESS,intent);
                        finish();
                    }
                    break;
            }
        }
    }
}
