package com.nader.intelligent.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.IRefreshSessionCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.nader.intelligent.activity.main.MainActivity;
import com.nader.intelligent.R;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.util.SettingUtils;

import java.util.HashMap;

/**
 * 初始页
 * author:zhangpeng
 * date: 2019/11/7
 */
public class StartActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;
        initView();
//        initData();
    }

    public void initView(){
//        if (LoginBusiness.isLogin()){
//            String username = SettingUtils.get(mContext,"username");
//            String password = SettingUtils.get(mContext,"password");
//            HashMap<String,String> map = new HashMap<>();
//            map.put("username",username);
//            map.put("password",password);
//            login(map);
//        }else{
            Log.d(TAG,LoginBusiness.getLoginAdapter().getSessionData().toString());
            LoginBusiness.getLoginAdapter().login(new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    Log.d(TAG,"successLogin");

                    LoginBusiness.refreshSession(true, new IRefreshSessionCallback() {
                        @Override
                        public void onRefreshSuccess() {
                            IoTCredentialManageImpl ioTCredentialManage = IoTCredentialManageImpl.getInstance(getApplication());
                            IoTCredentialData ioTCredentialData = ioTCredentialManage.getIoTCredential();
                            Log.d(TAG,ioTCredentialData.toString());
                            goMain();
                        }

                        @Override
                        public void onRefreshFailed() {
                            linkToast(mContext,"登录失败");
                        }
                    });
                }

                @Override
                public void onLoginFailed(int i, String s) {

                }
            });
//        }
    }

    public void initData(){
        goMain();
    }

    private void goMain(){
        intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(HashMap<String,String> map){
        LoginBusiness.refreshSession(true, new IRefreshSessionCallback() {
            @Override
            public void onRefreshSuccess() {
                IoTCredentialManageImpl ioTCredentialManage = IoTCredentialManageImpl.getInstance(getApplication());
                IoTCredentialData ioTCredentialData = ioTCredentialManage.getIoTCredential();
                Log.d(TAG,ioTCredentialData.toString());
                goMain();
            }

            @Override
            public void onRefreshFailed() {
                linkToast(mContext,"登录失败");
            }
        });
    }
}
