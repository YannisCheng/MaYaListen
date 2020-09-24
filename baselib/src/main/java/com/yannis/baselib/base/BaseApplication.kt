package com.yannis.baselib.base

import android.app.Application
import com.tencent.bugly.Bugly
import com.yannis.maplib.MapInit

/**
 * 原bugly的App升级配置
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/9
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Bugly.init(getApplicationContext(), "f2acca239c", false);
        MapInit(this)
        // App安装后首次冷启动时调用预初始化函数
        //UmengInit.preInit(this)
        //UmengInit.initSetting(this)
    }
}