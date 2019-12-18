package com.nader.intelligent.util;

import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.nader.intelligent.api.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里接口管理类
 * author:zhangpeng
 * date: 2019/8/6
 */
public class IotAPIClientManager {

    /**
     *  阿里控制接口
     * @param isUser 是否需要用户验证
     * @param path 接口名称
     * @param apiVersion 接口版本号
     * @param maps 接口参数
     * @param ioTCallback 接口回调
     */
    public static void sendRequest(boolean isUser,String path,String apiVersion,Map<String, Object> maps, IoTCallback ioTCallback){
        IoTRequestBuilder builder = new IoTRequestBuilder();
        builder .setPath(path);
        builder .setApiVersion(apiVersion);
        if (isUser){
            builder.setAuthType("iotAuth");
        }
        builder .setParams(maps);
        IoTRequest request = builder.build();
        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, ioTCallback);


    }

    /**
     * 绑定设备
     * @param productKey
     * @param deviceName
     * @param token
     */
    public static void bindDevices(String productKey, String deviceName, String token, final OnBindDeviceCompletedListener onBindDeviceCompletedListener){
        String path = "";
        Map<String,Object> params = new HashMap<>();
        if (productKey.equals(Constants.CENTER_CONTROL)){
            path = "/awss/token/user/bind";
            params.put("token",token);
        }else{
            path = "/awss/subdevice/bind";
        }
        params.put("productKey",productKey);
        params.put("deviceName",deviceName);

        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath(path)
                .setApiVersion("1.0.3")
                .setAuthType("iotAuth")
                .setParams(params);
        IoTRequest request = builder.build();
        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                onBindDeviceCompletedListener.onFailed(e);
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode()!=200){
                    onBindDeviceCompletedListener.onFailed(ioTResponse.getCode(),ioTResponse.getMessage(),ioTResponse.getLocalizedMsg());
                    return;
                }else{
                    String iotId = (String) ioTResponse.getData();
                    onBindDeviceCompletedListener.onSuccess(iotId);
                }
            }
        });
    }
    /**
     * 解绑设备
     * @param iotId
     */
    public static void unBindDevices(String iotId,IoTCallback ioTCallback){
        String path = "";
        Map<String,Object> params = new HashMap<>();
            path = "/home/paas/device/reset";
        params.put("iotId",iotId);

        IoTRequestBuilder builder = new IoTRequestBuilder()
                .setPath(path)
                .setApiVersion("1.0.1")
                .setAuthType("iotAuth")
                .setParams(params);
        IoTRequest request = builder.build();
        IoTAPIClient ioTAPIClient = new IoTAPIClientFactory().getClient();
        ioTAPIClient.send(request,ioTCallback);
    }


}
