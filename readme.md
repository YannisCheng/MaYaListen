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

* [ ] 社会化
    * [ ] 登录
    * [ ] 分享
    
* [ ] 手机号
    * [ ] 登录
    * [ ] 修改密码
    * [ ] 找回密码
    
* [ ] 支付
    * [ ] 微信
    * [ ] 支付宝
    
* [ ] 换肤
    * [ ] Android10夜间模式
    * [ ] [QMUI](https://github.com/Tencent/QMUI_Android/wiki/QMUI-%E6%8D%A2%E8%82%A4)
    
### 地图
    * [ ] google
[百度-我的应用](http://lbsyun.baidu.com/apiconsole/key#/home)
[高德-我的应用](https://console.amap.com/dev/key/app)
    
### maven搭建本地私有库
 - 地址：http://localhost:8082/
 - 用户名：admin
 - 密码：admin123
 - 服务开启：stanex
 - 服务关闭：stonex
 
 ### 获取签名文件密钥信息
 
 keytool -list -v -keystore key路径