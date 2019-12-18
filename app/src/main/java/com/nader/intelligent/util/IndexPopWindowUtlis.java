package com.nader.intelligent.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.aliyun.iot.aep.sdk.log.ALog;
import com.nader.intelligent.R;


/**
 * 作者：wb-zxq412979 on 2018/6/11.
 * Good luck
 */
public class IndexPopWindowUtlis {

    public static PopupWindow getAsDropDownPopWindow( Context mContext, int layoutID, boolean focusble) {

        return createPopWindow(mContext,layoutID,focusble,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
    }

    public static PopupWindow getMacthWidthAsDropDownPopWindow( Context mContext, int layoutID, boolean focusble){
        return createPopWindow(mContext,layoutID,focusble,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
    }

    public static PopupWindow getMacthAsDropDownPopWindow( Context mContext, int layoutID, boolean focusble){
        return createPopWindow(mContext,layoutID,focusble,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, R.style.page_index_top);
    }

    private static PopupWindow createPopWindow(Context mContext, int layoutID, boolean focusble,int w,int h,int styles){
        View view = LayoutInflater.from(mContext).inflate(layoutID, null, false);
        PopupWindow popupWindow=null;
        if(h==ViewGroup.LayoutParams.MATCH_PARENT){
            popupWindow  = new PopupWindow(view, w, h){
                @Override
                public void showAsDropDown(View anchor) {
                    if(Build.VERSION.SDK_INT >= 24){

                        Rect visibleFrame = new Rect();
                        anchor.getGlobalVisibleRect(visibleFrame);
                        int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                        setHeight(height);
                        ALog.d("PopupWindow","height="+height);
                    }

                    super.showAsDropDown(anchor);

                }
            };
        }else {
            popupWindow=new PopupWindow(view, w, h);
        }

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.pop_black_btm_bg));
        popupWindow.setFocusable(focusble);// 点击空白处时，隐藏掉pop窗口
        if(styles!=0){
            popupWindow.setAnimationStyle(styles);
        }
        return popupWindow;
    }



    public static void setBackgroundAlpha(float bgAlpha ,Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
