
# Android自带音频/视频
[免费的音乐网站](https://freemusicarchive.org/)

## 目标

- 实现最简单的MediaPlayer
- 实现ExoPlayer

### ExoPlayer

[exoplayer.dev](https://exoplayer.dev/)

[exoplayer-blog](https://medium.com/google-exoplayer)

[ExoPlayer-GitHub](https://github.com/google/ExoPlayer)

## 5个Demo示例

- [ExoPlayer实验室。ExoPlayer是在 `Android YouTube` 应用中运行的视频播放器](https://developer.android.com/codelabs/exoplayer-intro#0)

- [`uamp` 通用音乐播放器使用 ExoPlayer 来播放本地音频](https://github.com/android/uamp.git)

- [`ExoPlayer` 官方代码库中包含一个演示应用，该应用展示了该库的许多高级功能](https://github.com/google/ExoPlayer.git)

-  [企业级应用中包含的实例](https://github.com/android/enterprise-samples/tree/master)

- `ijkplayer-exoplayer`

- [入门视频](https://www.youtube.com/watch?v=XQwe30cZffg)

## 里程

2020-11-18  媒体应用架构


## 各种场景
各种广播接收器

媒体按钮 mediabutton

媒体焦点 audiofocus

锁屏控件

通知

## UAMP项目解析：

### Kotlin语法
 - **函数：** [let、with、run、apply、also](https://www.jianshu.com/p/6e8d4912c576)
 - **协程：**[Job和SupervisorJob](https://www.jianshu.com/p/95aa7d074150)、[suspend](https://blog.csdn.net/qq_39969226/article/details/101058033)
 -  **延迟初始化：**[lateinit 和 by lazy](https://www.jianshu.com/p/e2cb4c65d4ff)


### 主要概念及其实现
- **MusicServiceConnection**
  - MediaBrowserCompat；
  - MediaBrowserCompat.ConnectionCallback（作为MediaBrowserCompat的构造参数）
  - MediaControllerCompat（在MediaBrowserCompat.ConnectionCallback.onConnected()初始化）
  - MediaControllerCompat.Callback()（作为MediaControllerCompat的注册回调的参数）
  -  以及向外界提供各种连接状态通知

```
管理与MediaBrowserServiceCompat实例的连接的类，通常是MusicService或其子类之一。
通常，最好使用DI或像UAMP一样在app模块中使用InjectorUtils来构造/注入依赖项。 这里有一些难点：

1. MediaBrowserCompat是最终类，因此很难对其进行模拟。
2. 一个MediaBrowserConnectionCallback是构造MediaBrowserCompat的参数成，并且提供回调此类。
3. MediaBrowserCompat.ConnectionCallback.onConnected是构造MediaControllerCompat的最佳位置，该控件将用于控制MediaSessionCompat 。
由于这些原因，而不是构造其他类，因此将其视为黑匣子（这就是为什么这里的逻辑很少）的原因。
这也是为什么构造MusicServiceConnection的参数是简单参数而不是私有属性的原因。 仅需要它们来构建MediaBrowserConnectionCallback和MediaBrowserCompat对象。
```

- **MusicService : MediaBrowserServiceCompat**

```
此类是从APP的UI和希望通过UAMP播放音乐的其他应用（例如Android Auto或Google Assistant）浏览和播放命令的入口点。
浏览从MusicService.onGetRoot方法开始，并在回调MusicService.onLoadChildren中继续。
```
