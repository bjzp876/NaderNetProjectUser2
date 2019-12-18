package com.nader.intelligent.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.device.ADDDeviceListActivity;
import com.nader.intelligent.activity.device.swich.OutletActivity;
import com.nader.intelligent.activity.device.swich.TwoSwitchActivity;
import com.nader.intelligent.activity.main.MainActivity;
import com.nader.intelligent.activity.room.RoomSettingActivity;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseFragment;
import com.nader.intelligent.entity.Attribute;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Project;
import com.nader.intelligent.entity.Projects;
import com.nader.intelligent.entity.RoomDevice;
import com.nader.intelligent.entity.Rooms;
import com.nader.intelligent.entity.vo.ProjectVo;
import com.nader.intelligent.util.ImageUtil;
import com.nader.intelligent.util.IndexPopWindowUtlis;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.view.MyGridView;
import com.nader.intelligent.view.ProgrammeDialog;
import com.nader.intelligent.view.VpSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 房间详情
 * author:zhangpeng
 * date: 2019/11/8
 */
public class SingleRoomFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View view;
    private MyGridView gv_devices;
    private CommonAdapter adapter;
    private LinearLayout ll_content;
    private LinearLayout ll_room_parent;
    private ImageView iv_single_bg;
    private TextView tv_pm25_value;
    private TextView tv_home2;
    private LinearLayout ll_pm;
    private TextView tv_temperature;
    private TextView tv_current_humidity;
    private LinearLayout ll_room_tem;
    //编辑
    private TextView tv_edit;
    //下拉刷新
    private VpSwipeRefreshLayout refreshLayout;
    private House room;
    private House house;
    //弹框
    private PopupWindow more_pop;
    private List<RoomDevice> projectVoList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        gson = new Gson();
        view = inflater.inflate(R.layout.fragment_single_room, container, false);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (getArguments() != null) {
            room = (House) getArguments().getSerializable("data");
        }
    }


    //传递数据
    public static SingleRoomFragment newInstance(House house) {

        SingleRoomFragment fragment = new SingleRoomFragment();

        Bundle args = new Bundle();
        args.putSerializable("data", house);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        ll_content = view.findViewById(R.id.ll_content);
        ll_room_parent = view.findViewById(R.id.ll_room_parent);
        iv_single_bg = view.findViewById(R.id.iv_single_bg);
        gv_devices = view.findViewById(R.id.gv_devices);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_current_humidity = view.findViewById(R.id.tv_current_humidity);
        ll_room_tem = view.findViewById(R.id.ll_room_tem);
        tv_pm25_value = view.findViewById(R.id.tv_pm25_value);
        ll_pm = view.findViewById(R.id.ll_pm);
        tv_home2 = view.findViewById(R.id.tv_home2);
        tv_edit = view.findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
//        commonDevicesAdapter = new CommonDevicesAdapter(mContext);
//        gv_devices.setAdapter(commonDevicesAdapter);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
//                getDevicesFromRoom(dataBean.getRoomId());
            }
        });
        gv_devices.setOnItemClickListener(this);
        ll_content.setFocusable(true);
        ll_content.setFocusableInTouchMode(true);

        adapter = new CommonAdapter(mContext, projectVoList, R.layout.item_common_device) {
            @Override
            protected void convertView(View item, Object o) {
                final RoomDevice roomDevice = (RoomDevice) o;
                TextView tv_device_name = item.findViewById(R.id.tv_device_name);
                TextView tv_device_state = item.findViewById(R.id.tv_device_state);
                ImageView iv_icon = item.findViewById(R.id.iv_icon);
                final CheckBox iv_state = item.findViewById(R.id.iv_state);
                if (TextUtils.isEmpty(roomDevice.getNickName())){
                    tv_device_name.setText(roomDevice.getProductName());
                }else{
                    tv_device_name.setText(roomDevice.getNickName());
                }
                if (roomDevice.getStatus() ==1 ){
                    tv_device_state.setText("在线");
                    tv_device_state.setTextColor(mContext.getResources().getColor(R.color.text_black));
                }else{
                    tv_device_state.setText("离线");
                    tv_device_state.setTextColor(mContext.getResources().getColor(R.color.text_main_gray));
                }
                ImageUtil.setImage(mContext,roomDevice.getProductImage(),iv_icon);

                if (roomDevice.getProductKey().equals(Constants.ONE_SWITCH)||roomDevice.getProductKey().equals(Constants.OUTLET)
                        ||roomDevice.getProductKey().equals(Constants.WIND_CONTROL)||roomDevice.getProductKey().equals(Constants.WATER_CONTROL)||
                roomDevice.getProductKey().equals(Constants.ELECTRIC_CONTROL)||
                        roomDevice.getProductKey().equals(Constants.INFRARED_CHANGE_CONTROL)){
                    iv_state.setVisibility(View.VISIBLE);
                    Attribute attribute = null;
                    for (int i=0;i<roomDevice.getAttributeList().size();i++){
                        if (roomDevice.getAttributeList().get(i).getAttribute().equals("PowerSwitch_1")||roomDevice.getAttributeList().get(i).getAttribute().equals("PowerSwitch")){
                            attribute = roomDevice.getAttributeList().get(i);
                            break;
                        }
                    }
                    if (attribute!=null){
                        double value = (double) attribute.getValue();
                        if (value == 1.0){
                                iv_state.setChecked(true);
                        }else{
                            iv_state.setChecked(false);
                        }
                    }
                }else{
                    iv_state.setVisibility(View.GONE);
                }
                iv_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int state = 0;
                        if (isChecked){
                            state = 1;
                        }else{
                            state = 0;
                        }
                        if (roomDevice.getProductKey().equals(Constants.OUTLET)){
                            controlDevice(roomDevice.getIotId(),0,state,iv_state);
                        }else{
                            controlDevice(roomDevice.getIotId(),1,state,iv_state);
                        }
                    }
                });
            }
        };
        gv_devices.setAdapter(adapter);
    }

    @Override
    protected void pauseData() {

    }

    @Override
    protected void loadData() {

        Log.d(TAG, room.getName());

        Map<String, Object> map = new HashMap<>();
        map.put("projectId", SettingUtils.get(mContext, "projectId"));
        map.put("spaceId", room.getId());
        map.put("includeSubSpace", true);
        map.put("pageNo", 1);
        map.put("pageSize", 100);
        apiConfig.getRoomDevice(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                projectVoList.clear();
                refreshLayout.setRefreshing(false);
                if (baseResponse != null) {
                    Projects<RoomDevice> projects = gson.fromJson(gson.toJson(baseResponse.data), new TypeToken<Projects<RoomDevice>>() {
                    }.getType());
                    projectVoList = projects.getData();
                    if (projectVoList==null){
                        projectVoList = new ArrayList<>();
                    }
                    adapter.notifyDataSetChanged(projectVoList);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                refreshLayout.setRefreshing(false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                moreBtn(v);
                break;
            case R.id.pop_room_setting:
                more_pop.dismiss();
                intent = new Intent(getContext(), RoomSettingActivity.class);
                intent.putExtra("room", room);
                startActivityForResult(intent, Constants.DEL_ROOM);
                break;
            case R.id.pop_add_scene:
                more_pop.dismiss();
                intent = new Intent(getContext(), ADDDeviceListActivity.class);
                intent.putExtra("room", room);
                getParentFragment().startActivityForResult(intent, Constants.REFRESH_DEVICE);
                break;
        }
    }


    /**
     * 点击添加按钮执行的操作
     *
     * @param view 添加
     */
    private void moreBtn(View view) {
        if (more_pop == null) {
            more_pop = IndexPopWindowUtlis.getAsDropDownPopWindow(mContext, R.layout.pop_room, true);
            View more_contentview = more_pop.getContentView();
            more_contentview.findViewById(R.id.pop_add_scene).setOnClickListener(this);
            more_contentview.findViewById(R.id.pop_room_setting).setOnClickListener(this);
            more_contentview.findViewById(R.id.pop_share_room).setOnClickListener(this);
            more_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    IndexPopWindowUtlis.setBackgroundAlpha(1.0f, mContext);
                }
            });
        }
        more_pop.showAsDropDown(view, 20, 30);
        IndexPopWindowUtlis.setBackgroundAlpha(0.5f, mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RoomDevice roomDevice = projectVoList.get(position);
            if (roomDevice.getProductKey().equals(Constants.TWO_SWITCH)){
                    intent = new Intent(mContext, TwoSwitchActivity.class);
                    intent.putExtra("device",roomDevice);
                    intent.putExtra("room",room);
                startActivityForResult(intent,Constants.REFRESH_DEVICE);
            }else if (roomDevice.getProductKey().equals(Constants.OUTLET)){
                intent = new Intent(mContext, OutletActivity.class);
                intent.putExtra("device",roomDevice);
                startActivityForResult(intent,Constants.REFRESH_DEVICE);
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1004:
                    Log.d(TAG, "SingleRoomFragment");
                    if (resultCode == Constants.SUCCESS) {
                        linkToast(mContext, "添加成功");
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.roomFragment.getRoomList();
                    } else {
                        linkToast(mContext, "添加失败");
                    }
                    break;
                case 1005:
                    Log.d(TAG, "SingleRoomFragment");
                    loadData();
                    break;
            }
        }
    }

    /**
     * 刷新设备
     */
    public void refreshDevice(){
            loadData();
    }

    /**
     * 获取房间id
     * @return
     */
    public String getRoomId(){
        return room.getId();
    }

    /*
     * 设备控制
     * 0 关，1开
     * */
    private void controlDevice(String iotId, final int index, final int value, final CheckBox checkBox) {
        try {
            JSONObject paramsJsonObject = new JSONObject();
            paramsJsonObject.put("targetId", iotId);
            JSONObject itemsJSonObject = new JSONObject();
            if (index == 0) {
                itemsJSonObject.put("PowerSwitch", value);

            } else {
                itemsJSonObject.put("PowerSwitch_1" + index, value);
            }
            paramsJsonObject.put("properties", itemsJSonObject);
            apiConfig.controlDevice(SettingUtils.get(mContext,"token"), paramsJsonObject.toString()).subscribe(new Action1<ResponseBody>() {
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
                            boolean flag = false;
                            if (value == 1){
                                flag = true;
                            }else{
                                flag = false;
                            }
                            checkBox.setChecked(flag);

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
}
