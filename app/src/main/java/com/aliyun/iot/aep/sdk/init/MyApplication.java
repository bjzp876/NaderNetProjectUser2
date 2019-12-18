package com.aliyun.iot.aep.sdk.init;

import android.content.Context;

import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.nader.intelligent.R;
import com.nader.intelligent.entity.Tracker;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * author:zhangpeng
 * date: 2019/8/6
 */
public class MyApplication extends AApplication {
    public static MyApplication myApplication;
    private static Context mContext;
    private String TAG = "ProjectUser";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        mContext = getApplicationContext();


        // 开启网络请求日志
        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
            @Override
            public void run() {
                // 开启网络请求日志
                IoTAPIClientImpl.getInstance().registerTracker(new Tracker());
                ALog.setLevel(ALog.LEVEL_DEBUG);
                // 开启长连接日志
                com.aliyun.alink.linksdk.tools.ALog.setLevel(ALog.LEVEL_DEBUG);
            }
        });

    initRefreshLayout();

    }

    public static Context getContext() {
        return mContext;
    }

    private void initRefreshLayout(){
        //static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

}
