package com.nader.intelligent.util.permission;


import com.nader.intelligent.base.BaseActivity;

/**
 * 权限获取工具类
 * author:zhangpeng
 * date: 2018/9/27
 */
public class PermissionUtil {
    /**
     * 获取权限回调
     * @param permissions 权限列表
     * @param permissionListener
     */
    public static void requestPermission(String[] permissions,PermissionListener permissionListener){
        BaseActivity.requestRunTimePermission(permissions
                ,permissionListener);
    }
}
