package com.nader.intelligent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aliyun.iot.aep.component.router.Router;
import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.main.MainActivity;
import com.nader.intelligent.activity.room.AddRoomNameActivity;
import com.nader.intelligent.activity.scan.ScanActivity;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseFragment;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Projects;
import com.nader.intelligent.entity.Triggers;
import com.nader.intelligent.entity.vo.ProjectVo;
import com.nader.intelligent.util.IndexPopWindowUtlis;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.util.http.Fault;
import com.nader.intelligent.util.permission.PermissionListener;
import com.nader.intelligent.util.permission.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

import static android.Manifest.permission.CAMERA;

/**
 * 首页
 * author:zhangpeng
 * date: 2019/11/7
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private SwipeRefreshLayout refresh;
    //房屋名称
    private TextView tv_user_name;
    //添加
    private ImageView iv_add_img;
    private PopupWindow more_pop;
    private House house;
    //权限
    private String[] permissions = new String[]{CAMERA};

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
        view = inflater.inflate(R.layout.fragment_hoom, null);
        initView();
        return view;
    }


    @Override
    protected void initView() {
        refresh = view.findViewById(R.id.refresh);
        iv_add_img = view.findViewById(R.id.iv_add_img);
        tv_user_name = view.findViewById(R.id.tv_user_name);

        iv_add_img.setOnClickListener(this);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    @Override
    protected void pauseData() {

    }

    @Override
    protected void loadData() {
        getProjectList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_img:
                moreBtn(iv_add_img);
                break;
            case R.id.more_pop_add_room_btn:
                more_pop.dismiss();
                intent = new Intent(mContext, AddRoomNameActivity.class);
                startActivityForResult(intent, Constants.ADD_ROOM);
                break;
            case R.id.more_pop_scan_btn:
                more_pop.dismiss();
                intent = new Intent(mContext, ScanActivity.class);
                startActivityForResult(intent, 0x012);
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
            more_pop = IndexPopWindowUtlis.getAsDropDownPopWindow(mContext, R.layout.page_index_more_pop_layout, true);
            View more_contentview = more_pop.getContentView();
            more_contentview.findViewById(R.id.more_pop_scan_btn).setOnClickListener(this);
            more_contentview.findViewById(R.id.more_pop_add_equipment_btn).setOnClickListener(this);
            more_contentview.findViewById(R.id.more_pop_add_room_btn).setOnClickListener(this);
            more_contentview.findViewById(R.id.more_pop_share_btn).setOnClickListener(this);
            more_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    IndexPopWindowUtlis.setBackgroundAlpha(1.0f, mContext);
                }
            });
        }

        more_pop.showAtLocation(view, Gravity.TOP | Gravity.END, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, mContext.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, mContext.getResources().getDisplayMetrics()));

        IndexPopWindowUtlis.setBackgroundAlpha(0.5f, mContext);
    }

    /**
     * 获取项目列表
     */
    private void getProjectList() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("pageNo", 1);
        params.put("pageSize", 100);
        map.put("spaceQuery", params);
        apiConfig.getProjectForAli(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse responseBody) {

                if (responseBody != null) {

                    Projects<ProjectVo> response = gson.fromJson(gson.toJson(responseBody.data), new TypeToken<Projects<ProjectVo>>() {
                    }.getType());
                    List<ProjectVo> projectVoList = new ArrayList<>();
                    projectVoList = response.getData();
                    if (projectVoList != null && projectVoList.size() > 0) {
                        ProjectVo projectVo = new ProjectVo();
                        projectVo = projectVoList.get(2);
                        getHouseList(projectVo.getId());
                        SettingUtils.set(mContext, "projectId", projectVo.getId());
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                refresh.setRefreshing(false);
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

    /**
     * 获取房屋列表
     */
    private void getHouseList(String projectId) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String[] str = new String[1];
        str[0] = "house";
        params.put("spaceId", projectId);
        params.put("rootSpaceId", projectId);
        params.put("pageNo", 1);
        params.put("pageSize", 100);
        params.put("typeCodeList", str);
        map.put("spaceQuery", params);
        apiConfig.getHouseForAli(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                refresh.setRefreshing(false);
                Projects<House> response = gson.fromJson(gson.toJson(baseResponse.data), new TypeToken<Projects<House>>() {
                }.getType());
                List<House> houseList = new ArrayList<>();
                houseList = response.getData();
                String saveData = SettingUtils.get(mContext,"house");
                house = null;
                if (!TextUtils.isEmpty(saveData)){
                    House saveHouse = gson.fromJson(saveData,House.class);
                    if (houseList != null && houseList.size() > 0) {
                        for (int i=0;i<houseList.size();i++){
                            if (houseList.get(i).getId().equals(saveHouse.getId())){
                                house = houseList.get(i);
                                break;
                            }
                        }
                        //该用户有房屋
                        if (house == null){
                            house = houseList.get(0);
                        }
                        SettingUtils.set(mContext, "house", gson.toJson(house));
                        tv_user_name.setText(house.getName());
                    }
                    //初始化页面
                    else{

                    }

                }else{
                    if (houseList != null && houseList.size() > 0) {
                        house = houseList.get(0);
                        SettingUtils.set(mContext, "house", gson.toJson(house));
                        tv_user_name.setText(house.getName());
                    }//初始化页面
                    else{

                    }
                }


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                refresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1003:
                    if (resultCode == Constants.SUCCESS) {
                        linkToast(mContext, "添加成功");
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.roomFragment.getRoomList();
                    } else {
                        linkToast(mContext, "添加失败");
                    }
                    break;
            }
        }
    }
}
