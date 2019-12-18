package com.nader.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.nader.intelligent.R;

/**
 * author:zhangpeng
 * date: 2019/11/22
 */
public abstract class BaseIndexBotmDialog extends Dialog {
    protected Window window;
    protected Context context;

    public BaseIndexBotmDialog(@NonNull Context context) {
        super(context, R.style.bot_dialog_style);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.bot_dialog_animation); //设置窗口弹出动画
        window.setGravity(Gravity.BOTTOM);//让其在底部
    }

    @Override
    public void show() {
        super.show();
        //让Dialog横向铺满
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }

    protected abstract void initView();

    protected abstract int getContentView();
}

