package com.nader.intelligent.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nader.intelligent.R;
import com.nader.intelligent.base.BaseFragment;

/**
 * 安防页
 * author:zhangpeng
 * date: 2019/11/11
 */
public class SafetyFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        gson = new Gson();
        view = inflater.inflate(R.layout.fragment_safety, container, false);
        initView();
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void pauseData() {

    }

    @Override
    protected void loadData() {

    }
}
