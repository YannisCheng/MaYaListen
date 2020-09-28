package com.yannis.sociallib

import android.content.Context
import android.content.Intent
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.UMShareAPI

/**
 * 友盟初始化工具类，供 BaseApplication使用
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/24
 */
open class UmengInit() {

    companion object {

        fun preInit(context: Context) {
            // SDK预初始化函数
            // preInit预初始化函数耗时极少，不会影响App首次冷启动用户体验
            UMConfigure.preInit(context, "5f6c4d0e906ad811171397da", "umeng")
        }

        fun initSetting(context: Context) {
            /**
             * 1、设置组件化的Log开关
             *
             * 参数: boolean 默认为false，如需查看LOG设置为true
             */
            UMConfigure.setLogEnabled(true)
            /**
             * 2、初始化SDK
             *
             * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
             * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
             */
            UMConfigure.init(
                context,
                "5f6c4d0e906ad811171397da",
                "umeng",
                UMConfigure.DEVICE_TYPE_PHONE,
                ""
            )

            /**
             * 3、统计：页面采集
             *
             * 选择AUTO页面采集模式，统计SDK基础指标无需手动埋点可自动采集。
             * 建议在宿主App的Application.onCreate函数中调用此函数。
             * 注意：选择AUTO页面采集模式后，所有Activity中都不能调用 'MobclickAgent.onResume和onPause' 方法
             */
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

            /**
             * 4、分享：设置各个平台的appkey
             */
            // 微信
            //PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0")
            // QQ
            //PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")
            // 支付宝
            //PlatformConfig.setAlipay("2015111700822536")
            // 新浪微博
            //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        }

        /**
         * QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中。
         * 注意: onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
         */
        fun QQCallBack(context: Context, requestCode: Int, resultCode: Int, data: Intent) {
            UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data)
        }
    }
}