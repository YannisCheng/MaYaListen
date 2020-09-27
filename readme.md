- 'build.gradle(:app)'
 // 依赖插件脚本
 //apply from: 'tinker-support.gradle'
 存在问题：No such property: variantConfiguration for class: com.android.build.gradle.internal.variant.ApplicationVariantData
 
 问题：
 ### [kapt] An exception occurred: android.databinding.tool.util.LoggedErrorException
 问题原因：在使用databinding时，在xml文件中进行赋值时同时使用了：“中文”+表达式 造成这样的问题
 >> 解决方式：https://www.jianshu.com/p/0d72f4a55c32

 ### 移除git中已将添加到版本控制中的文件
>> git  rm  -rf  --cached .idea/

 ### 在终端中查看具体的 Android编译问题
>> gradlew clean build  --stacktrace
    
 
     
    
### maven搭建本地私有库
 - 地址：http://localhost:8082/
 - 用户名：admin
 - 密码：admin123
 - 服务开启：stanex
 - 服务关闭：stonex
 
 ### 获取签名文件密钥信息
 
 keytool -list -v -keystore key 路径
 
 ---  
 
 [Android项目中用得最多最火的第三方框架](https://www.cnblogs.com/jingping/p/10471056.html)
    
## 换肤
    
   ### 1.官方黑暗模式  
   [Android10黑暗模式](https://juejin.im/post/6844904173788463112，https://www.jianshu.com/p/b34ee4e75c53)
    
   **方式1：Force Dark方式**
   从API23后，Android就有自带的api能够实现夜间模式与白天模式的切换，用到的就是AppCompatDelegate.setDefaultNightMode。当然这种只能实现白天与黑夜的切换
   
   1. style设置'<item name="android:forceDarkAllowed">true</item>'时，配合使用'android:configChanges="uiMode"'可以不走onCreate()方法，同时目录下不能有'res/values-night'文件；
   2. 手动调用：AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)，需要配合重写：onConfigurationChanged()。
    
   **方式2：DayNight方式**
    设置：'res/values-night'文件及对应资源。
    
   ### 2.QMUI
   [QMUI](https://github.com/Tencent/QMUI_Android/wiki/QMUI-%E6%8D%A2%E8%82%A4)

 
 
 ## App社会化
 
 >> 参考：https://blog.csdn.net/axi295309066/article/details/52901991
 
 ### 开放平台
  
  - [支付宝开放平台](https://openhome.alipay.com/platform/home.htm)
  - [腾讯开放平台](https://open.tencent.com/)
  - [友盟](https://www.umeng.com/)
 
 ### 统计、分享
  
 - [友盟-统计SDK](https://developer.umeng.com/docs/119267/detail/182050)
 - [友盟-分享SDK](https://developer.umeng.com/docs/128606/detail/182094)
 
 ### 登录/智能认证
 
 - [友盟-智能认证SDK](https://developer.umeng.com/docs/143070/detail/182078)
 
 ### 推送
 
 - [小米推送](https://dev.mi.com/console/appservice/push.html)
 - [华为推动](https://developer.huawei.com/consumer/cn/hms/huawei-pushkit)
 - [X不针对个人开发者：vivo推送](https://dev.vivo.com.cn/promotion/appPromotion)
 - [OPPO推送](https://open.oppomobile.com/newservice/capability?pagename=push)
 - [魅族推送](https://open.flyme.cn/open-web/views/push.html)
 - [友盟-推送SDK](https://developer.umeng.com/docs/67966/detail/179087)
 
 ### bug测试平台
 
 - [bugly测试平台]()
 
 ### 地图
 
  - [百度-我的应用](http://lbsyun.baidu.com/apiconsole/key#/home)
  - [高德-我的应用](https://console.amap.com/dev/key/app)
  
 ### 应用上架/App市场
 
 >> 参考：http://www.opp2.com/128243.html

 - [腾讯开放平台-应用宝](https://wiki.open.qq.com/wiki/%E9%A6%96%E9%A1%B5)
 - [华为开发者社区-华为应用市场](https://developer.huawei.com/consumer/cn/appgallery/)
 - [小米开发平台-小米应用商店](https://dev.mi.com/console/app/phone.html)
 - [OPPO开放平台-OPPO软件商店](https://open.oppomobile.com/newservice/capability?pagename=app_store)
 - [X不针对个人开发者：vivo开发者平台-vivo手机助手](https://dev.vivo.com.cn/distribute/appStore)
 - [魅族开放平台-魅族应用商店](http://open.flyme.cn/openNew/application.html)
 - [阿里应用分发开放平台-豌豆荚、PP助手](http://open.uc.cn/login)
 - [360移动开放平台-360手机助手](http://dev.360.cn/)
 - [百度开放平台-百度手机助手](https://app.baidu.com/)
 - [三星开发者平台-三星应用商店](https://developer.samsung.com/galaxy-store)
 
 ### 语音
   
   - [科大讯飞-语音听写](https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html)
   
 ### 即时通信
   
   - [网易云信](https://app.yunxin.163.com/index?#/)
 
 * [ ] 支付
     * [ ] 微信
     * [ ] 支付宝
     
 * [ ] 性能检测
     * [ ] 
     
  * [ ] 广告平台
      * [ ]
     
 * [ ] 安全加密、加固
     * [ ] 360

 * [ ] 云服务器
     * [ ] 阿里云、七牛
     

     
     
     
     