package com.nader.intelligent.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.nader.intelligent.R;
import com.nader.intelligent.adapter.MyOALoginAdapter;
import com.nader.intelligent.base.BaseActivity;
import com.nader.intelligent.entity.Login;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.http.Fault;
import com.nader.intelligent.view.MyClearEditText;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 登录页
 * author:zhangpeng
 * date: 2019/8/2
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    //账号
    private MyClearEditText et_account;
    //密码
    private MyClearEditText et_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        findViewById(R.id.bt_login).setOnClickListener(this);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                HashMap<String,String> map = new HashMap<>();
                map.put("username",et_account.getText().toString().trim());
                map.put("password",et_password.getText().toString().trim());
                Subscription subscription = apiConfig.login(map).subscribe(new Action1<Login>() {

                    @Override
                    public void call(Login login) {
                        if (login.getCode()!=200){
                            linkToast(mContext,login.getMsg());
                            return;
                        }
                        String token = login.getData().getToken();
                        SettingUtils.set(mContext,"token",token);
                        SettingUtils.set(mContext,"username",et_account.getText().toString().trim());
                        SettingUtils.set(mContext,"password",et_password.getText().toString().trim());
                        MyOALoginAdapter myOALoginAdapter = (MyOALoginAdapter) LoginBusiness.getLoginAdapter();
                        myOALoginAdapter.authLogin(token);
//                        MyOALoginAdapter myOALoginAdapter = (MyOALoginAdapter) LoginBusiness.getLoginAdapter();
//                        myOALoginAdapter.authLogin(token);
//                        finish();
                    }

//                    @Override
//                    public void call(Login o) {
//                        if (o.getCode() != 200){
//                            LinkToast.makeText(mContext,o.getMsg(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        String token = o.getData().getToken();
//                        SettingUtils.get(mContext,"token",token);
//                        //authCode 是三方登录页面返回的code
//                        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
//                        service.authCodeLogin(mContext, token, new LoginCallback() {
//                            @Override
//                            public void onSuccess(OpenAccountSession openAccountSession) {
//                                String userId = openAccountSession.getUserId();
//                                SettingUtils.get(mContext,"token",userId);
//                                LinkToast.makeText(mContext,"登录成功",Toast.LENGTH_SHORT).show();
//                                intent = new Intent(mContext, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//
//                            }
//                        });
//                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                        Log.e("TAG", "error message:" + throwable.getMessage());
                        try {
                            String errorMsg = responseBody.string();
                            Login login = gson.fromJson(errorMsg,Login.class);
                            linkToast(mContext,login.getMsg());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (throwable instanceof Fault) {
                            Fault fault = (Fault) throwable;
                            if (fault.getErrorCode() == 404) {
                                //错误处理
                            } else if (fault.getErrorCode() == 500) {
                                //错误处理
                            } else if (fault.getErrorCode() == 501) {
                                //错误处理
                            } else if (fault.getErrorCode() == 415) {
                                Log.d(TAG, fault.getMessage());
                            }
                        }
                    }
                });
                addSubscription(subscription);
                break;
        }
    }
}
