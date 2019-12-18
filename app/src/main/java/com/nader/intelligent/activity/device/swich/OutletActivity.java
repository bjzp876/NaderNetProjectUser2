package com.nader.intelligent.activity.device.swich;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.nader.intelligent.R;
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
 * author:zhangpeng
 * date: 2019/11/21
 */
public class OutletActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;

    private ImageView iv_outlet;
    private RelativeLayout rl_power;

    private PanelDevice panelDevice;
    private boolean oneState = false;
    private String productKey;
    private String iotId;
    private String deviceName;
    private String name;
    private String token;
    private String nickName;
    private RoomDevice roomDevice;
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
        mContext = this;
        setContentView(R.layout.activity_outlet);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_outlet = findViewById(R.id.iv_outlet);
        rl_power = findViewById(R.id.rl_power);
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        rl_power.setOnClickListener(this);
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
        panelDevice = new PanelDevice(roomDevice.getIotId());
        panelDevice.init(mContext, new IPanelCallback() {
            @Override
            public void onComplete(boolean b, Object o) {

            }
        });

        panelDevice.subAllEvents(new IPanelEventCallback() {
            @Override
            public void onNotify(String iotid, String topic, Object data) {
                Log.d(TAG,String.valueOf(data));
                if (iotid.equals(iotId)){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(data));
                        jsonObject = jsonObject.getJSONObject("items");
                        JSONObject switchJsonObject;
                        int state;
                        if (jsonObject.has("PowerSwitch")){
                            switchJsonObject = jsonObject.getJSONObject("PowerSwitch");
                            state = switchJsonObject.getInt("value");
                            if (state == 1) {
                                oneState = true;
                            } else {
                                oneState = false;
                            }
                        }
                        mHandler.sendEmptyMessage(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        },null);
        List<Attribute> attributes = new ArrayList<>();
        attributes = roomDevice.getAttributeList();
        if (attributes!=null&&attributes.size()>0){
            for (int i=0;i<attributes.size();i++){
                if (attributes.get(i).getAttribute().equals("PowerSwitch")){
                    double value = (double) attributes.get(i).getValue();
                    if (value == 1.0){
                        oneState = true;
                    }else{
                        oneState = false;
                    }
                }
            }
        }
        mHandler.sendEmptyMessage(0);
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
                            if (attribute.getString("attribute").equals("PowerSwitch")){
                                state = attribute.getInt("value");
                                if (state == 0){
                                    oneState = false;
                                }else{
                                    oneState = true;
                                }
                            }
                            break;
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
            iv_outlet.setImageResource(R.drawable.plug_on);
        }else{
            iv_outlet.setImageResource(R.drawable.plug_off);
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
            apiConfig.controlDevice(token, paramsJsonObject.toString()).subscribe(new Action1<ResponseBody>() {
                @Override
                public void call(ResponseBody responseBody) {
                    try {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            int code = jsonObject.getInt("code");
                            if (code != 200) {
                                String msg = jsonObject.getString("msg");
                                linkToast(mContext, msg);
                                return;
                            }
                            if (oneState) {
                                oneState = false;
                            } else {
                                oneState = true;
                            }
                            mHandler.sendEmptyMessage(0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    linkToast(mContext, throwable.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        panelDevice.uninit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_power:
                if (oneState){
                    controlDevice(0,0);
                }else{
                    controlDevice(0,1);
                }
                break;
        }
    }
}
