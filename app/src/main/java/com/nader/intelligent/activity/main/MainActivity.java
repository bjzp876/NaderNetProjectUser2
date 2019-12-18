package com.nader.intelligent.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.iot.link.ui.component.LinkToast;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.MainAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.fragment.HomeFragment;
import com.nader.intelligent.fragment.RoomsFragment;
import com.nader.intelligent.fragment.SafetyFragment;
import com.nader.intelligent.fragment.SceneListFragment;
import com.nader.intelligent.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private NoScrollViewPager container;
    private MainAdapter adapter;
    private FragmentManager fm;
    private List<Fragment> fragments = new ArrayList<>();
    //房间列表
    public RoomsFragment roomFragment;
    //常用列表
    private HomeFragment homePageFragment;
    //安防页
    private SafetyFragment safetyFragment;
    //场景列表
    private SceneListFragment sceneListFragment;
    //首页
    private ImageView iv_homepage;
    private TextView tv_homepage;
    //房间
    private ImageView iv_room;
    private TextView tv_room;
    //场景
    private ImageView iv_scene;
    private TextView tv_scene;
    //安防
    private ImageView iv_security;
    private TextView tv_security;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        container = findViewById(R.id.container);
        findViewById(R.id.ll_homepage).setOnClickListener(this);
        findViewById(R.id.ll_room).setOnClickListener(this);
        findViewById(R.id.ll_scene).setOnClickListener(this);
        findViewById(R.id.ll_security).setOnClickListener(this);
        iv_homepage = findViewById(R.id.iv_homepage);
        tv_homepage = findViewById(R.id.tv_homepage);
        iv_room = findViewById(R.id.iv_room);
        tv_room = findViewById(R.id.tv_room);
        iv_scene = findViewById(R.id.iv_scene);
        tv_scene = findViewById(R.id.tv_scene);
        iv_security = findViewById(R.id.iv_security);
        tv_security = findViewById(R.id.tv_security);
        roomFragment = new RoomsFragment();
        homePageFragment = new HomeFragment();
        sceneListFragment = new SceneListFragment();
        safetyFragment = new SafetyFragment();
        fragments.add(homePageFragment);
        fragments.add(roomFragment);
        fragments.add(sceneListFragment);
        fragments.add(safetyFragment);
        fm = getSupportFragmentManager();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void initData() {
        super.initData();
        adapter = new MainAdapter(fm, fragments);
        container.setOffscreenPageLimit(4);
        container.setAdapter(adapter);
        setChecked(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_homepage:
                //主页
                setChecked(0);
                break;
            case R.id.ll_room:
                //房间
                setChecked(1);
                break;
            case R.id.ll_scene:
                //场景
                setChecked(2);
                break;
            case R.id.ll_security:
                //安防
                setChecked(3);
                break;
        }
    }

    /**
     * @param index 底部下标
     */
    /*
     *@date:2018/6/19 21:32
     *@author:6648
     *@description:底部选中状态
     */
    private void setChecked(int index) {
        iv_homepage.setImageResource(R.drawable.homepage_logo_up);
        iv_room.setImageResource(R.drawable.room_up);
        iv_scene.setImageResource(R.drawable.scene);
        iv_security.setImageResource(R.drawable.safe_up);
        tv_homepage.setTextColor(getResources().getColor(R.color.text_gray));
        tv_room.setTextColor(getResources().getColor(R.color.text_gray));
        tv_scene.setTextColor(getResources().getColor(R.color.text_gray));
        tv_security.setTextColor(getResources().getColor(R.color.text_gray));
        if (index == 0) {
            iv_homepage.setImageResource(R.drawable.homepage_logo_down);
            tv_homepage.setTextColor(getResources().getColor(R.color.color_for_common_secnes_on));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, HomePageFragment.newInstance()).commitAllowingStateLoss();
        } else if (index == 1) {
            iv_room.setImageResource(R.drawable.room_down);
            tv_room.setTextColor(getResources().getColor(R.color.color_for_common_secnes_on));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, RoomsFragment.newInstance()).commitAllowingStateLoss();
        } else if (index == 2) {
            iv_scene.setImageResource(R.drawable.scene_2);
            tv_scene.setTextColor(getResources().getColor(R.color.color_for_common_secnes_on));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, SecurityFragment.newInstance(gateway)).commitAllowingStateLoss();
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, HomePageFragment.newInstance()).commitAllowingStateLoss();
        } else if (index == 3) {
            iv_security.setImageResource(R.drawable.safe_down);
            tv_security.setTextColor(getResources().getColor(R.color.color_for_common_secnes_on));
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, SecurityFragment.newInstance(gateway)).commitAllowingStateLoss();
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, HomePageFragment.newInstance()).commitAllowingStateLoss();
        }
        container.setCurrentItem(index, false);
    }


    /**
     * 第三种方法
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1003:
                    if (resultCode == Constants.SUCCESS) {
                        linkToast(mContext, "添加成功");
                        roomFragment.getRoomList();
                    } else {
                        linkToast(mContext, "添加失败");
                    }
                    break;
                case 1004:
                    if (resultCode == Constants.SUCCESS) {
                        linkToast(mContext, "删除成功");
                        roomFragment.getRoomList();
                    } else {
                        linkToast(mContext, "删除失败");
                    }
                    break;
                case 0x012:
                    if ((RESULT_OK == resultCode)) {
                        if (data == null) {
                            LinkToast.makeText(this, "二维码解析失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // result can be either bar code or failed message
                        String result = data.getStringExtra("data");
                        Log.d(TAG, result);
                        Uri uri = Uri.parse(result);
                        if (null == uri) {
                            LinkToast.makeText(this, "二维码解析失败", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (null == uri.getQueryParameter("pk")) {
//                            handleQRCode(uri);
                        } else {
                            //TODO

//                            HouseBean checkHouse = HouseBean.getCheckHousBean(mContext);
//                            if (checkHouse != null) {
//                                if (checkHouse.getRole() != 1) {
//                                    LinkToast.makeText(mContext, "家庭用户无法添加设备", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    groupId = checkHouse.getHouseId();
//                                    AddDeviceScanHelper.wiFiConfig(this, data, groupId);
//                                }
//                            } else {
//                                LinkToast.makeText(mContext, "您还没有添加房屋", Toast.LENGTH_SHORT).show();
//                            }


                        }
                    } else if (resultCode == RESULT_CANCELED && data != null) {
                        String message = data.getStringExtra("data");
                        if (message != null) {
                            LinkToast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                //刷新场景
                case 1007:
                    sceneListFragment.refreshData();
                    break;
            }
        }
    }
}
