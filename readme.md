
##  问题Log：

### 1. tinker


```
 'build.gradle(:app)'
 // 依赖插件脚本
 //apply from: 'tinker-support.gradle'
 存在问题：No such property: variantConfiguration for class: com.android.build.gradle.internal.variant.ApplicationVariantData
```
 

### 2. kapt An exception occurred: android.databinding.tool.util.LoggedErrorException
 问题原因：在使用databinding时，在xml文件中进行赋值时同时使用了：“中文”+表达式 造成这样的问题

[解决方式](https://www.jianshu.com/p/0d72f4a55c32)


## 开发常用功能Kit

### 1. 移除git中已将添加到版本控制中的文件

```
git  rm  -rf  --cached .idea/
```

### 2. 在终端中查看具体的 Android编译问题

```
gradlew clean build  --stacktrace
```   
 
### 3. 获取签名文件密钥信息
 
```
keytool -list -v -keystore key 路径
``` 

## 项目集成     
    
### 1. maven搭建本地私有库

 - 地址：http://localhost:8082/
 - 用户名：admin
 - 密码：admin123
 - 服务开启：stanex
 - 服务关闭：stonex
 
### 2. Jenkins配置 

 [报错：Can't retrieve the Gradle executable 解决](https://blog.csdn.net/lamar_quin/article/details/96129427)
    
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
 
> 参考：https://blog.csdn.net/axi295309066/article/details/52901991
 
### 1. 开放平台
  
 - [支付宝开放平台](https://openhome.alipay.com/platform/home.htm)
 - [腾讯开放平台](https://open.tencent.com/)
 - [友盟](https://www.umeng.com/)
 
### 2. 统计、分享
  
 - [友盟-统计SDK](https://developer.umeng.com/docs/119267/detail/182050)
 - [友盟-分享SDK](https://developer.umeng.com/docs/128606/detail/182094)
 
### 3. 登录/智能认证
 
 - [友盟-智能认证SDK](https://developer.umeng.com/docs/143070/detail/182078)
 
### 4. 推送
 
 - [小米推送](https://dev.mi.com/console/appservice/push.html)
 - [华为推动](https://developer.huawei.com/consumer/cn/hms/huawei-pushkit)
 - [X不针对个人开发者：vivo推送](https://dev.vivo.com.cn/promotion/appPromotion)
 - [OPPO推送](https://open.oppomobile.com/newservice/capability?pagename=push)
 - [魅族推送](https://open.flyme.cn/open-web/views/push.html)
 - [友盟-推送SDK](https://developer.umeng.com/docs/67966/detail/179087)
 
### 5. bug测试平台
 
 - [bugly测试平台]()
 
 
### 6. 地图
 
 - [百度-我的应用](http://lbsyun.baidu.com/apiconsole/key#/home)
 - [高德-我的应用](https://console.amap.com/dev/key/app)
  
  ### 语音
   
 - [科大讯飞-语音听写](https://www.xfyun.cn/doc/asr/voicedictation/Android-SDK.html)
   
### 7. 即时通信
   
 - [网易云信](https://app.yunxin.163.com/index?#/)
   
### 8. 云存储
          
 - [阿里云-对象存储](https://www.aliyun.com/product/oss?spm=5176.12825654.eofdhaal5.100.24d22c4a0vRi31)
 - [阿里云-域名解析](https://dns.console.aliyun.com/?spm=5176.100251.111252.22.e86c4f15ZFpIoq#/dns/domainList)
 - [阿里云-域名-列表](https://dc.console.aliyun.com/next/index?spm=5176.100251.recommends.ddomain.32614f153Kxuap#/domain/list/all-domain)
 
 - [七牛-对象存储](https://www.qiniu.com/products/kodo)
 - [七牛-对象存储-空间管理](https://portal.qiniu.com/kodo/bucket/overview?bucketName=mayalisten)
 - [七牛-cdn-域名管理-列表](https://portal.qiniu.com/cdn/domain)
 - [七牛-token认证实现-Java版](https://github.com/qiniu/java-sdk/blob/master/src/main/java/com/qiniu/util/Auth.java)

  
 * [ ] 支付
     * [ ] 微信
     * [ ] 支付宝
     
 * [ ] 性能检测
     * [ ] 
     
 * [ ] 广告平台
     * [ ]
     
 * [ ] 视频播放
     * [ ] ijkplayer
  
## App后续流程

### 1. 应用上架/App市场
 
 > 常用App市场地址总结：http://www.opp2.com/128243.html

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
 
### 2. 多渠道打包
 
[美团多渠道打包使用](https://blog.csdn.net/u014449096/article/details/89338517)
 
`美团多渠道打包`的运行位置：`Gradle/MaYaListen/app/Tasks/package/`，命令 
 
```
assembleReleaseChannels 
```

### 3. 加固 

 [ ]  安全加密、加固
     * [ ] 360
 
## 第三方框架
 
 [Android项目中用得最多最火的第三方框架](https://www.cnblogs.com/jingping/p/10471056.html)
 
### 1. 文件下载
 
 [liulishuo-FileDownloader](https://github.com/lingochamp/FileDownloader)
 
### 2. 原生折线绘制
    
 [官方文档](https://weeklycoding.com/mpandroidchart-documentation/)
 [MPAndroid使用文档](https://www.jianshu.com/p/fc73b490edd5)   
 [使用基础概念总结](https://www.jianshu.com/p/cef974ae463c)
   
 - 对于图表关键需要知道并理解的是：图、数据（LineData）、数据集（LineDataSet）以及 Entry，这是定义并显示图表的关键概念。它们的关系是：Entry -> 数据集 -> 数据 -> 图表。
   
 - 如果要对 X 轴和 Y 轴进行设置可分别通过 XAxis 和 YAxis 进行设置
   
 - 如果要对数据进行设置，则通过 DataSet 进行设置
   
 - 如果要设置手势等，可通过图表 Chart 进行设置
     
## 正常开发流程外的耗时环节总结：
 
### 1. 前期：第三方平台接入   
 
#### 1.1 平台认证、审核，App注册
 
 - 实名认证（针对公司认证）
  - 公司本身的各种证件（如果没有，注册）
  - 特殊App业务需求，需要特殊的资格证书、资质（如果没有，注册）
  - 实控人认证（公司领导、法人）
  
 - 审核（资质、证书、信息） 
 
 - App平台注册
  - App SHA1值
  - icon
  - App网站地址Url
  - App流程、运行的"图+文"资料（各种声明、隐私html）
  
#### 1.2 App接入SDK
  
 - 初始化
  - 依赖导入
  - 权限处理

 - 流程梳理（本身调用流程，前台、后台流程梳理）
 
### 2. 后期：各个App应用市场发布上架
 
#### 2.1 资质审核

  - 公司
   - 营业执照
   - ICP备案
   - 计算机软件著作权登记证书
   - 电子软著
   - 承诺（免责）书
   - App安全审核书
   - 特殊行业需要特殊资质证明
   
  - 个人认证、资料
 
 - 图片资料整理、准备
  - icon
  - 截图
  
 - 文字资料整理、准备
  - App一句话介绍
  - App简介
  - 更新说明
 
 - 各个平台自身审核特点（严厉程度）
 
 ### 问题
 
 - [x] 多渠道打包、jenkins打包区分
 
 