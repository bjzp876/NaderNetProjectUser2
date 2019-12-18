package com.nader.intelligent.entity;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;
import com.aliyun.iot.aep.sdk.framework.config.AConfigure;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.nader.intelligent.api.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author:zhangpeng
 * date: 2019/8/29
 */
public class Tracker implements com.aliyun.iot.aep.sdk.apiclient.tracker.Tracker {

    private final String TAG = "Tracker";

    @Override
    public void onSend(IoTRequest request) {
        ALog.i(TAG, "onSend:\r\n" + toString(request));
    }

    @Override
    public void onRealSend(IoTRequestWrapper ioTRequest) {
        ALog.d(TAG, "onRealSend:\r\n" + toString(ioTRequest));
    }

    @Override
    public void onRawFailure(IoTRequestWrapper ioTRequest, Exception e) {
        ALog.d(TAG, "onRawFailure:\r\n" + toString(ioTRequest) + "ERROR-MESSAGE:" + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onFailure(IoTRequest request, Exception e) {
        ALog.i(TAG, "onFailure:\r\n" + toString(request) + "ERROR-MESSAGE:" + e.getMessage());
    }

    @Override
    public void onRawResponse(IoTRequestWrapper request, IoTResponse response) {
        ALog.d(TAG, "onRawResponse:\r\n" + toString(request) + toString(response));
    }

    @Override
    public void onResponse(IoTRequest request, IoTResponse response) {
        ALog.i(TAG, "onResponse:\r\n" + toString(request) + toString(response));
    }

    private static String toString(IoTRequest request) {

        return "Request:" + "\r\n" +
                "url:" + request.getScheme() + "://" + (null == request.getHost() ? "" : request.getHost()) + request.getPath() + "\r\n" +
                "apiVersion:" + request.getAPIVersion() + "\r\n" +
                "params:" + (null == request.getParams() ? "" : JSON.toJSONString(request.getParams())) + "\r\n";
    }

    private static String toString(IoTRequestWrapper wrapper) {
        IoTRequest request = wrapper.request;
        String apiEnv = AConfigure.getInstance().getConfig("env");
        String defaultHost = IoTAPIClientImpl.getInstance().getDefaultHost();
        if (request.getParams() != null) {
            try {
                JSONObject jsonObject = new JSONObject(JSON.toJSONString(request.getParams()));
                if (jsonObject.has("request")) {
                    jsonObject = jsonObject.getJSONObject("request");
                    if (jsonObject.has("appKey")) {
                        Constants.appKey = jsonObject.getString("appKey");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "Request:" + "\r\n" +
                "id:" + wrapper.payload.getId() + "\r\n" +
                "apiEnv:" + apiEnv + "\r\n" +
                "url:" + request.getScheme() + "://" + (TextUtils.isEmpty(wrapper.request.getHost()) ? defaultHost : wrapper.request.getHost()) + request.getPath() + "\r\n" +
                "apiVersion:" + request.getAPIVersion() + "\r\n" +
                "params:" + (null == request.getParams() ? "" : JSON.toJSONString(request.getParams())) + "\r\n" +
                "payload:" + JSON.toJSONString(wrapper.payload) + "\r\n";
    }

    private static String toString(IoTResponse response) {
        if (response != null) {
            if (response.getData() != null && response.getCode() == 200) {
                try {
                    JSONObject jsonObject = new JSONObject(response.getData().toString());
                    if (jsonObject.getJSONObject("data") != null) {
                        jsonObject = jsonObject.getJSONObject("data");
                        if (jsonObject.has("identityId")) {
                            Constants.identityId = jsonObject.getString("identityId");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return "Response:" + "\r\n" +
                "id:" + response.getId() + "\r\n" +
                "code:" + response.getCode() + "\r\n" +
                "message:" + response.getMessage() + "\r\n" +
                "localizedMsg:" + response.getLocalizedMsg() + "\r\n" +
                "data:" + (null == response.getData() ? "" : response.getData().toString()) + "\r\n";
    }
}
