package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择条件
 * author:zhangpeng
 * date: 2019/9/12
 */
public class SceneFactorActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;

    private ListView lv_scene_list;
    private CommonAdapter commonAdapter;
    private List<Scene> scenes = new ArrayList<>();
    private int state = 0;

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

        commonAdapter = new CommonAdapter(mContext,scenes,R.layout.item_scene_list) {
            @Override
            protected void convertView(View item, Object o) {
                Scene scene = (Scene) o;
                TextView name = item.findViewById(R.id.tv_scene_name);
                TextView description = item.findViewById(R.id.tv_scene_description);
                ImageView arrow = item.findViewById(R.id.iv_arrow);
                arrow.setVisibility(View.VISIBLE);
                name.setText(scene.getName());
                description.setText(scene.getDescription());
            }
        };
        lv_scene_list.setAdapter(commonAdapter);
        lv_scene_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0){
                    intent = new Intent(mContext,SceneListActivity.class);
                    intent.putExtra("stateNum",getIntent().getIntExtra("stateNum",1));
                    startActivity(intent);

                }else{
                    if (getIntent().getIntExtra("stateNum",1) == 1){
                        intent = new Intent(mContext,SceneTimeSelectActivity.class);
                    }else{
                        intent = new Intent(mContext,SceneChangeTimeActivity.class);
                    }
                    intent.putExtra("stateNum",getIntent().getIntExtra("stateNum",1));
                    startActivity(intent);
                }
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
        tv_title.setText("选择设备");
        if (getIntent().getIntExtra("stateNum",1) == 1){
            Scene scene = new Scene();
            scene.setName("定时触发");
            scene.setDescription("例如：每天上午6点");
            Scene scene1 = new Scene();
            scene1.setName("设备事件触发");
            scene1.setDescription("如“门打开”或“灯打开”");
            scenes.add(scene);
            scenes.add(scene1);
        }else{
            Scene scene = new Scene();
            scene.setName("时间限制");
            scene.setDescription("例如：工作日19-22点");
            Scene scene1 = new Scene();
            scene1.setName("设备状态");
            scene1.setDescription("如“门打开”或“灯打开”");
            scenes.add(scene);
            scenes.add(scene1);
        }
        commonAdapter.notifyDataSetChanged(scenes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
