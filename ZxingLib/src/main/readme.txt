
注：本"zxingmodule"库不具有通用性。

--- 说明 ---
2020-02-13 11:15:10

CaptureActivity：扫描主页
FrontLightMode：闪光等设置
BeepManager：震动、声音设置
DecodeFormatManager：条形码、二维码格式设置
DecodeThread：解码格式

在结束"扫描界面"时，会"突然闪一下"的问题，解决方式：加activity切换动画
--- 说明 ---


--- 大bug ---
2020-02-14 11:15:16

    Zxing 在"横屏状态"下可以扫描"二维码"+"条形码"；
    在"竖屏状态"仅能扫描"二维码"，无法扫描"条形码"；

    解决：https://blog.csdn.net/qq_38356174/article/details/89520544
    步骤：

    1. 在AndroidManifest.xml中，android:screenOrientation="portrait"是限制此页面竖屏显示。

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

    3. ###修改核心###
    CameraManager#buildLuminanceSource(){
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

--- 大bug ---