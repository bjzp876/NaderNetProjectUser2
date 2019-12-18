package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Week;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择星期
 * author:zhangpeng
 * date: 2019/9/12
 */
public class SceneDateActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right;
    private FrameLayout fl_right;

    private ListView lv_scene_list;
    private CommonAdapter commonAdapter;
    private List<Week> weeks = new ArrayList<>();
    private String weeksSaveStr;

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

        commonAdapter = new CommonAdapter(mContext,weeks,R.layout.item_scene_week) {
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
                if (week.isFlag()){
                    isCheck = false;
                }else{
                    isCheck = true;
                }
                weeks.get(position).setFlag(isCheck);
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
        weeksSaveStr = getIntent().getStringExtra("weeksSaveStr");
        tv_right.setText("保存");
        tv_right.setTextColor(getResources().getColor(R.color.text_green));
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("重复");
        fl_right.setVisibility(View.VISIBLE);
        Week week1 = new Week();
        week1.setDate("星期一");
        Week week2 = new Week();
        week2.setDate("星期二");
        Week week3 = new Week();
        week3.setDate("星期三");
        Week week4 = new Week();
        week4.setDate("星期四");
        Week week5 = new Week();
        week5.setDate("星期五");
        Week week6 = new Week();
        week6.setDate("星期六");
        Week week7 = new Week();
        week7.setDate("星期日");
        weeks.add(week1);
        weeks.add(week2);
        weeks.add(week3);
        weeks.add(week4);
        weeks.add(week5);
        weeks.add(week6);
        weeks.add(week7);
        if (TextUtils.isEmpty(weeksSaveStr)||weeksSaveStr.equals("*")){

        }else{
            String[] crons = weeksSaveStr.split(",");
            for (int i=0;i<crons.length;i++){
                int num = Integer.valueOf(crons[i])-1;
                weeks.get(num).setFlag(true);
            }
        }
        commonAdapter.notifyDataSetChanged(weeks);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                linkToast(mContext,"保存成功");
                String data = gson.toJson(weeks);
                intent = new Intent();
                intent.putExtra("data",data);
                setResult(Constants.SUCCESS,intent);
                finish();
                break;
        }
    }
}
