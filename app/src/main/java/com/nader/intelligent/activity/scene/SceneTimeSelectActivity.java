package com.nader.intelligent.activity.scene;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;
import com.nader.intelligent.R;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Scene;
import com.nader.intelligent.entity.Week;
import com.nader.intelligent.util.ChineseNumToArabicNumUtil;
import com.nader.intelligent.view.wheelview.TimeRange;
import com.nader.intelligent.view.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 场景时间选择页
 * author:zhangpeng
 * date: 2019/9/12
 */
public class SceneTimeSelectActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private FrameLayout fl_right;
    private TextView tv_right;
    private TextView tv_title;
    private TextView tv_date_select;

    private WheelView wv1;
    private WheelView wv2;

    private RelativeLayout rl_date;
    private List<Week> weeks = new ArrayList<>();
    private String weeksSaveStr = "";
    private String weeksPreform;
    private int stateNum = 1;
    private Scene scene = null;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scene_timeselect);
        initView();
        initListener();
        initData();
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void initView() {
        super.initView();
        iv_back = findViewById(R.id.iv_back);
        tv_right = findViewById(R.id.tv_right);
        fl_right = findViewById(R.id.fl_right);
        tv_title = findViewById(R.id.tv_title);
        tv_date_select = findViewById(R.id.tv_date_select);
        wv1 = findViewById(R.id.wv1);
        wv2 = findViewById(R.id.wv2);
        rl_date = findViewById(R.id.rl_date);
    }

    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        rl_date.setOnClickListener(this);
        fl_right.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        stateNum = getIntent().getIntExtra("stateNum",1);
        scene = (Scene) getIntent().getSerializableExtra("scene");
        position = getIntent().getIntExtra("position",-1);
        fl_right.setVisibility(View.VISIBLE);
        tv_right.setText("保存");
        tv_right.setTextColor(getResources().getColor(R.color.text_green));
        tv_right.setVisibility(View.VISIBLE);
        tv_title.setText("设定时间");

        final TimeRange timeRange = getTimeRange();
        List<String> hours = new ArrayList<>();
        List<String> minutes = new ArrayList<>();
        for (int i=0;i<60;i++){
            if (i<24){
                hours.add(String.valueOf(i));
            }
            minutes.add(String.valueOf(i));
        }
        if (scene!=null){
            String cron = scene.getCron();
            String[] cronStr = new String[]{};
            cronStr = cron.split(" ");
            wv1.setItems(hours,Integer.valueOf(cronStr[1]));
            wv2.setItems(minutes,Integer.valueOf(cronStr[0]));
            weeksSaveStr = cronStr[cronStr.length-1];
            String[] timeStr = weeksSaveStr.split(",");
            String time = "";
            if (timeStr.length>0){
                for (String date : timeStr){
                        time += "星期"+ ChineseNumToArabicNumUtil.arabicNumToChineseNum(Integer.valueOf(date))+",";
                }
                time = time.substring(0,time.length()-1);
                tv_date_select.setText(time);
            }
        }else{
            wv1.setItems(hours,0);
            wv2.setItems(minutes,0);
        }

        wv1.setDegree("时");
        wv2.setDegree("分");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                String minute = "*";
                String hour = "*";
                String cron = "";
                String description= "";
                    if (Integer.valueOf(wv2.getSelectedItem())<10){
                        minute = "0"+wv2.getSelectedItem();
                    }else{
                        minute = wv2.getSelectedItem();
                    }

                    if (Integer.valueOf(wv1.getSelectedItem())<10){
                        hour = "0"+wv1.getSelectedItem();
                    }else{
                        hour = wv1.getSelectedItem();
                    }

                if (TextUtils.isEmpty(weeksSaveStr)){
                    weeksSaveStr = "*";
                }
                if (!wv2.getSelectedItem().equals("0")||!wv1.getSelectedItem().equals("0")){
                    description = hour+":"+minute;
                }
                if (!TextUtils.isEmpty(weeksPreform)){
                    description = description+" "+weeksPreform;
                }
                cron = minute+" "+hour+" * * "+weeksSaveStr;
                Log.d(TAG,cron);
                intent = new Intent(mContext,SceneDetailActivity.class);
                intent.putExtra("cron",cron);
                intent.putExtra("stateNum",stateNum);
                intent.putExtra("description",description);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_date:
                intent = new Intent(mContext,SceneDateActivity.class);
                intent.putExtra("weeksSaveStr",weeksSaveStr);
                startActivityForResult(intent, Constants.DATE_WEEK);
                break;
        }
    }

    //取一个30天内的时间范围进行显示
    private TimeRange getTimeRange() {
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.DAY_OF_YEAR,30);
        TimeRange timeRange = new TimeRange();
        timeRange.setStart_time(calendarStart.getTime());
        timeRange.setEnd_time(calendarEnd.getTime());
        return timeRange;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (requestCode){
                case Constants.DATE_WEEK:
                    String dateStr = null;
                    dateStr = data.getStringExtra("data");
                    weeks.clear();
                    weeksSaveStr = "";
                    weeksPreform = "";
                    if (!TextUtils.isEmpty(dateStr)){
                        weeks = gson.fromJson(dateStr,new TypeToken<List<Week>>(){}.getType());
                        if (weeks!=null&&weeks.size()>0){
                            String weekStr = "";
                            for (int i=0;i<weeks.size();i++){
                                if (weeks.get(i).isFlag()){
                                    weekStr +=weeks.get(i).getDate()+",";
                                    weeksSaveStr += (i+1)+",";
                                    weeksPreform +=" "+i;
                                }
                            }
                            weeksSaveStr = weeksSaveStr.substring(0,weeksSaveStr.length()-1);
                            weekStr = weekStr.substring(0,weekStr.length()-1);
                            tv_date_select.setText(weekStr);
                            weeksSaveStr.trim();
                            Log.d(TAG,weeksSaveStr);
                        }
                    }
                    break;
            }
        }
    }
}
