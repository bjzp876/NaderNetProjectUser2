package com.nader.intelligent.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 主页adapter
 * Created by wangxw on 2017/2/10 0009 15:05.
 * E-mail:wangxw725@163.com
 * function:
 */
public class MainAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public MainAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList!=null ? fragmentList.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * 更新fragment
     */
    public void notification(int position, Fragment fragment){
        fragmentList.set(position,fragment);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
