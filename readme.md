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
 