package com.nader.intelligent.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.iot.aep.component.router.Router;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.RoomDevice;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.view.MyListView;
import com.nader.intelligent.view.RoomTwoButtonDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import rx.functions.Action1;

/**
 * 设备信息详情
 * author:zhangpeng
 * date: 2019/11/21
 */
public class DeviceInfoActivity extends BaseActivity implements View.OnClickListener {
    //标题
    private TextView tv_title;
    private ImageView iv_back;
    //设备名称
    private TextView tv_name;
    private String deviceName;
    //设备ID
    private TextView tv_device_id;
    private String deviceID;
    //设备版本
    private TextView tv_device_swid;
    private String deviceVersion;
    //解绑设备
    private TextView tv_delete_device;
    //电机设置
    private LinearLayout ll_motro;
    //电机正转
    private TextView tv_forward;
    //电机反转
    private TextView tv_reversal;
    //电机百分比
    private SeekBar seekbar;
    private int num = 0;
    private TextView tv_progress_value;
    //取消边界
    private TextView tv_cancleBoundary;
    //设置边界
    private TextView tv_setBoundary;
    //设备实体bean
    private RoomDevice roomDevice;
    private PanelDevice panelDevice;
    private House checkHouse;
    private Handler mHandler = new Handler();
    private RoomTwoButtonDialog dialog;
    private CommonAdapter safeDataAdapter;
    private MyListView lv_devicesinfo;
    private TextView tv_center;
    private House room;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_device_info);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        roomDevice = (RoomDevice) getIntent().getSerializableExtra("device");
        if (roomDevice == null)
            return;

        tv_title = findViewById(R.id.tv_title);
        ll_motro = findViewById(R.id.ll_motro);
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.tv_name);
        tv_device_id = findViewById(R.id.tv_device_id);
        tv_device_swid = findViewById(R.id.tv_device_swid);
        tv_delete_device = findViewById(R.id.tv_delete_device);

        tv_forward = findViewById(R.id.tv_forward);
        tv_reversal = findViewById(R.id.tv_reversal);
        seekbar = findViewById(R.id.seekbar);
        tv_cancleBoundary = findViewById(R.id.tv_cancleBoundary);
        tv_setBoundary = findViewById(R.id.tv_setBoundary);
        tv_progress_value = findViewById(R.id.tv_progress_value);
        lv_devicesinfo = findViewById(R.id.lv_devicesinfo);
        tv_center = findViewById(R.id.tv_center);

        findViewById(R.id.rl_name_edit).setOnClickListener(this);



//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                num = seekbar.getProgress();
//                tv_progress_value.setText(num+"%");
//                if (num>65){
//                    controlDevice(-1,num);
//                }else{
//                    Toast.makeText(mContext, "电机边界请设置在65%以上", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        if (devicesBean.getProductKey().equals(NaderContants.ProductKey.CENTER_CONTROL)||devicesBean.getProductKey().equals(NaderContants.ProductKey.CENTER_SECOND_CONTROL)){
//            tv_center.setVisibility(View.VISIBLE);
//            safeDataAdapter = new CommonAdapter(mContext, safetyListBeans, R.layout.item_safe) {
//                @Override
//                protected void convertView(View item, Object o) {
//                    DeviceBean.DataBean safetyListBean = (DeviceBean.DataBean) o;
//                    //标题
//                    LinearLayout ll_safe_title = item.findViewById(R.id.ll_safe_title);
//                    TextView tv_safe_title = item.findViewById(R.id.tv_safe_title);
//                    //内容
//                    LinearLayout rl_safe_detail = item.findViewById(R.id.rl_safe_detail);
//                    //设备名
//                    TextView tv_safe_name = item.findViewById(R.id.tv_safe_name);
//                    //设备icon
//                    ImageView iv_safe_icon = item.findViewById(R.id.iv_safe_icon);
//                    //设备状态
//                    TextView tv_safe_state = item.findViewById(R.id.tv_safe_state);
//                    tv_safe_state.setVisibility(View.GONE);
//                    if (safetyListBean == null) {
////                    ll_safe_title.setVisibility(View.VISIBLE);
////                    rl_safe_detail.setVisibility(View.GONE);
//                        tv_safe_title.setText(safetyListBean.getNickName());
//                    } else {
////                    ll_safe_title.setVisibility(View.GONE);
////                    rl_safe_detail.setVisibility(View.VISIBLE);
//                        if (!TextUtils.isEmpty(safetyListBean.getNickName())) {
//                            tv_safe_name.setText(safetyListBean.getNickName());
//                        } else {
//                            tv_safe_name.setText(safetyListBean.getProductName());
//                        }
//                        Glide.with(mContext).load(safetyListBean.getDrawableResId()).into(iv_safe_icon);
////                    setState(safetyListBean, tv_safe_state);
//                    }
//                }
//            };
//            lv_devicesinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    DeviceBean.DataBean dataBean = (DeviceBean.DataBean) lv_devicesinfo.getItemAtPosition(position);
//                    if (dataBean == null)
//                        return;
//                    gson = new Gson();
//                    String devicesBean = gson.toJson(dataBean);
//                    SettingUtils.set(mContext, "devicesBean", devicesBean);
//                    switch (dataBean.getProductKey()) {
//                        case NaderContants.ProductKey.ONE_SWITCH:
//                            intent = new Intent(mContext, OneSwitchActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.TWO_SWITCH:
//                            intent = new Intent(mContext, TwoSwitchActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.THREE_SWITCH:
//                            intent = new Intent(mContext, ThreeSwitchActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.OUTLET:
//                            intent = new Intent(mContext, OutletActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.ELECTRIC_MACHINE:
//                            intent = new Intent(mContext, ElectircMachineActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.INFRARED_CURTAIN:
//                            intent = new Intent(mContext, SensorActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.MAGNETIC:
//                            intent = new Intent(mContext, MagneticActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        //红外幕帘
//                        case NaderContants.ProductKey.WATER_IMMERSION_SENSOR:
//                            intent = new Intent(mContext, SensorActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        //水浸传感器
//                        case NaderContants.ProductKey.HUMAN_BODY_SENSOR:
//                            //人体红外
//                            intent = new Intent(mContext, SensorActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.LOCK_FOR_XILEQI:
//                        case NaderContants.ProductKey.LOCK_FOR_HUITAILONG:
//                            //西勒奇门锁
//                            intent = new Intent(mContext, LockActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.SENSOR_PM:
////                    HashMap<String,Object> map = new HashMap<>();
////                    map.put("iotId",dataBean.getIotId());
////                    map.put("identifier","PM25");
////                    map.put("eventType","info");
////                    map.put("start", TimeUtil.getPastDateToLong(3));
////                    map.put("end",System.currentTimeMillis());
////                    map.put("pageSize",50);
////                    map.put("ordered",true);
////                    IotAPIClientManager.sendRequest(true,NaderContants.GET_TIMELINE,"1.0.2",map,this);
//                            intent = new Intent(mContext, PmSensorActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.TEMPERATURE_HUMIDITY:
//                            intent = new Intent(mContext, TempHumiActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.ONE_ZIGBEE_SWITCH:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.TWO_ZIGBEE_SWITCH:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.FOUR_ZIGBEE_SWITCH:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.SINGLE_CURTAIN_CONTROL_PANEL:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.DOUBLE_CURTAIN_CONTROL_PANEL:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//
//                        case NaderContants.ProductKey.DOORBELL:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        case NaderContants.ProductKey.EMERGENCY_BUTTON:
//                            intent = new Intent(mContext, DeviceInfoActivity.class);
//                            intent.putExtra(DEVICE, dataBean);
//                            startActivityForResult(intent, 1);
//                            break;
//                        scene_icon:
//                            Bundle bundle = new Bundle();
//                            bundle.putString("iotId", dataBean.getIotId());
//                            Router.getInstance().toUrl(mContext, "link://router/" + dataBean.getProductKey(), bundle);
//                            break;
//
//                    }
//                }
//            });
//            lv_devicesinfo.setAdapter(safeDataAdapter);
//            getDeviceListForGateway();
//        }

    }

    @Override
    public void initData() {
        super.initData();
        room = (House) getIntent().getSerializableExtra("room");
        if (TextUtils.isEmpty(roomDevice.getNickName())) {
            tv_name.setText(roomDevice.getProductName());
            tv_title.setText(roomDevice.getProductName());
        } else {
            tv_name.setText(roomDevice.getNickName());
            tv_title.setText(roomDevice.getNickName());
        }
        tv_device_id.setText(roomDevice.getDeviceName());
        if (roomDevice.getProductKey().equals(Constants.ELECTRIC_MACHINE)){
            int position = getIntent().getIntExtra("position",0);
            seekbar.setProgress( position );
            tv_progress_value.setText( position +"%");
            ll_motro.setVisibility(View.VISIBLE);
        }
        seekbar.setMax(100);
        dialog = new RoomTwoButtonDialog(this);
        dialog.setMessage("你确定要解绑该设备吗?");
    }

    @Override
    public void initListener() {
        super.initListener();
        tv_delete_device.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_forward.setOnClickListener(this);
        tv_reversal.setOnClickListener(this);
        tv_cancleBoundary.setOnClickListener(this);
        tv_setBoundary.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_delete_device:

                break;
        }
    }

    /**
     * 删除设备
     */
    private void delDevice(){
        dialog.show();
        dialog.setRoomTwoButtonDialogDetermineClick(new RoomTwoButtonDialog.RoomTwoButtonDialoClick() {
            @Override
            public void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog) {
                dialog.dismiss();
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("iotId", roomDevice.getIotId());
                        apiConfig.resetDevice(SettingUtils.get(mContext,"token"),params).subscribe(new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody responseBody) {
                                intent = new Intent();
                                intent.putExtra("success","success");
                                intent.putExtra("roomId",room.getId());
                                setResult(Constants.SUCCESS,intent);
                                finish();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                linkToast(mContext,"删除失败");
                            }
                        });
            }
        });
    }
}
