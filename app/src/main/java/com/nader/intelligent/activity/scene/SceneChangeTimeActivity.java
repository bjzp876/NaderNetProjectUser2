package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Scene;
import com.nader.intelligent.entity.Week;
import com.nader.intelligent.util.ChineseNumToArabicNumUtil;
import com.nader.intelligent.view.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 设定时间
 * author:zhangpeng
 * date: 2019/9/12
 */
public class SceneChangeTimeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private FrameLayout fl_right;
    private TextView tv_right;

    private RelativeLayout rl_scene_change;

    private LinearLayout ll_start;
    private RelativeLayout rl_start;
    private TextView tv_start_time;
    private WheelView wv1;
    private WheelView wv2;

    private LinearLayout ll_end;
    private RelativeLayout rl_end;
    private TextView tv_end_time;
    private WheelView wv3;
    private WheelView wv4;
    private TextView tv_select_date;

    private String hour_start = "00";
    private String minutes_start = "00";
    private boolean startFlag = false;
    private boolean endFlag = false;
    private String hour_end = "00";
    private String minutes_end = "00";
    private int stateNum = 1;
    private List<Week> weeks = new ArrayList<>();
    private String weeksSaveStr = "";
    private String weeksPreform;
    private Scene scene = null;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scene_changetime);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        fl_right = findViewById(R.id.fl_right);
        tv_right = findViewById(R.id.tv_right);
        rl_start = findViewById(R.id.rl_start);
        rl_scene_change = findViewById(R.id.rl_scene_change);

        ll_start = findViewById(R.id.ll_start);
        ll_end = findViewById(R.id.ll_end);
        rl_end = findViewById(R.id.rl_end);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_select_date = findViewById(R.id.tv_select_date);

        wv1 = findViewById(R.id.wv1);
        wv2 = findViewById(R.id.wv2);
        wv3 = findViewById(R.id.wv3);
        wv4 = findViewById(R.id.wv4);
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        fl_right.setOnClickListener(this);
        rl_scene_change.setOnClickListener(this);
        tv_start_time.setOnClickListener(this);
        tv_end_time.setOnClickListener(this);
        rl_start.setOnClickListener(this);
        rl_end.setOnClickListener(this);
        wv1.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                hour_start = item;
                setStartTime();
            }
        });
        wv2.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                minutes_start = item;
                setStartTime();
            }
        });
        wv3.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                hour_end = item;
                setEndTime();
            }
        });
        wv4.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                minutes_end = item;
                setEndTime();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        stateNum = getIntent().getIntExtra("stateNum", 1);
        scene = (Scene) getIntent().getSerializableExtra("scene");
        position = getIntent().getIntExtra("position", -1);
        tv_title.setText("设置时间段");
        fl_right.setVisibility(View.VISIBLE);
        tv_right.setText("保存");
        tv_right.setTextColor(getResources().getColor(R.color.text_green));
        List<String> hours = new ArrayList<>();
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 24) {
                hours.add(String.valueOf(i));
            }
            minutes.add(String.valueOf(i));
        }
        if (scene != null) {
            String cron = scene.getCron();
            if (!TextUtils.isEmpty(cron)) {
                String[] cronStr = cron.split(" ");
                String beginDate = cronStr[0];
                String endDate = cronStr[1];
                String[] beginTime = beginDate.split(":");
                String[] endTime = endDate.split(":");
                weeksSaveStr = cronStr[cronStr.length - 1];
                String[] weeks = new String[]{};
                weeks = weeksSaveStr.split(",");
                String week = "";
                tv_start_time.setText(beginDate);
                tv_end_time.setText(endDate);
                wv1.setItems(hours, Integer.valueOf(beginTime[0]));
                wv3.setItems(hours, Integer.valueOf(endTime[0]));
                wv2.setItems(minutes, Integer.valueOf(beginTime[1]));
                wv4.setItems(minutes, Integer.valueOf(endTime[1]));
                if (weeks != null && weeks.length > 0) {
                    for (String weekNum : weeks) {
                        week += "星期" + ChineseNumToArabicNumUtil.arabicNumToChineseNum(Integer.valueOf(weekNum)) + ",";
                    }
                    week = week.substring(0, week.length() - 1);
                    tv_select_date.setText(week);
                }

            } else {
                wv1.setItems(hours, 0);
                wv3.setItems(hours, 0);
                wv2.setItems(minutes, 0);
                wv4.setItems(minutes, 0);
            }
        } else {
            wv1.setItems(hours, 0);
            wv3.setItems(hours, 0);
            wv2.setItems(minutes, 0);
            wv4.setItems(minutes, 0);
        }
        wv1.setDegree("时");
        wv2.setDegree("分");
        wv3.setDegree("时");
        wv4.setDegree("分");
        getVisible();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fl_right:
                String description = "";
                description = tv_start_time.getText().toString().trim() + " " + tv_end_time.getText().toString().trim() + " " + weeksSaveStr;
                intent = new Intent(mContext, SceneDetailActivity.class);
                intent.putExtra("stateNum", stateNum);
                intent.putExtra("beginDate", tv_start_time.getText().toString().trim());
                intent.putExtra("endDate", tv_end_time.getText().toString().trim());
                intent.putExtra("cron", weeksSaveStr);
                if (!TextUtils.isEmpty(weeksSaveStr)) {
                    intent.putExtra("repeat", weeksSaveStr);
                }
                intent.putExtra("description", description);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_scene_change:
                intent = new Intent(mContext, SceneDateActivity.class);
                intent.putExtra("weeksSaveStr", weeksSaveStr);
                startActivityForResult(intent, Constants.DATE_WEEK);
                break;
            case R.id.rl_start:
                if (startFlag) {
                    startFlag = false;
                } else {
                    startFlag = true;
                }
                getVisible();
                break;
            case R.id.rl_end:
                if (endFlag) {
                    endFlag = false;
                } else {
                    endFlag = true;
                }
                getVisible();
                break;
        }
    }

    /**
     * 设置开始时间
     */
    private void setStartTime() {
        String hour = "";
        String minute = "";
        if (Integer.valueOf(hour_start) < 10) {
            hour = "0" + hour_start;
        } else {
            hour = hour_start;
        }
        if (Integer.valueOf(minutes_start) < 10) {
            if (minutes_start.equals("00")) {
                minute = "00";
            } else {
                minute = "0" + minutes_start;
            }

        } else {
            minute = minutes_start;
        }
        tv_start_time.setText(hour + ":" + minute);
    }

    /**
     * 设置结束时间
     */
    private void setEndTime() {
        String hour = "";
        String minute = "";
        if (Integer.valueOf(hour_end) < 10) {
            hour = "0" + hour_end;
        } else {
            hour = hour_end;
        }
        if (Integer.valueOf(minutes_end) < 10) {
            if (minutes_end.equals("00")) {
                minute = "00";
            } else {
                minute = "0" + minutes_end;
            }

        } else {
            minute = minutes_end;
        }
        tv_end_time.setText(hour + ":" + minute);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1008:
                    String data1 = data.getStringExtra("data");
                    weeks.clear();
                    weeksSaveStr = "";
                    if (!TextUtils.isEmpty(data1)) {
                        weeks = gson.fromJson(data1, new TypeToken<List<Week>>() {
                        }.getType());
                        if (weeks != null && weeks.size() > 0) {
                            String weekStr = "";
                            for (int i = 0; i < weeks.size(); i++) {
                                if (weeks.get(i).isFlag()) {
                                    weekStr += weeks.get(i).getDate() + ",";
                                    weeksSaveStr += (i + 1) + ",";
                                    weeksPreform += " " + i;
                                }
                            }
                            weeksSaveStr = weeksSaveStr.substring(0, weeksSaveStr.length() - 1);
                            weekStr = weekStr.substring(0, weekStr.length() - 1);
                            tv_select_date.setText(weekStr);
                            weeksSaveStr.trim();
                            Log.d(TAG, weeksSaveStr);
                        }
                    }
                    break;
            }
        }
    }

    private void getVisible() {
        if (startFlag) {
            ll_start.setVisibility(View.VISIBLE);
        } else {
            ll_start.setVisibility(View.GONE);
        }
        if (endFlag) {
            ll_end.setVisibility(View.VISIBLE);
        } else {
            ll_end.setVisibility(View.GONE);
        }
    }


}
