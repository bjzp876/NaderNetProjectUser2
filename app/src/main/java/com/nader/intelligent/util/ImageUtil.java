package com.nader.intelligent.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nader.intelligent.R;

/**
 * 图像处理
 * author:zhangpeng
 * date: 2019/11/20
 */
public class ImageUtil {
    /**
     * 加载图片
     * @param context
     * @param url
     * @param view
     */
    public static void setImage(Context context, String url, ImageView view){
        RequestOptions options = new RequestOptions().placeholder(R.drawable.page_index_def_img).error(R.drawable.page_index_def_img).fallback(R.drawable.page_index_def_img);
        Glide.with(context).load(url).apply(options).into(view);
    }
}
