package com.nader.intelligent.util.permission;

import java.util.List;

/**
 * 权限回调接口
 * author:zhangpeng
 * date: 2018/9/27
 */
public interface PermissionListener {

    //授权成功
    void onGranted();

    //授权部分
    void onGranted(List<String> grantedPermission);

    // 拒绝授权
    void onDenied(List<String> deniedPermission);

    //权限申请完毕
    void onFinish();


}
