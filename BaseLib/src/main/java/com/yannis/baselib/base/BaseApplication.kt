package com.yannis.baselib.base

import android.app.Application
import com.sensorsdata.analytics.android.sdk.SAConfigOptions
import com.sensorsdata.analytics.android.sdk.SensorsAnalyticsAutoTrackEventType
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI
import com.tencent.bugly.Bugly
import com.yannis.filedownloadlib.FileDownloadInit
import com.yannis.maplib.MapInit

/**
 * 原bugly的App升级配置
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/9
 */
open class BaseApplication : Application() {

    private val SA_SERVER_URL = "http://192.168.8.104:8002/"

    override fun onCreate() {
        super.onCreate()
        Bugly.init(getApplicationContext(), "f2acca239c", false);
        MapInit(this)
        // App安装后首次冷启动时调用预初始化函数
        //UmengInit.preInit(this)
        //UmengInit.initSetting(this)
        FileDownloadInit(this)
        initSensorsDataAPI()
    }

    private fun initSensorsDataAPI() {
        // 设置收集数据的服务器地址
        val configOptions = SAConfigOptions(SA_SERVER_URL)
        // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
        configOptions.setAutoTrackEventType(
            SensorsAnalyticsAutoTrackEventType.APP_START or
                    SensorsAnalyticsAutoTrackEventType.APP_END or
                    SensorsAnalyticsAutoTrackEventType.APP_VIEW_SCREEN or
                    SensorsAnalyticsAutoTrackEventType.APP_CLICK
        )
            .enableJavaScriptBridge(true) // 注册DevOps时提供该值
            .setAppIdFromDevOps("nJPbGvJG") // 注册DevOps时提供该值
            .setAppSecretFromDevOps("c9a671c45ef69a31d0954098019734403fcc58f5") // 设置 ui主线程阻塞阈值
            .setUIBlockThresholdMill(500) // 设置 全方法埋点，方法耗时阈值
            .setMethodCostThresholdMill(10) // 设置 http网络请求耗时阈值
            .setHttpCostThresholdMill(10) // 是否开启Log日志输出
            .enableLog(true)
        SensorsDataAPI.startWithConfigOptions(this, configOptions)
        SensorsDataAPI.sharedInstance(this).trackFragmentAppViewScreen()
        SensorsDataAPI.sharedInstance().enableHeatMap()
    }
}