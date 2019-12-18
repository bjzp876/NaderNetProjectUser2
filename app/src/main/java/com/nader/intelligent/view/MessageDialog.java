package com.nader.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.nader.intelligent.R;


/**
 * 信息弹框
 * author:zhangpeng
 * date: 2018/12/28
 */
public class MessageDialog extends Dialog{
    private Context mContext;
    private String message;
    private TextView tv_msg;
    private WebView web_dialog;
    private WindowManager windowManager;
    public MessageDialog(@NonNull Context context, WindowManager windowManager) {
        super(context);
        mContext = context;
        this.windowManager = windowManager;
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_message, null);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        tv_msg = view.findViewById(R.id.tv_msg);
        web_dialog = view.findViewById(R.id.web_dialog);
        tv_msg.setText(message);
        setWeb("file:///android_asset/软件许可协议.html");
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        Window window = getWindow();
//        if (window != null) { //设置dialog的布局样式 让其位于底部
//            window.setGravity(Gravity.CENTER);
//        }
        setContentView(view);
    }

    public void setMessage(String message){
        this.message = message;
        tv_msg.setText(message);
    }

    public void setWeb(String url){
        web_dialog.loadUrl(url);
        web_dialog.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //支持App内部javascript交互
        web_dialog.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        web_dialog.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_dialog.getSettings().setLoadWithOverviewMode(true);
    }

}
