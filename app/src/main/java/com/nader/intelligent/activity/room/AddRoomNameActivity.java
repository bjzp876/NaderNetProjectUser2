package com.nader.intelligent.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliyun.iot.homelink.si.component.TopBar;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.google.gson.Gson;
import com.nader.intelligent.R;
import com.nader.intelligent.activity.main.MainActivity;
import com.nader.intelligent.api.Constants;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.House;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.BaseResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * 增添房间名称页
 * author:zhangpeng
 * date: 2019/11/14
 */
public class AddRoomNameActivity extends BaseActivity implements View.OnClickListener {
    //标题
    private TopBar page_index_common_add_new_room_name_topbar;
    //输入框
    private EditText page_index_common_add_new_room_name_input_et;
    private String houseStr;
    private House house;
    //消除按钮
    private ImageView page_index_common_add_new_room_name_del_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_name);
        mContext = this;
        initView();
        initListener();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        page_index_common_add_new_room_name_topbar = findViewById(R.id.page_index_common_add_new_room_name_topbar);
        page_index_common_add_new_room_name_input_et = findViewById(R.id.page_index_common_add_new_room_name_input_et);
        page_index_common_add_new_room_name_del_btn = findViewById(R.id.page_index_common_add_new_room_name_del_btn);
    }

    @Override
    public void initListener() {
        super.initListener();
        page_index_common_add_new_room_name_topbar.setRightTextViewText(mContext.getResources().getString(R.string.page_index_complete));
        page_index_common_add_new_room_name_del_btn.setOnClickListener(this);
        page_index_common_add_new_room_name_topbar.setOnLeftClickListener(new TopBar.OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        page_index_common_add_new_room_name_input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    page_index_common_add_new_room_name_topbar.setRightTextViewEnabled(false);
                }else {
                    page_index_common_add_new_room_name_topbar.setRightTextViewEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        page_index_common_add_new_room_name_topbar.setOnRightClickListener(new TopBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                if (!TextUtils.isEmpty(page_index_common_add_new_room_name_input_et.getText().toString().trim())&&!page_index_common_add_new_room_name_input_et.getText().toString().trim().equals("")){
                    if (page_index_common_add_new_room_name_input_et.getText().toString().length()<=8){
                        onNext();
                    }else{
                        LinkToast.makeText(mContext,"房间名称不得超过8个字符", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    LinkToast.makeText(mContext,"请输入合法字符", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        houseStr = SettingUtils.get(mContext,"house");
        house = new House();
        house = gson.fromJson(houseStr,House.class);
        page_index_common_add_new_room_name_topbar.setRightTextViewEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.page_index_common_add_new_room_name_del_btn:
                if(page_index_common_add_new_room_name_input_et.getText().toString().length()>0){
                    page_index_common_add_new_room_name_input_et.setText("");
                    page_index_common_add_new_room_name_topbar.setRightTextViewEnabled(false);
                }
                break;
        }
    }

    private void onNext(){
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        params.put("name",page_index_common_add_new_room_name_input_et.getText().toString().trim());
        params.put("typeCode","room");
        params.put("rootSpaceId",house.getId());
        params.put("parentId",house.getId());
        params.put("description","123");

        apiConfig.createRoom(SettingUtils.get(mContext,"token"),params).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    Log.d(TAG,responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent = new Intent();
                intent.putExtra("data","success");
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
