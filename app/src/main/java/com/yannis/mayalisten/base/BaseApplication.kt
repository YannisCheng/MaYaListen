package com.yannis.mayalisten.base

import android.app.Application
import com.tencent.bugly.Bugly

/**
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