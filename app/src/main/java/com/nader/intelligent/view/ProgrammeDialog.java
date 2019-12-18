package com.nader.intelligent.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.nader.intelligent.R;
import com.nader.intelligent.adapter.CommonAdapter;
import com.nader.intelligent.entity.ProjectNew;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目列表弹出框
 * author:zhangpeng
 * date: 2019/8/12
 */
public class ProgrammeDialog extends Dialog {
    private Context mContext;
    private ListView lv_programme;
    private CommonAdapter adapter;
    private List<ProjectNew> houseList = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public ProgrammeDialog(@NonNull Context context) {
        super(context,R.style.ActionSheetDialogStyle);
        this.mContext = context;
        initView();
        initData();
    }

    private void initView(){
        View view = View.inflate(mContext, R.layout.dialog_programme,null);
        lv_programme = view.findViewById(R.id.lv_programme);
        adapter = new CommonAdapter(mContext,houseList,R.layout.item_programme) {
            @Override
            protected void convertView(View item, Object o) {
                ProjectNew house = (ProjectNew) o;
                TextView textView = item.findViewById(R.id.tv_promramme_name);
                textView.setText(house.getName());
            }
        };
        lv_programme.setAdapter(adapter);
        lv_programme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                ProjectNew house = houseList.get(position);
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(house,position);
                }
            }
        });
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.TOP);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            WindowManager windowManager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
        setContentView(view);
    }




    public void setData(List<ProjectNew> list){
        if (houseList!=null){
        }else{
            houseList = new ArrayList<>();
        }
        houseList = list;
        adapter.notifyDataSetChanged(houseList);
    }

    private void initData(){
        for (int i=0;i<5;i++){
            ProjectNew house = new ProjectNew();
            house.setId(String.valueOf(i+1));
            house.setName("项目"+i);
            houseList.add(house);
        }
        adapter.notifyDataSetChanged(houseList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(ProjectNew house, int position);
    }

    private int getStateBar(){
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }else{
            double statusBarHeight = Math.ceil(20 * mContext.getResources().getDisplayMetrics().density);
            result  = (int) statusBarHeight;
        }
        return result;
    }

}
