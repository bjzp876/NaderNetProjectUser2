package com.nader.intelligent.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.nader.intelligent.R;
import com.nader.intelligent.activity.device.DeviceInfoActivity;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.DeviceInfo;
import com.nader.intelligent.entity.Scene;
import com.nader.intelligent.entity.SceneDetail;
import com.nader.intelligent.entity.Triggers;
import com.nader.intelligent.entity.Week;
import com.nader.intelligent.util.JSONUtil;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.view.MyListView;
import com.nader.intelligent.view.NameEditDialog;
import com.nader.intelligent.view.RoomTwoButtonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 场景详情页
 * author:zhangpeng
 * date: 2019/9/11
 */
public class SceneDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_right;
    private FrameLayout fl_right;
    private String title = "";
    private Scene scene;

    //场景图标
    private RelativeLayout rl_scene_icon;
    private ImageView iv_scene;

    //删除场景
    private Button bt_scene_del;
    private RoomTwoButtonDialog dialog;

    //如果
    private RelativeLayout rl_if;
    private MyListView rv_if;
    private CommonAdapter adapter_if;
    private List<Scene> if_list = new ArrayList<>();

    //并且
    private RelativeLayout rl_and;
    private MyListView rv_and;
    private CommonAdapter adapter_and;
    private List<Scene> and_list = new ArrayList<>();

    //如果
    private RelativeLayout rl_result;
    private MyListView rv_result;
    private CommonAdapter adapter_result;
    private List<Scene> result_list = new ArrayList<>();
    private final String DEVICE_STATE_IF = "if_device";
    //是否是刷新
    private String icon;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    linkToast(mContext, "创建成功");
                    intent = new Intent();
                    intent.putExtra("success", "success");
                    setResult(Constants.SUCCESS, intent);
                    finish();
                    break;
                case 1:
                    linkToast(mContext, "创建失败");
                    break;
                case 2:
                    adapter_if.notifyDataSetChanged(if_list);
                    adapter_and.notifyDataSetChanged(and_list);
                    adapter_result.notifyDataSetChanged(result_list);
                    break;
                case 3:
                    linkToast(mContext, "删除成功");
                    intent = new Intent();
                    intent.putExtra("success", "success");
                    setResult(Constants.SUCCESS, intent);
                    finish();
                    break;
                case 4:
                    linkToast(mContext, "更新成功");
                    intent = new Intent();
                    intent.putExtra("success", "success");
                    setResult(Constants.SUCCESS, intent);
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_scene_detail);
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        fl_right = findViewById(R.id.fl_right);
        tv_right = findViewById(R.id.tv_right);

        rl_scene_icon = findViewById(R.id.rl_scene_icon);
        iv_scene = findViewById(R.id.iv_scene);

        bt_scene_del = findViewById(R.id.bt_scene_del);

        rl_if = findViewById(R.id.rl_if);
        rv_if = findViewById(R.id.rv_if);

        rl_and = findViewById(R.id.rl_and);
        rv_and = findViewById(R.id.rv_and);

        rl_result = findViewById(R.id.rl_result);
        rv_result = findViewById(R.id.rv_result);

        adapter_if = new CommonAdapter(mContext, if_list, R.layout.item_scene_list) {

            @Override
            protected void convertView(View item, Object o) {
                Scene scene = (Scene) o;
                TextView name = item.findViewById(R.id.tv_scene_name);
                TextView description = item.findViewById(R.id.tv_scene_description);
                name.setText(scene.getName());
                if (TextUtils.isEmpty(scene.getCron())){
                    description.setText(scene.getDescription());
                }else{
                    String[] cronStr = new String[]{};
                    cronStr = scene.getCron().split(" ");
                    Log.d(TAG,cronStr.toString());
                    String time = cronStr[1]+":"+cronStr[0]+" "+cronStr[cronStr.length-1];
                    description.setText(time);
                }
                View line = item.findViewById(R.id.line);
                View line_end = item.findViewById(R.id.line_end);
                int num = 0;
                for (int i = 0; i < if_list.size(); i++) {
                    if (if_list.get(i).equals(scene)) {
                        num = i;
                    }
                }
                if (num == if_list.size() - 1) {
                    line.setVisibility(View.GONE);
                    line_end.setVisibility(View.VISIBLE);
                } else {
                    line.setVisibility(View.VISIBLE);
                    line_end.setVisibility(View.GONE);
                }
            }
        };

        adapter_and = new CommonAdapter(mContext, if_list, R.layout.item_scene_list) {

            @Override
            protected void convertView(View item, Object o) {
                Scene scene = (Scene) o;
                TextView name = item.findViewById(R.id.tv_scene_name);
                TextView description = item.findViewById(R.id.tv_scene_description);
                name.setText(scene.getName());
                description.setText(scene.getDescription());
                View line = item.findViewById(R.id.line);
                View line_end = item.findViewById(R.id.line_end);
                int num = 0;
                for (int i = 0; i < and_list.size(); i++) {
                    if (and_list.get(i).equals(scene)) {
                        num = i;
                    }
                }
                if (num == and_list.size() - 1) {
                    line.setVisibility(View.GONE);
                    line_end.setVisibility(View.VISIBLE);
                } else {
                    line.setVisibility(View.VISIBLE);
                    line_end.setVisibility(View.GONE);
                }
            }
        };

        adapter_result = new CommonAdapter(mContext, if_list, R.layout.item_scene_list) {

            @Override
            protected void convertView(View item, Object o) {
                Scene scene = (Scene) o;
                TextView name = item.findViewById(R.id.tv_scene_name);
                TextView description = item.findViewById(R.id.tv_scene_description);
                name.setText(scene.getName());
                description.setText(scene.getDescription());
                View line = item.findViewById(R.id.line);
                View line_end = item.findViewById(R.id.line_end);
                int num = 0;
                for (int i = 0; i < result_list.size(); i++) {
                    if (result_list.get(i).equals(scene)) {
                        num = i;
                    }
                }
                if (num == result_list.size() - 1) {
                    line.setVisibility(View.GONE);
                    line_end.setVisibility(View.VISIBLE);
                } else {
                    line.setVisibility(View.VISIBLE);
                    line_end.setVisibility(View.GONE);
                }
            }
        };

        rv_if.setAdapter(adapter_if);
        rv_and.setAdapter(adapter_and);
        rv_result.setAdapter(adapter_result);
    }

    @Override
    public void initData() {
        super.initData();
        scene = (Scene) getIntent().getSerializableExtra("scene");
        tv_right.setText("保存");
        tv_right.setTextColor(getResources().getColor(R.color.text_green));

        adapter_if.notifyDataSetChanged();
        adapter_and.notifyDataSetChanged();
        adapter_result.notifyDataSetChanged();
        fl_right.setVisibility(View.VISIBLE);

        if (scene != null) {
            icon = scene.getIcon();
            title = scene.getName();
            getSceneDetail(scene.getSceneId());
            bt_scene_del.setVisibility(View.VISIBLE);
        } else {
            title = "新建场景";
        }
        tv_title.setText(title);
        dialog = new RoomTwoButtonDialog(this);
        dialog.setMessage("你确定要删除该场景吗?");

        rv_if.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = if_list.get(position);
                if (TextUtils.isEmpty(scene.getCron())){
                    intent = new Intent(mContext, SceneDeviceStateActivity.class);
                    if (scene.getWeek() != null) {
                        intent.putExtra("iotId", scene.getIotId());
                        intent.putExtra("nickName", scene.getName());
                        intent.putExtra("stateNum", 1);
                        intent.putExtra("position", position);
                        intent.putExtra("productKey", scene.getWeek().getProductKey());
                        if (scene.getDeviceInfo() != null) {
                            intent.putExtra("deviceInfo", scene.getDeviceInfo());
                        }
                        startActivity(intent);
                    }
                }else{
                    intent = new Intent(mContext, SceneTimeSelectActivity.class);
                    intent.putExtra("stateNum", 1);
                    intent.putExtra("position", position);
                    intent.putExtra("scene",scene);
                    startActivity(intent);
                }

            }
        });

        rv_and.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = and_list.get(position);
                if (TextUtils.isEmpty(scene.getCron())){
                    intent = new Intent(mContext, SceneDeviceStateActivity.class);
                    if (scene.getWeek() != null) {
                        intent.putExtra("iotId", scene.getIotId());
                        intent.putExtra("nickName", scene.getName());
                        intent.putExtra("stateNum", 2);
                        intent.putExtra("position", position);
                        intent.putExtra("productKey", scene.getWeek().getProductKey());
                        if (scene.getDeviceInfo() != null) {
                            intent.putExtra("deviceInfo", scene.getDeviceInfo());
                        }
                        startActivity(intent);
                    }
                }else{
                    intent = new Intent(mContext, SceneChangeTimeActivity.class);
                    intent.putExtra("stateNum", 2);
                    intent.putExtra("position",position);
                    intent.putExtra("scene",scene);
                    startActivity(intent);
                }

            }
        });


        rv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = result_list.get(position);
                intent = new Intent(mContext, SceneDeviceStateActivity.class);
                if (scene.getWeek() != null) {
                    intent.putExtra("iotId", scene.getIotId());
                    intent.putExtra("nickName", scene.getName());
                    intent.putExtra("stateNum", 3);
                    intent.putExtra("position", position);
                    intent.putExtra("productKey", scene.getWeek().getProductKey());
                    if (scene.getDeviceInfo() != null) {
                        intent.putExtra("deviceInfo", scene.getDeviceInfo());
                    }
                    startActivity(intent);
                }
            }
        });

        rv_if.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.show();
                dialog.setRoomTwoButtonDialogDetermineClick(new RoomTwoButtonDialog.RoomTwoButtonDialoClick() {
                    @Override
                    public void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog) {
                        dialog.dismiss();
                        if_list.remove(position);
                        adapter_if.notifyDataSetChanged(if_list);
                    }
                });
                return false;

            }
        });

        rv_and.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.show();
                dialog.setRoomTwoButtonDialogDetermineClick(new RoomTwoButtonDialog.RoomTwoButtonDialoClick() {
                    @Override
                    public void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog) {
                        dialog.dismiss();
                        and_list.remove(position);
                        adapter_and.notifyDataSetChanged(and_list);
                    }
                });
                return false;

            }
        });

        rv_result.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog.show();
                dialog.setRoomTwoButtonDialogDetermineClick(new RoomTwoButtonDialog.RoomTwoButtonDialoClick() {
                    @Override
                    public void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog) {
                        dialog.dismiss();
                        result_list.remove(position);
                        adapter_result.notifyDataSetChanged(result_list);
                    }
                });
                return false;

            }
        });
    }


    @Override
    public void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        bt_scene_del.setOnClickListener(this);
        rl_if.setOnClickListener(this);
        rl_and.setOnClickListener(this);
        rl_result.setOnClickListener(this);
        rl_scene_icon.setOnClickListener(this);
        fl_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //名称编辑
                final NameEditDialog editDialog = new NameEditDialog(mContext);
                editDialog.setTitle("请输入场景昵称");
                if (scene!=null){
                    editDialog.setEditorText(scene.getName());
                }
                editDialog.setCancleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
                editDialog.setSureListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                        if (TextUtils.isEmpty(editDialog.getText().toString().trim())) {
                            linkToast(mContext, "请输入场景名称");
                            return;
                        }
                        if (scene != null) {
                            //更新场景
                            if (!scene.getName().equals(editDialog.getText().toString().trim())){
                                updateSceneByName(editDialog.getText().toString().trim());
                            }else {
                                updateSceneByTCA();
                            }
                        } else {
                            //新建场景
                            createScene(editDialog.getText().toString().trim());
                        }
                    }
                });
                editDialog.show();
                break;
            case R.id.bt_scene_del:
                dialog.show();
                dialog.setRoomTwoButtonDialogDetermineClick(new RoomTwoButtonDialog.RoomTwoButtonDialoClick() {
                    @Override
                    public void onRoomTwoButtonDialoClick(RoomTwoButtonDialog twoButtonDialog) {
                        dialog.dismiss();
                        delete();
                    }
                });
                break;
            case R.id.rl_if:
                intent = new Intent(mContext, SceneFactorActivity.class);
                intent.putExtra("stateNum", 1);
                startActivity(intent);
                break;
            case R.id.rl_and:
                intent = new Intent(mContext, SceneFactorActivity.class);
                intent.putExtra("stateNum", 2);
                startActivity(intent);
                break;
            case R.id.rl_result:
                intent = new Intent(mContext, SceneActionSelectActivity.class);
                intent.putExtra("stateNum", 3);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        setIntent(intent);
        String data = intent.getStringExtra("data");
        String nickName = intent.getStringExtra("nickName");
        String deviceName = intent.getStringExtra("deviceName");
        String weekStr = (String) getIntent().getSerializableExtra("week");
        String productKey = intent.getStringExtra("productKey");
        String cron = intent.getStringExtra("cron");
        String description = getIntent().getStringExtra("description");
        String beginDate = getIntent().getStringExtra("beginDate");
        String endDate = getIntent().getStringExtra("endDate");
        String repeat = getIntent().getStringExtra("repeat");
        int position = getIntent().getIntExtra("position", -1);
        int stateNum = intent.getIntExtra("stateNum", 1);
        DeviceInfo deviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");
        Scene scene = new Scene();
        if (!TextUtils.isEmpty(productKey)) {
            Week week = gson.fromJson(weekStr, Week.class);
            week.setProductKey(productKey);
            //增添
            if (position == -1) {
                scene.setDescription(data);
                scene.setName(nickName);
                scene.setIotId(week.getIotId());
                scene.setWeek(week);
                if (deviceInfo != null) {
                    scene.setDeviceInfo(deviceInfo);
                }
                switch (stateNum) {
                    case 1:
                        if_list.add(scene);
                        break;
                    case 2:
                        and_list.add(scene);
                        break;
                    case 3:
                        result_list.add(scene);
                        break;
                }
            }//修改
            else {
                switch (stateNum) {
                    case 1:
                        if_list.get(position).setWeek(week);
                        if_list.get(position).setDescription(data);
                        break;
                    case 2:
                        and_list.get(position).setWeek(week);
                        and_list.get(position).setDescription(data);
                        break;
                    case 3:
                        result_list.get(position).setWeek(week);
                        result_list.get(position).setDescription(data);
                        break;
                }
            }

        } else if (!TextUtils.isEmpty(cron)) {
            if (position == -1) {
                scene.setCron(cron);
                scene.setName("执行时间");
                scene.setDescription(description);
                if (!TextUtils.isEmpty(beginDate)) {
                    Week week = new Week();
                    week.setBeginDate(beginDate);
                    week.setEndDate(endDate);
                    week.setRepeat(repeat);
                    scene.setWeek(week);
                }
                switch (stateNum) {
                    case 1:
                        if_list.add(scene);
                        break;
                    case 2:
                        and_list.add(scene);
                        break;
                    case 3:
                        result_list.add(scene);
                        break;
                }
            } else {
                scene.setCron(cron);
                scene.setDescription(description);
                scene.setName("执行时间");
                Week week = new Week();
                if (!TextUtils.isEmpty(beginDate)) {
                    week.setBeginDate(beginDate);
                    week.setEndDate(endDate);
                    week.setRepeat(repeat);
                    scene.setWeek(week);
                }
                switch (stateNum) {
                    case 1:
                        if_list.get(position).setCron(cron);
                        if_list.get(position).setWeek(week);
                        if_list.get(position).setDescription(data);
                        break;
                    case 2:
                        and_list.get(position).setCron(description);
                        and_list.get(position).setWeek(week);
                        and_list.get(position).setDescription(description);
                        break;
                    case 3:
                        result_list.get(position).setWeek(week);
                        result_list.get(position).setDescription(data);
                        break;
                }
            }

        }
        //刷新
        switch (stateNum) {
            case 1:
                adapter_if.notifyDataSetChanged(if_list);
                break;
            case 2:
                adapter_and.notifyDataSetChanged(and_list);
                break;
            case 3:
                adapter_result.notifyDataSetChanged(result_list);
                break;
        }


        linkToast(mContext, data);
    }


    /**
     * 创建场景
     */
    private void createScene(String sceneName) {
        final Map<String, Object> map = new HashMap<>();
        map.put("name", sceneName);
        map.put("icon", "https://g.aliplus.com/scene_icons/scene_icon.pngpng");
        map.put("enable", false);

        if (if_list != null && if_list.size() > 0) {
            List<Triggers> triggersList = new ArrayList<>();
            for (int i = 0; i < if_list.size(); i++) {
                Triggers triggers = new Triggers();
                if (if_list.get(i).getWeek() == null) {
                    triggers.setUri("trigger/timer");
                    Triggers.ParamsBean paramsBean = new Triggers.ParamsBean();
                    paramsBean.setCron(if_list.get(i).getCron());
                    paramsBean.setCronType("linux");
                    triggers.setParams(paramsBean);
                    triggersList.add(triggers);

                } else {
                    if (if_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || if_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                        triggers.setUri("trigger/device/event");
                    } else {
                        triggers.setUri("trigger/device/property");
                    }
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setIotId(if_list.get(i).getIotId());
                    trigger.setPropertyName(if_list.get(i).getDeviceInfo().getIdentifier());
                    trigger.setCompareType("==");
                    trigger.setCompareValue(if_list.get(i).getWeek().getKey());
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                }

            }
            map.put("triggers", triggersList);
        }

        if (and_list != null && and_list.size() > 0) {
            List<Triggers> triggersList = new ArrayList<>();
            for (int i = 0; i < and_list.size(); i++) {
                Triggers triggers = new Triggers();
                if (TextUtils.isEmpty(and_list.get(i).getWeek().getProductKey())) {
                    triggers.setUri("condition/timeRange");
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setBeginDate(and_list.get(i).getWeek().getBeginDate());
                    trigger.setEndDate(and_list.get(i).getWeek().getEndDate());
                    trigger.setFormat(and_list.get(i).getWeek().getFormat());
                    if (!TextUtils.isEmpty(and_list.get(i).getWeek().getRepeat())) {
                        trigger.setRepeat(and_list.get(i).getWeek().getRepeat());
                    }
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                } else {
                    if (and_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || and_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                        triggers.setUri("condition/device/event");
                    } else {
                        triggers.setUri("condition/device/property");
                    }
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setIotId(and_list.get(i).getIotId());
                    trigger.setPropertyName(and_list.get(i).getDeviceInfo().getIdentifier());
                    trigger.setCompareType("==");
                    trigger.setCompareValue(and_list.get(i).getWeek().getKey());
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                }

            }
            map.put("conditions", triggersList);
        }
        if (result_list != null && result_list.size() > 0) {
            List<Triggers> actionList = new ArrayList<>();
            for (int i = 0; i < result_list.size(); i++) {
                Triggers action = new Triggers();
                if (result_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || result_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                    action.setUri("action/device/setProperty");
                } else {
                    action.setUri("action/device/setProperty");
                }
                Triggers.ParamsBean<Map<String, Object>> actionsBean = new Triggers.ParamsBean<>();
                actionsBean.setIotId(result_list.get(i).getIotId());
                Map<String, Object> propertyItems = new HashMap<>();
                propertyItems.put(result_list.get(i).getWeek().getIdentifier(), result_list.get(i).getWeek().getKey());
                actionsBean.setPropertyItems(propertyItems);
                action.setParams(actionsBean);
                actionList.add(action);
                map.put("actions", actionList);
            }
        } else {
            linkToast(mContext, "请选择执行结果");
            return;
        }
        if (if_list.size() <= 0 && and_list.size() <= 0) {
            linkToast(mContext, "请选择执行条件");
            return;
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                apiConfig.createScene(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        String sceneId = (String) baseResponse.data;
                        deployScene(sceneId);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                            mHandler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        };

        thread.start();

    }

    /**
     * 部署场景
     *
     * @param sceneId
     */
    private void deployScene(final String sceneId) {
        Map<String, Object> map = new HashMap<>();
        map.put("sceneId", sceneId);
        apiConfig.deployScene(getToken(mContext), map).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    Log.d(TAG, responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bindScene(sceneId);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mHandler.sendEmptyMessage(1);
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                try {
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param sceneId
     */
    private void bindScene(String sceneId) {
        map = new HashMap<>();
        map.put("targetId", "57b917c100f911ea915600163e0406b6");
        map.put("sceneId", sceneId);
        apiConfig.bindScene(getToken(mContext), map).subscribe(new Action1<BaseResponse>() {
            @Override
            public void call(BaseResponse baseResponse) {
                if (baseResponse.code == 200) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mHandler.sendEmptyMessage(1);
            }
        });

    }

    /**
     * 获取场景详情
     *
     * @param sceneId
     */
    private void getSceneDetail(String sceneId) {
        map = new HashMap<>();
        map.put("sceneId", sceneId);
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                apiConfig.getSceneDetail(getToken(mContext), map).subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseResponse) {
                        String json = gson.toJson(baseResponse.data);
                        Log.d(TAG, json);
                        SceneDetail sceneDetail = gson.fromJson(json, SceneDetail.class);
                        if_list.clear();
                        and_list.clear();
                        result_list.clear();
                        //如果条件解析
                        if (sceneDetail.getTriggers() != null && sceneDetail.getTriggers().size() > 0) {
                            for (int i = 0; i < sceneDetail.getTriggers().size(); i++) {
                                final String uri = sceneDetail.getTriggers().get(i).getUri();
                                final Scene scene = JSONUtil.getSceneInfoByProperty(uri, sceneDetail.getTriggers().get(i));
                                //设备
                                if(uri.contains("property")){
                                    Map<String, Object> map = new HashMap<>();
                                    final String iotId = sceneDetail.getTriggers().get(i).getParams().getIotId();
                                    scene.setIotId(iotId);
                                    int compareValue = 0;
                                    final String compareType;
                                    compareValue = (int) Double.parseDouble(sceneDetail.getTriggers().get(i).getParams().getCompareValue());
                                    map.put("iotId", iotId);
                                    final int finalCompareValue = compareValue;
                                    apiConfig.getDeviceStateByScene(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<ResponseBody>() {
                                        @Override
                                        public void call(ResponseBody responseBody) {
                                            JSONObject result = null;
                                            try {
                                                String json = responseBody.string();
                                                result = new JSONObject(json);
                                                int code = result.getInt("code");
                                                if (code != 200) {
                                                    linkToast(mContext, "获取失败");
                                                    return;
                                                }
                                                result = result.getJSONObject("data");
                                                JSONArray jsonArray = null;
                                                if (uri.contains("property")) {
                                                    jsonArray = result.getJSONArray("properties");
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    if (jsonObject != null) {
                                                        String identifier = jsonObject.getString("identifier");
                                                        String name = jsonObject.getString("name");
                                                        JSONObject dataType = jsonObject.getJSONObject("dataType");
                                                        dataType = dataType.getJSONObject("specs");
                                                        String specs = dataType.getString(String.valueOf(finalCompareValue));
                                                        String description = name + "_" + specs;
                                                        DeviceInfo deviceInfo = new DeviceInfo();
                                                        deviceInfo.setAbilityType("PROPERTY");
                                                        deviceInfo.setCategoryType("IRDetector");
                                                        deviceInfo.setIdentifier(identifier);
                                                        deviceInfo.setName(name);
                                                        deviceInfo.setCompareValue(finalCompareValue);
                                                        scene.setDescription(description);
                                                        scene.setDeviceInfo(deviceInfo);
                                                    }
                                                }
                                                if_list.add(scene);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
                                }//时间
                                else if(uri.contains("time")){
                                        String cron = sceneDetail.getTriggers().get(i).getParams().getCron();
                                        if (TextUtils.isEmpty(cron)){
                                            linkToast(mContext,"加载错误");
                                            return;
                                        }
                                        scene.setDescription(cron);
                                        scene.setCron(cron);
                                        if_list.add(scene);

                                }

                            }

                        }
                        if (sceneDetail.getConditions() != null && sceneDetail.getConditions().size() > 0) {
                            for (int i = 0; i < sceneDetail.getConditions().size(); i++) {
                                Map<String, Object> map = new HashMap<>();
                                final String iotId = sceneDetail.getConditions().get(i).getParams().getIotId();
                                final String uri = sceneDetail.getConditions().get(i).getUri();
                                final Scene scene = JSONUtil.getSceneInfoByProperty(uri, sceneDetail.getConditions().get(i));
                                scene.setIotId(iotId);
                                int compareValue = 0;
                                final String compareType;
                                map.put("iotId", iotId);
                                if (uri.contains("property")){
                                    compareValue = (int)Double.parseDouble(sceneDetail.getConditions().get(i).getParams().getCompareValue());
                                    final int finalCompareValue = compareValue;
                                    apiConfig.getDeviceStateByScene(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<ResponseBody>() {
                                        @Override
                                        public void call(ResponseBody responseBody) {
                                            JSONObject result = null;
                                            try {
                                                String json = responseBody.string();
                                                result = new JSONObject(json);
                                                int code = result.getInt("code");
                                                if (code != 200) {
                                                    linkToast(mContext, "获取失败");
                                                    return;
                                                }
                                                result = result.getJSONObject("data");
                                                JSONArray jsonArray = null;
                                                if (uri.contains("property")) {
                                                    jsonArray = result.getJSONArray("properties");
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    if (jsonObject != null) {
                                                        String identifier = jsonObject.getString("identifier");
                                                        String name = jsonObject.getString("name");
                                                        JSONObject dataType = jsonObject.getJSONObject("dataType");
                                                        dataType = dataType.getJSONObject("specs");
                                                        String specs = dataType.getString(String.valueOf(finalCompareValue));
                                                        String description = name + "_" + specs;
                                                        DeviceInfo deviceInfo = new DeviceInfo();
                                                        deviceInfo.setAbilityType("PROPERTY");
                                                        deviceInfo.setCategoryType("IRDetector");
                                                        deviceInfo.setIdentifier(identifier);
                                                        deviceInfo.setName(name);
                                                        deviceInfo.setCompareValue(finalCompareValue);
                                                        scene.setDescription(description);
                                                        scene.setDeviceInfo(deviceInfo);
                                                    }
                                                }
                                                and_list.add(scene);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
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
                                }else if (uri.contains("time")){
                                    String cron = "";
                                    cron = sceneDetail.getConditions().get(i).getParams().getBeginDate()+" "+sceneDetail.getConditions().get(i).getParams().getEndDate()+" "+sceneDetail.getConditions().get(i).getParams().getRepeat();
                                    scene.setDescription(cron);
                                    scene.setCron(cron);
                                    Week week = new Week();
                                    week.setBeginDate(sceneDetail.getConditions().get(i).getParams().getBeginDate());
                                    week.setEndDate(sceneDetail.getConditions().get(i).getParams().getEndDate());
                                    week.setRepeat(sceneDetail.getConditions().get(i).getParams().getRepeat());
                                    scene.setWeek(week);
                                    and_list.add(scene);
                                }

                            }
                        }

                        //执行
                        if (sceneDetail.getActions() != null && sceneDetail.getActions().size() > 0) {
                            for (int i = 0; i < sceneDetail.getActions().size(); i++) {
                                final String uri = sceneDetail.getActions().get(i).getUri();
                                if (uri.contains("setProperty")) {
                                    final Scene scene = JSONUtil.getSceneInfoByProperty(uri, sceneDetail.getActions().get(i));
                                    String iotId = sceneDetail.getActions().get(i).getParams().getIotId();
                                    scene.setIotId(iotId);
                                    String propertyItemsStr = String.valueOf(sceneDetail.getActions().get(i).getParams().getPropertyItems());
                                    try {
                                        JSONObject propertyItems = new JSONObject(propertyItemsStr);
                                        Iterator<String> it = propertyItems.keys();
                                        String identifier = null;
                                        int compareValue = 0;
                                        while (it.hasNext()) {
                                            identifier = it.next();
                                            compareValue = propertyItems.getInt(identifier);
                                        }
                                        if (TextUtils.isEmpty(identifier)) {
                                            linkToast(mContext, "获取错误");
                                            return;
                                        }
                                        scene.getWeek().setIdentifier(identifier);
                                        scene.getWeek().setKey(String.valueOf(compareValue));
                                        final int finalCompareValue = compareValue;
                                        map = new HashMap<>();
                                        map.put("iotId", iotId);
                                        final String finalIdentifier = identifier;
                                        apiConfig.getDeviceStateByScene(SettingUtils.get(mContext, "token"), map).subscribe(new Action1<ResponseBody>() {
                                            @Override
                                            public void call(ResponseBody responseBody) {
                                                JSONObject result = null;
                                                try {
                                                    String json = responseBody.string();
                                                    result = new JSONObject(json);
                                                    int code = result.getInt("code");
                                                    if (code != 200) {
                                                        linkToast(mContext, "获取失败");
                                                        return;
                                                    }
                                                    result = result.getJSONObject("data");
                                                    JSONArray jsonArray = null;
                                                    if (uri.contains("setProperty")) {
                                                        jsonArray = result.getJSONArray("properties");
                                                        int num = 0;
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                            if (jsonObject.getString("identifier").equals(finalIdentifier)) {
                                                                num = i;
                                                                break;
                                                            }
                                                        }
                                                        JSONObject jsonObject = jsonArray.getJSONObject(num);
                                                        if (jsonObject != null) {
                                                            String identifier = jsonObject.getString("identifier");
                                                            String name = jsonObject.getString("name");
                                                            JSONObject dataType = jsonObject.getJSONObject("dataType");
                                                            dataType = dataType.getJSONObject("specs");
                                                            String specs = dataType.getString(String.valueOf(finalCompareValue));
                                                            String description = name + "_" + specs;
                                                            DeviceInfo deviceInfo = new DeviceInfo();
                                                            deviceInfo.setAbilityType("PROPERTY");
                                                            deviceInfo.setCategoryType("IRDetector");
                                                            deviceInfo.setIdentifier(identifier);
                                                            deviceInfo.setName(name);
                                                            deviceInfo.setCompareValue(finalCompareValue);
                                                            scene.setDescription(description);
                                                            scene.setDeviceInfo(deviceInfo);
                                                        }
                                                    }
                                                    result_list.add(scene);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        mHandler.sendEmptyMessage(2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();
    }

    /**
     * 删除场景
     */
    private void delete() {
        map = new HashMap<>();
        map.put("sceneId", scene.getSceneId());
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                apiConfig.delScene(getToken(mContext), map).subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        mHandler.sendEmptyMessage(3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        linkToast(mContext, "删除失败");
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                            Log.d(TAG, responseBody.string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();
    }


    /**
     * 更新TCA场景
     */
    private void updateSceneByTCA() {
        map = new HashMap<>();
        map.put("sceneId",scene.getSceneId());
        if (if_list != null && if_list.size() > 0) {
            List<Triggers> triggersList = new ArrayList<>();
            for (int i = 0; i < if_list.size(); i++) {
                Triggers triggers = new Triggers();
                if (!TextUtils.isEmpty(if_list.get(i).getCron())) {
                    triggers.setUri("trigger/timer");
                    Triggers.ParamsBean paramsBean = new Triggers.ParamsBean();
                    paramsBean.setCron(if_list.get(i).getCron());
                    paramsBean.setCronType("linux");
                    triggers.setParams(paramsBean);
                    triggersList.add(triggers);

                } else {
                    if (if_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || if_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                        triggers.setUri("trigger/device/event");
                    } else {
                        triggers.setUri("trigger/device/property");
                    }
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setIotId(if_list.get(i).getIotId());
                    trigger.setPropertyName(if_list.get(i).getDeviceInfo().getIdentifier());
                    trigger.setCompareType("==");
                    trigger.setCompareValue(if_list.get(i).getWeek().getKey());
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                }

            }
            map.put("triggers", triggersList);
        }

        if (and_list != null && and_list.size() > 0) {
            List<Triggers> triggersList = new ArrayList<>();
            for (int i = 0; i < and_list.size(); i++) {
                Triggers triggers = new Triggers();
                if (!TextUtils.isEmpty(and_list.get(i).getCron())) {
                    triggers.setUri("condition/timeRange");
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setBeginDate(and_list.get(i).getWeek().getBeginDate());
                    trigger.setEndDate(and_list.get(i).getWeek().getEndDate());
                    trigger.setFormat(and_list.get(i).getWeek().getFormat());
                    trigger.setTimeZoneID("Asia/Shanghai");
                    if (!TextUtils.isEmpty(and_list.get(i).getWeek().getRepeat())) {
                        trigger.setRepeat(and_list.get(i).getWeek().getRepeat());
                    }
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                } else {
                    if (and_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || and_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                        triggers.setUri("condition/device/event");
                    } else {
                        triggers.setUri("condition/device/property");
                    }
                    Triggers.ParamsBean trigger = new Triggers.ParamsBean();
                    trigger.setIotId(and_list.get(i).getIotId());
                    trigger.setPropertyName(and_list.get(i).getDeviceInfo().getIdentifier());
                    trigger.setCompareType("==");
                    trigger.setCompareValue(and_list.get(i).getWeek().getKey());
                    triggers.setParams(trigger);
                    triggersList.add(triggers);
                }

            }
            map.put("conditions", triggersList);
        }
        if (result_list != null && result_list.size() > 0) {
            List<Triggers> actionList = new ArrayList<>();
            for (int i = 0; i < result_list.size(); i++) {
                Triggers action = new Triggers();
                if (result_list.get(i).getWeek().getProductKey().equals(Constants.EMERGENCY_BUTTON) || result_list.get(i).getWeek().getProductKey().equals(Constants.DOORBELL)) {
                    action.setUri("action/device/setProperty");
                } else {
                    action.setUri("action/device/setProperty");
                }
                Triggers.ParamsBean<Map<String, Object>> actionsBean = new Triggers.ParamsBean<>();
                actionsBean.setIotId(result_list.get(i).getIotId());
                Map<String, Object> propertyItems = new HashMap<>();
                propertyItems.put(result_list.get(i).getWeek().getIdentifier(), result_list.get(i).getWeek().getKey());
                actionsBean.setPropertyItems(propertyItems);
                action.setParams(actionsBean);
                actionList.add(action);
                map.put("actions", actionList);
            }
        } else {
            linkToast(mContext, "请选择执行结果");
            return;
        }
        if (if_list.size() <= 0 && and_list.size() <= 0) {
            linkToast(mContext, "请选择执行条件");
            return;
        }
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                apiConfig.updateSceneByTCA(getToken(mContext),map).subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        mHandler.sendEmptyMessage(4);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        linkToast(mContext, "更新失败");
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                            Log.d(TAG, responseBody.string());
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();
    }

    /**
     * 更新场景名称
     * @param sceneName
     */
    private void updateSceneByName(String sceneName){
        map = new HashMap<>();
        map.put("sceneId",scene.getSceneId());
        map.put("name",sceneName);
        map.put("icon",icon);
        map.put("description","new description");
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                apiConfig.updateSceneByName(getToken(mContext),map).subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        updateSceneByTCA();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        linkToast(mContext, "更新失败");
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        try {
                            BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                            Log.d(TAG, responseBody.string());
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();

    }
}
