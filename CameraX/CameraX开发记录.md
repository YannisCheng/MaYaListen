# CameraX

CameraX 是一个 Jetpack 支持库，旨在帮助您简化相机应用的开发工作。

## 概览

 - 用例（case）
 - 配置

## 权限：

 - CAMERA
 - WRITE_EXTERNAL_STORAGE
 
## 使用最低版本要求：

 - Android API 级别 21
 - Android 架构组件 1.1.1
 - 对于具有生命周期感知能力的 Activity，请使用 FragmentActivity 或 AppCompatActivity。

## CameraX特点：

 - CameraX中已经引入的基本用例的易用性：
  - [预览](https://developer.android.com/training/camerax/preview)
  - [分析](https://developer.android.com/training/camerax/analyze)
  - [拍摄](https://developer.android.com/training/camerax/take-photo)
   
 - 利用Camera2功能
 - 易用、向后兼容至5.0。
 - 基本用例的方法具有生命周期感知能力
 - [CameraX Extensions](https://developer.android.com/training/camerax/vendor-extensions) 包括人像、HDR、夜间模式和美颜
 - 确保在各个设备上的一致性：包括宽高比、屏幕方向、旋转角度、预览大小和高分辨率图片大小
 
## 架构

### CameraX结构
   
  在CameraX中通过借助名为 用例 这个抽象的概念，与硬件设备进行交互。
  
  - 预览：接受用于显示预览的 Surface
  - 分析：提供 CPU 可访问的缓冲区以进行分析（例如进行机器学习）。
  - 拍照：拍摄并保存
  
不同用例可以相互组合使用，也可以同时处于活动状态。例如：在预览相机采集的画面的同时，对采集的画面进行分析，同时也可以实时保存当前画面。

### API使用模型

 - 具有配置选项的所需用例
 - 通过附加监听器来指定如何处理输出数据
 - 将用例绑定到 [Android架构生命周期](https://developer.android.com/topic/libraries/architecture)来指定目标流程
 
### CameraX生命周期

#### CameraX基本生命周期

 - CameraX 会通过观察生命周期来确定：何时打开相机、何时创建拍摄会话以及何时停止和关闭。
 - 用例 API 提供：方法调用和回调来监控进度情况。
   
#### CameraX中具体用例与生命周期：
 
 - 多个组合用例可以绑定至单一生命周期中；
 - 当App中需要支持"无法组合的用例"时，可以执行以下操作：
  - 将兼容的用例划分至多个Fragment中，然后在fragment中进行切换
  - 创建自定义生命周期组件，并用"自定义的生命周期组件"手动控制相机的生命周期
