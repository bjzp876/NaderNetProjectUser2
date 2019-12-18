package com.nader.intelligent.api;

import android.util.Log;

import com.google.gson.Gson;
import com.nader.intelligent.entity.HouseNew;
import com.nader.intelligent.entity.Login;
import com.nader.intelligent.entity.Project;
import com.nader.intelligent.entity.ProjectNew;
import com.nader.intelligent.entity.Room;
import com.nader.intelligent.entity.SceneDetail;
import com.nader.intelligent.entity.vo.SceneVo;
import com.nader.intelligent.util.http.BaseResponse;
import com.nader.intelligent.util.http.ObjectLoader;
import com.nader.intelligent.util.http.RetrofitServiceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * author:zhangpeng
 * date: 2019/8/1
 */
public class ApiConfig extends ObjectLoader {
    private String TAG = "ProjectUser";
    /**
     * 服务器地址，应该根据 DEBUG 判断选用哪个环境
     */
    public static final String BASE_URL = "https://iot.sh-liangxin.com/";
    //    public static final String BASE_URL = "http://dxswnn.natappfree.cc/";
    private ApiConfigService apiConfigService;
    public static final int OFFSET = 0;//开始页数
    public static final int MAX = 100;//最大加载数量

    public ApiConfig() {
        apiConfigService = RetrofitServiceManager.getInstance().create(ApiConfig.ApiConfigService.class);
    }

    /**
     * 登录接口
     *
     * @param map
     * @return
     */
    public Observable<Login> login(HashMap<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.login(requestBody)).cast(Login.class);
    }

    /**
     * 登录接口
     *
     * @param map
     * @return
     */
    public Observable<ResponseBody> loginJson(HashMap<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.login(requestBody)).cast(ResponseBody.class);
    }


    /**
     * 获取项目列表
     *
     * @param token
     * @return
     */
    public Observable<BaseResponse> getProjectForAli(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getProjectsForAli(token, requestBody)).cast(BaseResponse.class);
    }

    /**
     * 获取房屋
     *
     * @param token
     * @return
     */
    public Observable<BaseResponse> getHouseForAli(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getHouseForAli(token, requestBody)).cast(BaseResponse.class);
    }

    /**
     * 创建房间
     *
     * @param token
     * @return
     */
    public Observable<ResponseBody> createRoom(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.createRoom(token, requestBody)).cast(ResponseBody.class);
    }

    /**
     * 删除房间
     *
     * @param token
     * @return
     */
    public Observable<ResponseBody> deleteRoom(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.deleteRoom(token, requestBody)).cast(ResponseBody.class);
    }

    /**
     * 获取房间下设备列表
     *
     * @param token
     * @return
     */
    public Observable<BaseResponse> getRoomDevice(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getRoomDevice(token, requestBody)).cast(BaseResponse.class);
    }

    /**
     * 绑定设备至房间
     *
     * @param token
     * @return
     */
    public Observable<ResponseBody> setDevicetoRoom(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.setDevicetoRoom(token, requestBody)).cast(ResponseBody.class);
    }

    /**
     * 获取房屋列表
     *
     * @param token
     * @param offset
     * @param max
     * @return
     */
    public Observable<List<HouseNew>> getHouse(String token, int offset, int max, String projectId) {
        return observe(apiConfigService.getHouse(token, offset, max, projectId).map(new Func1<List<HouseNew>, List<HouseNew>>() {
            @Override
            public List<HouseNew> call(List<HouseNew> houseNews) {
                return houseNews;
            }
        }));
    }

    /**
     * 获取房屋列表
     *
     * @param token
     * @param offset
     * @param max
     * @return
     */
    public Observable<List<HouseNew>> getCheckHouse(String token, int offset, int max, String projectId) {
        return observe(apiConfigService.getCheckHouse(token, offset, max, projectId).map(new Func1<List<HouseNew>, List<HouseNew>>() {
            @Override
            public List<HouseNew> call(List<HouseNew> houseNews) {
                return houseNews;
            }
        }));
    }

    /**
     * 获取房间列表
     *
     * @param token
     * @param offset
     * @param max
     * @return
     */
    public Observable<List<Room>> getRoom(String token, int offset, int max, String houseId) {
        return observe(apiConfigService.getRoom(token, offset, max, houseId).map(new Func1<List<Room>, List<Room>>() {
            @Override
            public List<Room> call(List<Room> rooms) {
                return rooms;
            }
        }));
    }

    /**
     * 获取调试房间列表
     *
     * @param token
     * @param offset
     * @param max
     * @return
     */
    public Observable<List<Room>> getCheckRoom(String token, int offset, int max, String houseId) {
        return observe(apiConfigService.getCheckRoom(token, offset, max, houseId).map(new Func1<List<Room>, List<Room>>() {
            @Override
            public List<Room> call(List<Room> rooms) {
                return rooms;
            }
        }));
    }

    /**
     * 获取设备列表
     *
     * @return
     */
    public Observable<List<Room>> getDevices(Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getDevices(requestBody)).map(new Func1<List<Room>, List<Room>>() {
            @Override
            public List<Room> call(List<Room> rooms) {
                return rooms;
            }
        });
    }

    /**
     * 控制设备
     *
     * @param token
     * @param json
     * @return
     */
    public Observable<ResponseBody> controlDevice(String token, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.controlDevice(token, requestBody));
    }


    /**
     * 重置设备
     *
     * @param token
     * @return
     */
    public Observable<ResponseBody> resetDevice(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.resetDevice(token, requestBody)).cast(ResponseBody.class);
    }

    /**
     * 上报设备组网
     *
     * @param token
     * @param iotId
     * @return
     */
    public Observable<ResponseBody> getDeviceInfo(String token, String iotId) {
        Map<String, String> map = new HashMap<>();
        map.put("iotId", iotId);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getDeviceInfo(token, requestBody)).cast(ResponseBody.class);
    }

    /**
     * 获取该用户下的场景列表
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<SceneVo> getSceneByUser(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return observe(apiConfigService.getSceneByUser(token, requestBody)).cast(SceneVo.class);
    }

    /**
     * 场景执行
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> run(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.run(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 创建场景
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> createScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.createScene(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 获取用户下的设备
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> getSceneDeviceList(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.getSceneDeviceList(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 获取用户下的设备属性
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> getDeviceInfoByScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.getDeviceInfoByScene(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 获取用户下的设备状态
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<ResponseBody> getDeviceStateByScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.getDeviceStateByScene(token, requestBody).cast(ResponseBody.class);
    }

    /**
     * 部署场景
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<ResponseBody> deployScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.deployScene(token, requestBody).cast(ResponseBody.class);
    }

    /**
     * 部署场景
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> bindScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.bindSecene(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 部署场景
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<BaseResponse> getSceneDetail(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.getSceneDetail(token, requestBody).cast(BaseResponse.class);
    }

    /**
     * 删除场景
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<ResponseBody> delScene(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.delScene(token, requestBody).cast(ResponseBody.class);
    }

    /**
     * 更新场景TCA
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<ResponseBody> updateSceneByTCA(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.updateSceneByTCA(token, requestBody).cast(ResponseBody.class);
    }


    /**
     * 更新场景TCA
     *
     * @param token
     * @param map
     * @return
     */
    public Observable<ResponseBody> updateSceneByName(String token, Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return apiConfigService.updateSceneByName(token, requestBody).cast(ResponseBody.class);
    }

    private interface ApiConfigService {
        @POST("/auth/login")
        Observable<Login> login(@Body RequestBody Msg);

        //项目列表
        @POST("/iot/homelink/space/root/list")
        Observable<BaseResponse> getProjectsForAli(@Header("Authorization") String token, @Body RequestBody Msg);

        //项目下房屋列表
        @POST("/iot/homelink/space/sub/list")
        Observable<BaseResponse> getHouseForAli(@Header("Authorization") String token, @Body RequestBody Msg);

        //房间设备列表
        @POST("/si/enroll/roomList")
        Observable<List<Room>> getDevices(@Body RequestBody Msg);

        //创建房间
        @POST("/iot/homelink/space/sub/create")
        Observable<ResponseBody> createRoom(@Header("Authorization") String token, @Body RequestBody Msg);

        //删除房间
        @POST("/iot/homelink/space/delete")
        Observable<ResponseBody> deleteRoom(@Header("Authorization") String token, @Body RequestBody Msg);

        //获取房间下设备列表
        @POST("iot/homelink/space/device/list")
        Observable<BaseResponse> getRoomDevice(@Header("Authorization") String token, @Body RequestBody Msg);

        //绑定设备至房间
        @POST("/iot/homelink/device/space/bind")
        Observable<ResponseBody> setDevicetoRoom(@Header("Authorization") String token, @Body RequestBody Msg);

        //删除设备
        @POST("iot/homelink/device/reset")
        Observable<ResponseBody> resetDevice(@Header("Authorization") String token, @Body RequestBody Msg);

        //房屋列表
        @GET("si/enroll/house/list")
        Observable<List<HouseNew>> getHouse(@Header("Authorization") String token, @Query("offset") int offset, @Query("max") int max, @Query("projectId") String projectId);

        //房屋列表
        @GET("si/check/house/list")
        Observable<List<HouseNew>> getCheckHouse(@Header("Authorization") String token, @Query("offset") int offset, @Query("max") int max, @Query("projectId") String projectId);

        //房间列表R
        @GET("si/enroll/room/list")
        Observable<List<Room>> getRoom(@Header("Authorization") String token, @Query("offset") int offset, @Query("max") int max, @Query("houseId") String houseId);

        //房间列表R
        @GET("si/check/room/list")
        Observable<List<Room>> getCheckRoom(@Header("Authorization") String token, @Query("offset") int offset, @Query("max") int max, @Query("houseId") String houseId);

        //设备控制
        @POST("iot/homelink/device/control")
        Observable<ResponseBody> controlDevice(@Header("Authorization") String token, @Body RequestBody Msg);

        //设备属性获取
        @POST("iot/homelink/device/detail")
        Observable<ResponseBody> getDeviceInfo(@Header("Authorization") String token, @Body RequestBody Msg);

        //场景列表获取
        @POST("iot/homelink/user/scene/list")
        Observable<SceneVo> getSceneByUser(@Header("Authorization") String token, @Body RequestBody Msg);

        //场景执行
        @POST("iot/homelink/scene/instance/run")
        Observable<BaseResponse> run(@Header("Authorization") String token, @Body RequestBody Msg);

        //创建场景
        @POST("iot/homelink/scene/create")
        Observable<BaseResponse> createScene(@Header("Authorization") String token, @Body RequestBody Msg);

        //场景内设备列表获取
        @POST("iot/homelink/scene/tca/user")
        Observable<BaseResponse> getSceneDeviceList(@Header("Authorization") String token, @Body RequestBody Msg);

        //场景内设备属性获取
        @POST("iot/homelink/product/tca/get")
        Observable<BaseResponse> getDeviceInfoByScene(@Header("Authorization") String token, @Body RequestBody Msg);

        //场景内设备状态获取
        @POST("iot/homelink/device/tsl")
        Observable<ResponseBody> getDeviceStateByScene(@Header("Authorization") String token, @Body RequestBody Msg);

        //部署场景
        @POST("iot/homelink/scene/instance/deploy")
        Observable<ResponseBody> deployScene(@Header("Authorization") String token, @Body RequestBody Msg);

        //绑定场景
        @POST("iot/homelink/user/scene/bind")
        Observable<BaseResponse> bindSecene(@Header("Authorization") String token, @Body RequestBody Msg);

        //获取场景详情
        @POST("iot/homelink/scene/get")
        Observable<BaseResponse> getSceneDetail(@Header("Authorization") String token, @Body RequestBody Msg);

        //删除场景
        @POST("iot/homelink/scene/delete")
        Observable<ResponseBody> delScene(@Header("Authorization") String token, @Body RequestBody Msg);

        //更新场景TCA
        @POST("iot/homelink/scene/tca/update")
        Observable<ResponseBody> updateSceneByTCA(@Header("Authorization") String token, @Body RequestBody Msg);

        //更新场景名称
        @POST("iot/homelink/scene/update")
        Observable<ResponseBody> updateSceneByName(@Header("Authorization") String token, @Body RequestBody Msg);
    }
}
