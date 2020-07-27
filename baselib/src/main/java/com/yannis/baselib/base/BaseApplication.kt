package com.yannis.baselib.base

import android.app.Application
import com.tencent.bugly.Bugly

/**
 * 原bugly的App升级配置
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/9
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Bugly.init(getApplicationContext(), "f2acca239c", false);
    }
}