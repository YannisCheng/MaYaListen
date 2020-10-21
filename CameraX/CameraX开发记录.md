# Android-CameraX

[Android-Camera基础知识储备](https://www.jianshu.com/p/f8d0d1467584)
[Android-Camera旋转角度分析](https://www.jianshu.com/p/3ba0dee1732f?utm_source=desktop)
Android端相机API演变过程：Camera、Camera2、CameraX。
CameraX 是一个 Jetpack 支持库，旨在帮助您简化相机应用的开发工作。

## 权限：
 
  - CAMERA
  - WRITE_EXTERNAL_STORAGE
  
## CameraX特点：

 - CameraX中已经引入的基本用例的易用性：
  - [预览](https://developer.android.com/training/camerax/preview)
  - [分析](https://developer.android.com/training/camerax/analyze)
  - [拍摄](https://developer.android.com/training/camerax/take-photo)，不仅提供简单的相机手动控制功能，还提供自动白平衡、自动曝光和自动对焦 (3A) 功能
   
 - 利用Camera2功能
 - 易用、向后兼容至5.0。
 - 基本用例的方法具有生命周期感知能力
 - [CameraX Extensions](https://developer.android.com/training/camerax/vendor-extensions) 包括人像、HDR、夜间模式和美颜
 - 确保在各个设备上的一致性：包括宽高比、屏幕方向、旋转角度、预览大小和高分辨率图片大小
 
  
 使用最低版本要求
 
  - Android API 级别 21
  - Android 架构组件 1.1.1
  - 对于具有生命周期感知能力的 Activity，请使用 FragmentActivity 或 AppCompatActivity。
 
## Camera基本术语
 
 [术语参考](https://developer.android.com/training/camerax/orientation-rotation)
 
 - 用例（case）
 - 用例配置
 - 方向
  - 自然屏幕方向：指设备哪一侧朝上，可为以下4个值之一：纵向、横向、反向纵向或反向横向。手机的自然方向是portrait（竖屏），平板的自然方向是landscape（横屏）
  - 当前屏幕方向：相对于`自然屏幕方向`的方向
  - 屏幕旋转角度（逆时针）：这是 Display.getRotation() 返回的值，表示设备从其自然屏幕方向逆时针旋转的角度值
  - 目标旋转角度（顺时针）：表示顺时针旋转设备使其达到自然屏幕方向需要旋转的度数。
  - 相机（图像）传感器方向：传感器方向被定义为一个常量值，表示当设备处于自然位置时，相对于设备顶部，传感器旋转的角度（0、90、180、270）。
  - 图片旋转角度
 - 尺寸
  - 预览区域尺寸
  - 成像区域尺寸
 - 成像比例


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
