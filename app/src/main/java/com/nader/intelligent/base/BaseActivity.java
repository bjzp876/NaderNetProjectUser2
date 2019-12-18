package com.nader.intelligent.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.aliyun.iot.link.ui.component.LinkToast;
import com.google.gson.Gson;
import com.nader.intelligent.R;
import com.nader.intelligent.api.ApiConfig;
import com.nader.intelligent.util.ActivityCollector;
import com.nader.intelligent.util.SettingUtils;
import com.nader.intelligent.util.StatusBarUtil;
import com.nader.intelligent.util.permission.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * author:zhangpeng
 * date: 2019/8/1
 */
public class BaseActivity extends AppCompatActivity {
    public String TAG ="ProjectUser" ;
    private CompositeSubscription sCompositeSubscription ;
    public Handler handler;
    public Intent intent;
    public Context mContext;
    public Gson gson;
    private static PermissionListener mlistener;
    public ApiConfig apiConfig;
    public Map<String,Object> map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        mContext = this;
        apiConfig = new ApiConfig();

        if(sCompositeSubscription == null || sCompositeSubscription.isUnsubscribed()){
            sCompositeSubscription = new CompositeSubscription();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
    }

    /**
     * 添加Subscription
     * @param subscription
     */
    public void addSubscription(Subscription subscription){
        Log.d(TAG,"add subscription");
        sCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        if(sCompositeSubscription!=null){
            Log.d(TAG,"base activity unscbscribe");
            sCompositeSubscription.unsubscribe();
        }
    }

    public void initView(){

    }

    public void initData(){

    }

    public void initListener(){

    }

    /**
     * 获取token
     * @param context
     * @return
     */
    public String getToken(Context context){
        String token = null;
        token = SettingUtils.get(getApplicationContext(),"token");
        return token;
    }


    public void toast(Context context,String msg){
        try {
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            Looper.loop();
        }

    }

    public void linkToast(Context context,String msg){
        try {
            LinkToast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            LinkToast.makeText(context,msg,Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }


    /**
     * 权限申请
     * @param permissions 待申请的权限集合
     * @param listener  申请结果监听事件
     */
    public static void requestRunTimePermission(String[] permissions, PermissionListener listener){
        mlistener = listener;

        Activity topActivity = ActivityCollector.getTopActivity();
        if (topActivity == null){
            return;
        }

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(topActivity,permission) != PackageManager.PERMISSION_GRANTED){
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()){  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(topActivity,permissionList.toArray(new String[permissionList.size()]),1);
        }else{  //为空，则已经全部授权
            listener.onGranted();
        }
    }

    /**
     * 权限申请结果
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED){ //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){  //用户拒绝权限为空
                        mlistener.onGranted();
                    }else {  //不为空
                        //回调授权成功的接口
                        mlistener.onDenied(deniedPermissions);
                        //回调授权失败的接口
                        mlistener.onGranted(grantedPermissions);
                        mlistener.onFinish();
                    }
                }
                break;
            default:
                break;
        }
    }

}
