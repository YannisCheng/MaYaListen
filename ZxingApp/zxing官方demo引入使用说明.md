# 临时



# Google Zxing包

 - [Zxing直接引用，官方Demo使用、修改-参考](https://blog.csdn.net/weixin_36570478/article/details/83062172#CaptureActivity_105)

## 项目集成及修改说明

### 一、项目运行前待修改的问题

#### 1.运行项目前，需添加Camera动态权限申请

项目运行前需要添加Camera权限，否则提示错误弹窗。

#### 2.扫描后界面闪屏问题

在结束"扫描界面"时，会"突然闪一下"的问题，解决方式：加activity切换动画

#### 3.在竖屏下在仅能扫描"二维码"，无法扫描"条形码"问题

[问题解决参考步骤](https://blog.csdn.net/qq_38356174/article/details/89520544)
步骤：

1. 在 `AndroidManifest.xml` 中添加 `android:screenOrientation="portrait"` 是限制此页面竖屏显示。



2. CameraManager#getFramingRectInPreview(){
      if (screenResolution.x < screenResolution.y) {
        // 下面为竖屏模式
        rect.left = rect.left * cameraResolution.y / screenResolution.x;
        rect.right = rect.right * cameraResolution.y / screenResolution.x;
        rect.top = rect.top * cameraResolution.x / screenResolution.y;
        rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
      } else {
        // 下面为横屏模式
        rect.left = rect.left * cameraResolution.x / screenResolution.x;
        rect.right = rect.right * cameraResolution.x / screenResolution.x;
        rect.top = rect.top * cameraResolution.y / screenResolution.y;
        rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
      }
}


3. CameraManager#buildLuminanceSource(){
     Rect rect = getFramingRectInPreview();
     if (rect == null) {
         return null;
     }
     PlanarYUVLuminanceSource source;
     Point point = configManager.getScreenResolution();
     if (point.x < point.y) {
        byte[] rotatedData = new byte[data.length];
        int newWidth = height;
        int newHeight = width;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * newWidth + newWidth - 1 - y] = data[x + y * width];
        }

        source = new PlanarYUVLuminanceSource(rotatedData, newWidth, newHeight,
                rect.left, rect.top, rect.width(), rect.height(), false);
    } else {
        source = new PlanarYUVLuminanceSource(data, width, height,
                rect.left, rect.top, rect.width(), rect.height(), false);
    }
    return source;
}
 

4. CameraConfigurationUtils#findBestPreviewSizeValue(){
     double screenAspectRatio = 0;
     if (screenResolution.x < screenResolution.y) {
     	// 竖屏
     	screenAspectRatio = (double) screenResolution.y / (double) screenResolution.x;
     } else {
    	screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;
     }

    // 删除
    if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
            Point exactPoint = new Point(realWidth, realHeight);
            Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
            return exactPoint;
          }
}


### 二、项目工作流程分析

 1. 相机属性参数设置。
 2. 相机画面预览。
 
  [SurfaceView、SurfaceHolder、Surface与Camera](https://blog.csdn.net/Holmofy/article/details/66578852)
  
 3. 捕获相机预览帧数据。
  
  当Camera产生帧时，Camera.PreviewCallback#onPreviewFrame(byte[] data, Camera camera)的方法将被调用。这里的data就是我们需要的数据
  
 4. 处理并返回帧数据
 
Result rawResult = null;
PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);
if (source != null) {
	BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    try {
        rawResult = multiFormatReader.decodeWithState(bitmap);
    } catch (ReaderException re) {
        // continue
    } finally {
        multiFormatReader.reset();
    }
}


### 三、项目文件结构说明

1. CaptureActivity：扫描主页
 - CameraManager: 
  
  该对象包装了Camera服务对象，并期望是唯一与之对话的对象。 
  该实现封装了拍摄预览大小的图像所需的步骤，这些图像用于预览和解码。
  
 - CaptureActivityHandler: 
  
  此类处理构成状态机的所有消息传递，以进行捕获
  
 - InactivityTimer: 
  
  如果设备使用电池供电，则在一段时间不活动后结束活动
  
 - ViewfinderView: 
 
  该视图覆盖在摄像机预览的顶部。 
  它在其外部添加了 "取景器矩形" 和 "部分透明性"，以及 "激光扫描仪动画" 和 "结果点"。
  
 - BeepManager: 
 
  管理蜂鸣声和振动
  
 - AmbientLightManager: 
 
  检测环境光并在很暗时打开前灯，在足够亮时再次关闭
 
2. FrontLightMode：闪光等设置
3. BeepManager：震动、声音设置
4. DecodeFormatManager：条形码、二维码格式设置
5. DecodeThread：解码格式


