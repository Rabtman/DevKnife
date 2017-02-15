package com.rabtman.example;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @author Rabtman
 */
public class BaiduMapManager {

    private static final String TAG = "BaiduMapManager";
    private static volatile BaiduMapManager instance = null;
    private Context context;
    private LocationClientOption option;//参数设置
    private LocationClient locationClient;// 定位客户端

    /**
     * 位置变化监听
     */
    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d(TAG, "speed：" + location.getSpeed());
            Log.d(TAG, "locType：" + location.getLocType());
            Log.d(TAG, "radius：" + location.getRadius());
            Log.d(TAG, "lanlng：" + location.getLatitude() + "," + location.getLongitude());
            Log.d(TAG, "brand:" + android.os.Build.BRAND);

            copyData(location);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.d(TAG, "onConnectHotSpotMessage:" + s + "|" + i);
        }
    };

    private BaiduMapManager() {

    }

    public static BaiduMapManager getInstance(Context context) {
        BaiduMapManager inst = instance;
        if (inst == null) {
            synchronized (BaiduMapManager.class) {
                inst = instance;
                if (inst == null) {
                    inst = new BaiduMapManager();
                    inst.initLocationClient(context.getApplicationContext());
                    instance = inst;
                }
            }
        }
        return inst;
    }

    private void copyData(BDLocation location) {

    }

    public LocationClient getLocationClient(Context context) {
        if (null == locationClient) {
            initLocationClient(context.getApplicationContext());
        }
        return locationClient;
    }

    public void stopTrace() {
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    public void startTrace() {
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.start();
        }
    }

    /**
     * 获取地址位置信息
     */
    public BDLocation getLastKnownLocation() {
        return locationClient.getLastKnownLocation();// 同步定位，返回最近一次定位结果
    }

    //请求定位
    public int requestBDLocation() {
        return locationClient.requestLocation();
    }

    //设置定位时间
    public void setLocationClientOption(int s) {
        if (null != option) {
            option.setScanSpan(s);
            if (null != locationClient) {
                locationClient.setLocOption(option);
            }
        }
    }

    //定位初始化
    private void initLocationClient(Context context) {
        option = new LocationClientOption();
        // Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(2000);// 设置发起定位请求的间隔时间为5000ms
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.disableCache(true);//禁用缓存定位
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setProdName(context.getString(R.string.app_name));

        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(locationListener);
        locationClient.setLocOption(option);
    }

}
