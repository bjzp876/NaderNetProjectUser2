package com.nader.intelligent.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.nader.intelligent.entity.House;
import com.nader.intelligent.entity.Rooms;
import com.nader.intelligent.fragment.SingleRoomFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:create by 6648 on 2018/9/10 14:10
 * 邮箱:6648@shuncom.com
 * 描述:房间的列表
 */
public class RoomAdapter extends FragmentStatePagerAdapter {
    private List<SingleRoomFragment> list = new ArrayList<>();
    private List<House> dataBeans = new ArrayList<>();

    public RoomAdapter(FragmentManager fm, List<House> dataBeans) {
        super(fm);
        this.dataBeans = new ArrayList<>();
        this.dataBeans = dataBeans;
        list = new ArrayList<>();
        for (House house : dataBeans) {
            list.add(SingleRoomFragment.newInstance(house));
        }
    }

    @Override
    public SingleRoomFragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list!=null ? list.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    //重写这个方法，将设置每个Tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return dataBeans.get(position).getName();
    }

    public List<SingleRoomFragment> getRoomData(){
        return list;
    }
}
