package com.nader.intelligent.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aliyun.iot.link.ui.component.LinkToast;
import com.google.gson.Gson;
import com.nader.intelligent.api.ApiConfig;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者:create by 6648 on 2018/9/3 14:02
 * 邮箱:6648@shuncom.com
 * 描述:所有的Fragment 需集成 此Fragment
 */
public abstract class BaseFragment extends Fragment {
    public String TAG ="ProjectUser" ;
    public View view;
    protected Context mContext;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    public Intent intent;
    protected Gson gson;
    public ApiConfig apiConfig;
    private CompositeSubscription sCompositeSubscription ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        mContext = getActivity();
        apiConfig = new ApiConfig();
        if(sCompositeSubscription == null || sCompositeSubscription.isUnsubscribed()){
            sCompositeSubscription = new CompositeSubscription();
        }
    }


    protected abstract void initView();

    /**
     * 无参数启动Activity
     *
     * @param cls 目标Activity
     */
    public void startMyActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    /**
     * 启动Activity
     *
     * @param cls    目标Activity
     * @param bundle 需要传的参数
     */
    public void startMyActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            pauseData();
            isUIVisible = false;
        }
    }

    /**
     * 获取用户可见
     * @return
     */
    public boolean getIsVisibleToUser(){
        return isUIVisible;
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }else{
            reconnect();
        }
    }

    public void reconnect(){

    }

    /**
     * 添加Subscription
     * @param subscription
     */
    public void addSubscription(Subscription subscription){
        Log.d(TAG,"add subscription");
        sCompositeSubscription.add(subscription);
    }

    protected abstract void pauseData();



    protected abstract void loadData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //页面销毁,恢复标记
        isViewCreated = false;
        isUIVisible = false;
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sCompositeSubscription!=null){
            Log.d(TAG,"base activity unscbscribe");
            sCompositeSubscription.unsubscribe();
        }
    }
}
