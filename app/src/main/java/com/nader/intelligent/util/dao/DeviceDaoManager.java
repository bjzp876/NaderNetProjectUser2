package com.nader.intelligent.util.dao;

import com.aliyun.iot.aep.sdk.init.MyApplication;
import com.nader.intelligent.entity.vo.DeviceVo;

import java.util.List;

import greendao.DeviceVoDao;

/**
 * 设备数据管理类
 * author:zhangpeng
 * date: 2019/8/22
 */
public class DeviceDaoManager {
    private static DeviceDaoManager mManger;
    private static DeviceVoDao mDeviceVoDao;

    public static DeviceDaoManager getInstance() {
        synchronized (DeviceDaoManager.class) {
            if (mManger == null) {
                mManger = new DeviceDaoManager();
            }
            return mManger;
        }
    }

    public DeviceDaoManager() {
        mDeviceVoDao = DbManager.getDaoSession(MyApplication.getContext()).getDeviceVoDao();
    }

    /**
     * 新增数据
     * @param bean
     */
    public void insertData(DeviceVo bean) {
        mDeviceVoDao.insert(bean);
    }
    /**
     * 增加一组数据
     * @param list
     */
    public void insertData(List<DeviceVo> list) {
        mDeviceVoDao.insertInTx(list);
    }

    /**
     * 修改数据
     * @param bean
     */
    public void updateData(DeviceVo bean) {
        mDeviceVoDao.update(bean);
    }

    /**
     * 查询数量
     * @return
     */
    public long getUserCount() {
        return mDeviceVoDao.count();
    }



    /**
     * 按条件查询
     * @param name
     * @return
     */
    public List<DeviceVo> queryByName(String name) {
        List<DeviceVo> list = mDeviceVoDao
                .queryBuilder()
                .where(DeviceVoDao.Properties.Name.eq(name))
                .list();
        return list;
    }

    /**
     * 删除数据
     * @param bean
     */
    public void deleteData(DeviceVo bean) {
        mDeviceVoDao.delete(bean);
    }

    /**
     * 删除一组数据
     * @param list
     */
    public void deleteData(List<DeviceVo> list) {
        mDeviceVoDao.deleteInTx(list);
    }
    /**
     * 删除全部
     */
    public void deleteAll() {
        mDeviceVoDao.deleteAll();
    }
}
