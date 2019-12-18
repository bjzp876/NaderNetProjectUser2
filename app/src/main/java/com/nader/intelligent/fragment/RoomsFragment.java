package com.nader.intelligent.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.main.MainActivity;
import com.nader.intelligent.adapter.RoomAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseFragment;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Projects;
import com.nader.intelligent.entity.Rooms;
import com.nader.intelligent.util.FileUtil;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 房间列表页
 * author:zhangpeng
 * date: 2019/11/8
 */
public class RoomsFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout rl_add_room;
    private LinearLayout ll_null;

    private ViewPager viewPager;
    private TabLayout tablayout;
    private RoomAdapter adapter;
    //空页面
    private List<House> houseList = new ArrayList<>();
    private House house;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    viewPager.setVisibility(View.VISIBLE);
                    tablayout.setVisibility(View.VISIBLE);
                    ll_null.setVisibility(View.GONE);
                    adapter = new RoomAdapter(getChildFragmentManager(), houseList);
                    viewPager.setOffscreenPageLimit(houseList.size());
                    viewPager.setAdapter(adapter);
                    break;
            }
        }
    };

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
        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        initView();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView() {
        //实例化
        viewPager = view.findViewById(R.id.viewpager);
        rl_add_room = view.findViewById(R.id.rl_add_room);
        ll_null = view.findViewById(R.id.ll_null);
        rl_add_room.setOnClickListener(this);
        //页面，数据源
        //ViewPager的适配器
        tablayout = view.findViewById(R.id.tablayout);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tablayout.setTabTextColors(mContext.getResources().getColor(R.color.text_gray), Color.parseColor("#4A4A4A"));
    }


    @Override
    protected void pauseData() {

    }

    @Override
    protected void loadData() {
        getRoomList();
    }


    /**
     * 获取房屋列表
     */
    public void getRoomList(){
        String houseId;
        if (TextUtils.isEmpty(SettingUtils.get(mContext,"house"))){
            ll_null.setVisibility(View.GONE);
            return;
        }
        house = gson.fromJson(SettingUtils.get(mContext,"house"),House.class);
        houseId = house.getId();
        Map<String,Object> params = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        String[] str = new String[1];
        str[0] = "room";
        params.put("spaceId",houseId);
        params.put("rootSpaceId",houseId);
        params.put("pageNo",1);
        params.put("pageSize",100);
        params.put("typeCodeList",str);
        map.put("spaceQuery",params);
        apiConfig.getHouseForAli(SettingUtils.get(mContext,"token"),map).subscribe(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                Projects<House> response = gson.fromJson(gson.toJson(baseResponse.data), new TypeToken<Projects<House>>(){}.getType());
                houseList.clear();
                houseList = response.getData();
                if (houseList!=null&&houseList.size()>0){
                    mHandler.sendEmptyMessage(0);
                }else{
                    ll_null.setVisibility(View.VISIBLE);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case 1004:
                    Log.d(TAG,"RoomsFragment");
                    if (resultCode == Constants.SUCCESS){
                        linkToast(mContext,"删除成功");
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.roomFragment.getRoomList();
                    }else{
                        linkToast(mContext,"删除失败");
                    }
                    break;
                case 1005:
                    Log.d(TAG,"RoomsFragment");
                    if (resultCode == Constants.SUCCESS){
                        House house = null;
                        int num = -1;
                        String roomId = data.getStringExtra("roomId");

                        if (!TextUtils.isEmpty(roomId)){
                            List<SingleRoomFragment> list = new ArrayList<>();
                            list = adapter.getRoomData();

                            for (int i=0;i<list.size();i++){
                                    if (list.get(i).getRoomId().equals(roomId)){
                                        Log.d(TAG,"REFRESH_DEVICE");
                                        list.get(i).refreshDevice();
                                        break;
                                    }
                            }
                        }

                    }
                    break;
                case 1006:
                    Log.d(TAG,"RoomsFragment");
                    if (resultCode == Constants.SUCCESS){
                        House house = null;
                        int num = -1;
                        String roomId = data.getStringExtra("roomId");

                        if (!TextUtils.isEmpty(roomId)){
                            List<SingleRoomFragment> list = new ArrayList<>();
                            list = adapter.getRoomData();

                            for (int i=0;i<list.size();i++){
                                if (list.get(i).getRoomId().equals(roomId)){
                                    Log.d(TAG,"REFRESH_DEVICE");
                                    list.get(i).refreshDevice();
                                    break;
                                }
                            }
                        }

                    }
                    break;
            }
        }
    }
}
