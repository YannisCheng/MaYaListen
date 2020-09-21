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
    
* [ ] 地图
    * [ ] google
    * [ ] 高德
    * [ ] 百度
 