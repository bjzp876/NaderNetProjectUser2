package com.nader.intelligent.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.iot.aep.component.router.Router;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.BaseRecyclerAdapter;
import com.nader.intelligent.adapter.BaseViewHolder;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Device;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.HouseNew;
import com.nader.intelligent.entity.Room;
import com.nader.intelligent.util.IotAPIClientManager;
import com.nader.intelligent.util.OnBindDeviceCompletedListener;
import com.nader.intelligent.util.RecyclerViewSpacesItemDecoration;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 选择设备列表
 * author:zhangpeng
 * date: 2019/11/18
 */
public class ADDDeviceListActivity extends BaseActivity implements View.OnClickListener , OnBindDeviceCompletedListener {
    private TextView tv_title;
    private FrameLayout rl_back;
    private Button bt_network;

    private SwipeRefreshLayout refresh;
    //配网设备列表
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter recyclerAdapter;
    private List<Device> deviceOthers = new ArrayList<>();
    private HouseNew housesBean;
    //中控列表信息
    private List<Device> devices = new ArrayList<>();
    private RecyclerView lv_device;
    private BaseRecyclerAdapter adapter;
    //是否提交组网
    private boolean isClick = false;
    //是否是中控
    private boolean isCenter = false;

    private String token;
    private String productKey;
    private String iotId;
    private String deviceName;
    private String name;
    private String deviceId;
    private int position = 0;
    private House house;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.ERROR:
                    String message = (String) msg.obj;
                    linkToast(mContext,message);
                    break;
                case Constants.SUCCESS:
//                    boolean flag = sectionedExpandableLayoutHelper.checkItemState(position);
//                        isClick = flag;
//                        if (flag){
//                            bt_network.setBackground(mContext.getDrawable(R.drawable.bg_login));
//                        }else{
//                            bt_network.setBackground(mContext.getDrawable(R.drawable.bg_net_send));
//                        }
//                        bt_network.setClickable(flag);
//                    DeviceVo deviceVo = new DeviceVo();
//                    deviceVo.setIotId(iotId);
//                    deviceVo.setProductKey(productKey);
//                    deviceVo.setName(name);
//                    deviceVo.setDeviceName(deviceName);
//                    DeviceDaoManager.getInstance().insertData(deviceVo);
//                    if (!productKey.equals(Constants.CENTER_CONTROL)){
//                        boolean flag = sectionedExpandableLayoutHelper.checkItemState(position);
//                        isClick = flag;
//                        if (flag){
//                            bt_network.setBackground(mContext.getDrawable(R.drawable.bg_login));
//                        }else{
//                            bt_network.setBackground(mContext.getDrawable(R.drawable.bg_net_send));
//                        }
//                    }
//                    SwitchActivity.switchDevice(intent,mContext,productKey,deviceName,iotId,name);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_device_list);
        mContext = this;
        initView();
        initData();
    }


    public void initView(){
        token = SettingUtils.get(mContext,"token");
        refresh = findViewById(R.id.refresh);
        tv_title = findViewById(R.id.tv_title);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        housesBean = (HouseNew) getIntent().getSerializableExtra("houseBean");
        recyclerAdapter = new BaseRecyclerAdapter<Device>(mContext,R.layout.item_add_device,deviceOthers) {
            @Override
            public void convert(BaseViewHolder holder, final Device device) {
                holder.setText(R.id.deviceadd_category_list_category_title_tv,device.getDeviceInfo().deviceName);
                holder.setOnClickListener(R.id.fm_add_device, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
//                bundle.putString("groupId", housesBean.getId());
                        bundle.putString("productKey", device.getDeviceInfo().productKey);
                        bundle.putString("deviceName", null);
                        Router.getInstance().toUrlForResult(ADDDeviceListActivity.this,
                                Constants.PLUGIN_ID_DEVICE_CONFIG,
                                Constants.REQUEST_CODE_CONFIG_WIFI, bundle);
                    }
                });
            }
        };
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refresh.setRefreshing(true);
            }
        });
        if (housesBean!=null){
            tv_title.setText(housesBean.getName());
        }else{
//            finish();
//            return;
        }
        lv_device = findViewById(R.id.lv_device);
        adapter = new BaseRecyclerAdapter<Device>(mContext,R.layout.item_add_center,devices) {
            @Override
            public void convert(BaseViewHolder holder, final Device device) {
                    holder.setText(R.id.deviceadd_category_list_local_device_name_tv,device.getDeviceInfo().deviceName);
                    holder.setOnClickListener(R.id.deviceadd_category_list_local_device_add_fl, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = device.getDeviceInfo().regDeviceName;
                            Bundle bundle = new Bundle();
                            bundle.putString("groupId", house.getId());
                            bundle.putString("productKey", device.getDeviceInfo().productKey);
                            bundle.putString("deviceName", device.getDeviceInfo().deviceName);
                            bundle.putString("token", device.getDeviceInfo().token);
                            bundle.putString("addDeviceFrom", "null");
                            Router.getInstance().toUrlForResult(ADDDeviceListActivity.this,
                                    Constants.PLUGIN_ID_DEVICE_CONFIG,
                                    Constants.REQUEST_CODE_CONFIG_WIFI, bundle);
                        }
                    });
            }
        };
        LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
        lv_device.setLayoutManager(manager1);
        lv_device.setAdapter(adapter);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration1 = new RecyclerViewSpacesItemDecoration(1);
        lv_device.addItemDecoration(recyclerViewSpacesItemDecoration1);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(recyclerAdapter);
        RecyclerViewSpacesItemDecoration recyclerViewSpacesItemDecoration = new RecyclerViewSpacesItemDecoration(1);
        mRecyclerView.addItemDecoration(recyclerViewSpacesItemDecoration);
    }
    public void initData(){
        house = (House) getIntent().getSerializableExtra("room");
        EnumSet enumSet = EnumSet.noneOf(DiscoveryType.class);
        enumSet.add(DiscoveryType.LOCAL_ONLINE_DEVICE);
        enumSet.add(DiscoveryType.CLOUD_ENROLLEE_DEVICE);
        LocalDeviceMgr.getInstance().startDiscovery(mContext, enumSet, null, new IDeviceDiscoveryListener() {
            @Override
            public void onDeviceFound(DiscoveryType discoveryType, List<DeviceInfo> list) {
                devices.clear();
                // 发现的设备
                // LOCAL_ONLINE_DEVICE 当前和手机在同一局域网已配网在线的设备
                // CLOUD_ENROLLEE_DEVICE 零配或智能路由器发现的待配设备
                // BLE_ENROLLEE_DEVICE 发现的是蓝牙设备，需要根据设备的productId查询设备是否是wifi+蓝牙双模设备
                // SOFT_AP_DEVICE 发现的设备热点
                // 注意：发现蓝牙设备需添加 breeze SDK依赖
                if (discoveryType.equals(DiscoveryType.LOCAL_ONLINE_DEVICE)){
                    if (list.size()>0){
                        for (int i=0;i<list.size();i++){
                            Device device = new Device();
                            device.setDeviceInfo(list.get(i));
                            device.setCms(true);
                            devices.add(device);
                        }
                    }
                }
                else if(discoveryType.equals(DiscoveryType.CLOUD_ENROLLEE_DEVICE)){
                    if (list.size()>0){
                        for (int i=0;i<list.size();i++){
                            Device device = new Device();
                            device.setDeviceInfo(list.get(i));
                            device.setCms(false);
                            devices.add(device);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        getDeviceListData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.REQUEST_CODE_CONFIG_WIFI:
                if (data!=null){
                    if (resultCode==RESULT_OK){
                        productKey = data.getStringExtra("productKey");
                        if (!TextUtils.isEmpty(productKey)){
                            if (productKey.equals(Constants.CENTER_CONTROL)){
                                isCenter = true;
                            }else{
                                isCenter = false;
                            }
                            deviceName = data.getStringExtra("deviceName");
                            token = data.getStringExtra("token");
                            IotAPIClientManager.bindDevices(productKey,deviceName,token,this );
                        }
                    }
                }
                break;
        }
    }

    private void getDeviceListData(){
        refresh.setRefreshing(false);
        deviceOthers.clear();
        DeviceInfo device1 = new DeviceInfo();
        device1.productKey = Constants.ONE_SWITCH;
        device1.deviceName = "一键智能开关";
        device1.id = "1";
        DeviceInfo device2 = new DeviceInfo();
        device2.productKey = Constants.TWO_SWITCH;
        device2.deviceName = "二键智能开关";
        device2.id = "2";
        DeviceInfo device3 = new DeviceInfo();
        device3.productKey = Constants.THREE_SWITCH;
        device3.deviceName = "三键智能开关";
        device3.id = "3";
        DeviceInfo device4 = new DeviceInfo();
        device4.productKey = Constants.OUTLET;
        device4.deviceName = "五孔智能插座";
        device4.id = "4";
        Device device11 = new Device();
        device11.setDeviceInfo(device1);
        Device device22 = new Device();
        device22.setDeviceInfo(device2);
        Device device33 = new Device();
        device33.setDeviceInfo(device3);
        Device device44 = new Device();
        device44.setDeviceInfo(device4);
        deviceOthers.add(device11);
        deviceOthers.add(device22);
        deviceOthers.add(device33);
        deviceOthers.add(device44);
        recyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccess(String iotId) {
        if (house!=null){
            Map<String,Object> map = new HashMap<>();
            String[] iotids = new String[1];
            iotids[0] = iotId;
            map.put("projectId",SettingUtils.get(mContext,"projectId"));
            map.put("roomId",house.getId());
            map.put("iotIdList",iotids);
            apiConfig.setDevicetoRoom(SettingUtils.get(mContext,"token"),map).subscribe(new Action1<ResponseBody>() {
                @Override
                public void call(ResponseBody responseBody) {
                    linkToast(mContext,"添加成功");
                    intent = new Intent();
                    intent.putExtra("success","success");
                    intent.putExtra("roomId",house.getId());
                    setResult(Constants.SUCCESS,intent);
                    finish();
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    ResponseBody responseBody = ((HttpException)throwable).response().errorBody();
                    try {
                        BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                        linkToast(mContext, (String) baseResponse.message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{

        }

    }

    @Override
    public void onFailed(Exception e) {
        e.printStackTrace();
        linkToast(mContext,"添加失败");
        intent = new Intent();
        intent.putExtra("success","success");
        setResult(Constants.ERROR,intent);
        finish();
    }

    @Override
    public void onFailed(int code, String message, String localizedMsg) {
        linkToast(mContext,localizedMsg);
        intent = new Intent();
        intent.putExtra("success","success");
        setResult(Constants.ERROR,intent);
        finish();
    }
}
