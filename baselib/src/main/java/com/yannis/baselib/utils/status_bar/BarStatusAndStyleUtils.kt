package com.yannis.baselib.utils.status_bar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager


/**
 * BarStatusAndStyleUtils： 顶部状态栏设置与底部导航栏设置
 *
 * fitSystemWindows属性：
 * 用于根据系统窗口（例如状态栏）调整视图布局。
 * 如果为true，则调整此视图的填充以为系统窗口留出空间。
 * 仅当此视图处于非嵌入式活动中时才会生效。
 *
 * 参考：https://github.com/xiewenfeng/statusbartextcolorchange
 * 参考：https://blog.csdn.net/u014418171/article/details/81223681?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */
object BarStatusAndStyleUtils {


    /**
     * 判断是否存在NavigationBar
     */
    @SuppressLint("PrivateApi")
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar: Boolean = false
        val resources = context.resources
        val identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        if (identifier > 0) {
            hasNavigationBar == resources.getBoolean(identifier)
        }

        val forName = Class.forName("android.os.SystemProperties")
        val method = forName.getMethod("get", String::class.java)
        val navBarOverride: String = method.invoke(forName, "qemu.hw.mainkeys") as String
        if ("1" == navBarOverride) {
            hasNavigationBar = false
        } else if ("0" == navBarOverride) {
            hasNavigationBar = true
        }
        return hasNavigationBar
    }

    /**
     * 获取底部导航栏高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(identifier)
    }

    /**
     * 获取顶部状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(identifier)
    }

    /**
     * 设置透明状态栏
     */
    fun setTranslucentStatus(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 是从API 16开始启用，实现效果： 视图延伸至状态栏区域，状态栏悬浮于视图之上
            // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 是从 API 23开始启用，实现效果： 设置状态栏图标和状态栏文字颜色为深色，为适应状态栏背景为浅色调
            // 该Flag只有在使用了 FLAG_DRWS_SYSTEM_BAR_BACKGROUNDS，并且没有使用 FLAG_TRANSLUCENT_STATUS 时才有效，即只有在透明状态栏时才有效。
            val option: Int =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色，支持5.0以上版本
     */
    fun setStatusBarColor(activity: Activity, colorId: Int) {
        // 只做5.0以上的适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = colorId
        }
    }

    /**
     * 代码实现: <android:fitsSystemWindows>
     */
    fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
            if (viewGroup.childCount > 0) {
                val rootView: ViewGroup = viewGroup.getChildAt(0) as ViewGroup
                if (rootView != null) {
                    rootView.fitsSystemWindows = fitSystemWindows
                }
            }
        }
    }

    /**
     * 设置6.0 状态栏深色浅色切换
     */
    fun setStatusBarDarkTheme(activity: Activity, isDark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            if (decorView != null) {
                var vis = decorView.systemUiVisibility
                vis = if (isDark) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                if (decorView.systemUiVisibility != vis) {
                    decorView.systemUiVisibility = vis
                }
                return true
            }
        }
        return false
    }

}