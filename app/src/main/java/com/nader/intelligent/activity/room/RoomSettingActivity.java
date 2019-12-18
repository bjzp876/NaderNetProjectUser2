package com.nader.intelligent.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.aliyun.iot.link.ui.component.LinkAlertDialog;
import com.nader.intelligent.R;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 房间设置页
 * author:zhangpeng
 * date: 2019/11/15
 */
public class RoomSettingActivity extends BaseActivity implements View.OnClickListener {

    //删除
    private TextView page_index_room_setting_del_tv;
    private House room;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setting);
        mContext = this;
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        page_index_room_setting_del_tv = findViewById(R.id.page_index_room_setting_del_tv);
    }

    @Override
    public void initListener() {
        super.initListener();
        page_index_room_setting_del_tv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        room = (House) getIntent().getSerializableExtra("room");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.page_index_room_setting_del_tv:
                new LinkAlertDialog.Builder(mContext).setTitle(mContext.getString(R.string.page_index_main_aert_dialog_title))
                        .setMessage(mContext.getString(R.string.page_index_room_setting_del_room_message))
                        .setPositiveButton(mContext.getString(R.string.page_index_determine_st), new LinkAlertDialog.OnClickListener() {
                            @Override
                            public void onClick(LinkAlertDialog linkAlertDialog) {
                                linkAlertDialog.dismiss();
                                delRoom();
                            }
                        }).setNegativeButton(mContext.getString(R.string.page_index_cancel_st), new LinkAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(LinkAlertDialog linkAlertDialog) {
                        linkAlertDialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }


    /**
     * 删除房间
     */
    private void delRoom(){
        Map<String,Object> map = new HashMap<>();
        map.put("spaceId",room.getId());
        apiConfig.deleteRoom(SettingUtils.get(mContext,"token"),map).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                intent = new Intent();
                intent.putExtra("success","success");
                setResult(Constants.SUCCESS,intent);
                finish();
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
}
