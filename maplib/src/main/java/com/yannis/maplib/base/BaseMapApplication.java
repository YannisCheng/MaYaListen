package com.yannis.maplib.base;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.yannis.baselib.base.BaseApplication;

/**
 * BaseMapApplication
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-29
 */
public class BaseMapApplication extends BaseApplication {
    private static final String TAG = "BaseMapApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initBaiduMap();
    }

    private void initBaiduMap() {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
