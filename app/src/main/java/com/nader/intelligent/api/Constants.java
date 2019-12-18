package com.nader.intelligent.api;

/**
 * author:zhangpeng
 * date: 2019/8/21
 */
public class Constants {
    public static final String centerToken = "359630C1D839081181922E6A253D59F2";
    public static final String iotId = "eyd94CE765CPH3RUyDvU000100";
    public static final int SUCCESS = 1000;
    public static final int ERROR = 1001;
    public static final int REQUEST_CODE_CONFIG_WIFI = 0x1002;
    public static final int REQUEST_CODE_SCAN = 0x012;
    public static final int ADD_ROOM = 1003;
    public static final int DEL_ROOM = 1004;
    public static final int REFRESH_DEVICE = 1005;
    public static final int DELETE_DEVICE = 1006;
    public static final int REFRESH_SCENE = 1007;
    public static final int DATE_WEEK = 1008;

    /**
     * The constant PLUGIN_ID_DEVICE_CONFIG.
     * 线上/预发
     */
    public final static String PLUGIN_ID_DEVICE_CONFIG = "link://router/connectConfig";

    //对应设备的字段为dsp
    public static  final String LOCK_FOR_XILEQI = "b18NjdVajPj";//西勒奇智能门锁
    public static  final String LOCK_FOR_HUITAILONG = "b146fFrzmBD";//汇泰龙智能门锁
    public static  final String SENSOR_PM = "b1KfbfOp3WX";//PM25传感器
    public static  final String TEMPERATURE_HUMIDITY = "b1QvchG9Oiz";//温湿度
    public static  final String HUMAN_BODY_SENSOR = "b1RF03iD3N6";//人体传感器
    public static  final String INFRARED_CURTAIN = "b1QDLrWVyod";//红外幕帘
    public static  final String EMERGENCY_BUTTON = "a1KEqVuU8MN";//紧急按钮
    public static  final String MAGNETIC = "b1rAGD548an";//门磁
    public static  final String WATER_IMMERSION_SENSOR = "b1Ji3w5I9QL";//水浸传感器
    public static  final String FIRE_SENSOR = "b1y0MmQPzG2";//燃气报警器
    public static  final String ONE_ZIGBEE_SWITCH = "b1yOKVRXEvm";//一位ZigBee开关
    public static  final String TWO_ZIGBEE_SWITCH = "b1hWFUKEx5t";//两位ZigBee开关
    public static  final String FOUR_ZIGBEE_SWITCH = "b1QMfbBS7Ac";//场景控制面板 四键ZigBee开关
    public static  final String SINGLE_CURTAIN_CONTROL_PANEL = "b1jnQsdslSG";//单路窗帘控制面板
    public static  final String DOUBLE_CURTAIN_CONTROL_PANEL = "b1SvWvv1igb";//两路窗帘控制面板
    public static  final String ONE_SWITCH = "b1A2fiD5EMR";//一键智能开关
    public static  final String TWO_SWITCH = "b1LWj5QnPY6";//两键智能开关
    public static  final String THREE_SWITCH = "b1hrb0WBdg3";//三键智能开关
    public static  final String SZ_ENVMONING = "SZUiot EnvMoning";//环境盒子
    public static  final String LUMI_PLUG = "lumi.plug";//小米插座
    public static  final String MANDUNAIRONOFF = "SZMandunaironoff";//曼顿空气开关
    public static  final String ZSW07 = "ZSW07";//ABB四键情景面板
    public static  final String OUTLET = "b1yna91hZDc";//插座
    public static  final String ELECTRIC_MACHINE = "b1XaelAagzL";//智能电机
    public static  final String CENTER_CONTROL = "b1eFvC6jou0";//智能中控
    public static  final String CENTER_SECOND_CONTROL = "b10VBsm6zDZ";//智能中控面板
    public static  final String DOORBELL = "b13ULwAuLMe";//音乐门铃
    public static  final String AC = "a1Zp2GlLuHA";//空调
    public static  final String AC_CONTROL = "a1dfmwtBwMM";
    public static  final String WIND_CONTROL = "a1gDaH9fPdF";//新风控制面板
    public static  final String WATER_CONTROL = "a12Po3MKmru";//水暖控制面板
    public static  final String ELECTRIC_CONTROL = "a1mFhpxA0Rg";//电暖控制面板
    public static  final String INFRARED_CHANGE_CONTROL = "a1k5C9hrtVx";//红外转发

    public static final String ADD_ROOM_NAME_KEY="ADD_ROOM_NAME_KEY";//用于传递房间名称的KEY
    public static final String ADD_ROOM_TYPE_KEY="ADD_ROOM_TYPE_KEY";//用于传递当前页面类型的KEY
    public static final String SELECT_URL_KEY="SELECT_URL_KEY";//用于房间背景选择传入默认选择的URL的KEY
    public static final String ROOM_ID_KEY="ROOM_ID_KEY";
    public static final String ROOM_BEAN_KEY="ROOM_BEAN_KEY";
    public static final String ROOM_EQUIPMENT_LIST_KEY="ROOM_EQUIPMENT_LIST_KEY";
    public static final String ROOM_DEL_KEY="ROOM_DEL_KEY";
    public static final String UP_INDEX_FRAGMENT_ACTION="UP_INDEX_FRAGMENT_ACTION";
    public static final String HOUSE_DATA_STATE = "HOUSE_DATA_STATE";//房屋内设备长连接状态变化
    public static final String ADD_NEW_ROOM = "ADD_NEW_ROOM";//新添加房屋
    public static final String UP_INDEX_FRAGMENT_SCAN="UP_INDEX_FRAGMENT_SCAN";
    public static final String REFRESH_DATA="REFRESH_DATA";//刷新数据
    public static final String LOGIN="LOGIN";
    public static final String REFRESH_INFRARED_DEVICE="REFRESH_INFRARED_DEVICE";//刷新添加的红外设备
    //有新消息提醒
    public static final String UP_INDEX_FRAGMENT_MESSAGE_ACTION="UP_INDEX_FRAGMENT_MESSAGE_ACTION";
    //授权成功
    public static final String PERMISSION_SUCCESS="PERMISSION_SUCCESS";

    public static final int TYPE_ADD_ROOM=1; //新建房间
    public static final int TYPE_ROOM_CHENGE=2;//修改房间

    public static String appKey;
    public static String identityId;

    /**
     * 刷新设备
     */
    public static String REFRESH_DEVICE_DATA = "REFRESH_DEVICE";
    /**
     * 刷新房间
     */
    public static String REFRESH_ROOM = "REFRESH_ROOM";

}
