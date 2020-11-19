
# ijkplayer-android项目

## 项目框架整体认识

### 概述
[Ijkplayer-Android项目框架](https://blog.csdn.net/weixin_39799839/article/details/79186034?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.pc_relevant_is_cache&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.pc_relevant_is_cache)

ijkplayer底层是基于FFplay的，FFplay是FFmpeg项目提供的播放器示例。
因此要分析ijkplayer的底层处理流程首先需要了解ffplay的代码处理流程。
ijkplayer在底层重写了ffplay.c文件。其中去除了ffplay中使用"sdl音视频库"播放音视频的部分；增加了对移动端的硬件解码部分、视频渲染部分、音频播放的部分实现，其中ijkplayer不支持硬件音频解码。

### ijkplayer-android项目文件目录：

```
 ├── android    -android平台上的上层接口封装以及平台相关方法
 │   ├── contrib
 │   ├── ijkplayer
 │   └── patches
 ├── config     -编译ffmpeg使用的配置文件
 ├── doc
 ├── extra  -存放编译ijkplayer所需的依赖源文件, 如ffmpeg、openssl等
 │   ├── ffmpeg
 │   ├── libyuv
 │   ├── openssl
 │   └── soundtouch
 ├── ijkmedia   -核心代码
 │   ├── ijkj4a     -android平台下使用，用来实现c代码调用java层代码，这个文件夹是通过bilibili的另一个开源项目jni4android自动生成的。
 │   ├── ijkplayer      -播放器数据下载及解码相关
 │   ├── ijksdl     -音视频数据渲染相关
 │   ├── ijksoundtouch
 │   └── ijkyuv
 ├── ijkprof
 │   └── android-ndk-profiler-dummy
 ├── ios    -iOS平台上的上层接口封装以及平台相关方法
 └── tools    -初始化项目工程脚本
     └── copyrighter
```

- - - - - - - - - - - - - - - - - - - - - - - - - -

### 项目使用主要入口文件

  IjkPlayer从使用角度上主要有五个文件分别是:
  
   
  - IMediaPlayer：接口
  - AbstractMediaPlayer：实现接口的抽象类
  - AndroidMediaPlayer：实现抽象类的播放器类-使用Android系统的编解码对视屏进行操作
  - IjkMediaPlayer：实现抽象类的播放器类-使用Ijkplayer对视屏进行编解码处理。
  - IjkExoMediaPlayer：实现抽象类的播放器类-使用ExoPlayer对视屏进行处理。
  
  其中：
  
  - IjkExoMediaPlayer是一个独立的库文件，其他4个为同一个库文件。
  - AndroidMediaPlayer、IjkMediaPlayer、IjkExoMediaPlayer三个类都是独立的播放器，都是对IMediaPlayer的实现。
  - IjkMediaPlayer是使用ijkplayer底层库处理视频
 
  
  - - - - - - - - - - - - - - - - - - - - - - - - - -
  
  [IMediaPlayer 接口](https://github.com/bilibili/ijkplayer/blob/master/android/ijkplayer/ijkplayer-java/src/main/java/tv/danmaku/ijk/media/player/IMediaPlayer.java)
  定义了一个播放器的类应该需要实现的基础功能的接口，所以播放视屏的功能函数在这里都能找到。
  
  [AbstractMediaPlayer](https://github.com/bilibili/ijkplayer/blob/master/android/ijkplayer/ijkplayer-java/src/main/java/tv/danmaku/ijk/media/player/AbstractMediaPlayer.java) 抽象类
  实现了IMediaPlayer接口，将播放器通用的响应事件进行了统一实现，但是主要功能任然需要子类实现。播放过程中的事件的操作就可以在这里查。
  
  [AndroidMediaPlayer](https://github.com/bilibili/ijkplayer/blob/master/android/ijkplayer/ijkplayer-java/src/main/java/tv/danmaku/ijk/media/player/AndroidMediaPlayer.java)
  继承AbstractMediaPlayer，使用Android系统的编解码对视屏进行操作。
  
 ### ijkplayer播放器初始化
 
 初始化入口方法：IjkMediaPlayer#initPlayer()

 ```
     private void initPlayer(IjkLibLoader libLoader) {
         // 加载本地库：ijkffmpeg、ijksdl、ijkplayer
         loadLibrariesOnce(libLoader);
         // 在此方法中调用 native方法 native_init()，初始化已加载的本地库。
         initNativeOnce();
         
         // 通过Android平台的Handler处理Looper中遍历由native发送的指定消息（message）
         Looper looper;
         if ((looper = Looper.myLooper()) != null) {
             mEventHandler = new EventHandler(this, looper);
         } else if ((looper = Looper.getMainLooper()) != null) {
             mEventHandler = new EventHandler(this, looper);
         } else {
             mEventHandler = null;
         }
 
         /*
          * Native setup requires a weak reference to our object. It's easier to
          * create it here than in C++.
          * 本机设置需要对我们的对象的弱引用。 在这里创建它比在C ++中更容易。
          */
         native_setup(new WeakReference<IjkMediaPlayer>(this));
     }
 ```
  
  