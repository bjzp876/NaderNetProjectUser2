package com.nader.intelligent.adapter;

import android.content.Context;
import android.content.Intent;

import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.nader.intelligent.activity.user.LoginActivity;

import java.util.Map;

/**
 * author:zhangpeng
 * date: 2019/8/20
 */
public class MyOALoginAdapter extends OALoginAdapter {
    public String TAG ="Programme" ;
    private Context context;
    private ILoginCallback iLoginCallback;
    public MyOALoginAdapter(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public void login(ILoginCallback callback) {
        //打开三方登录页面
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
        iLoginCallback = callback;
        //当XXXActivity登录成功之后，调用
//        authLogin(SettingUtils.get(context,"token"),callback);
    }

    @Override
    public void login(ILoginCallback iLoginCallback, Map<String, String> map) {
        super.login(iLoginCallback, map);
        //打开三方登录页面
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
        //当XXXActivity登录成功之后，调用
//        authLogin(SettingUtils.get(context,"token"));
    }

    public ILoginCallback getILoginCallback(){
        if (iLoginCallback!=null){
            return iLoginCallback;
        }else{
            return null;
        }
    }

    public void authLogin(String authCode){
        //authCode 是三方登录页面返回的code
        ILoginCallback loginCallback = iLoginCallback;
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        service.authCodeLogin(context, authCode, new OALoginCallback(loginCallback));
    }
}
