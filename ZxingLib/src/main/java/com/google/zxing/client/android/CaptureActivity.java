/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.ResultHandlerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    // 报废界面常量tag值
    public int flowBaofei = 0;
    // 主界面扫描tag值
    public int mainScan = 0;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler = null;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private ImageView ivBack;
    private ArrayList<String> scanResultList = new ArrayList<>();
    /**
     * 是否为单次扫描：true 单次扫描；false 连续扫描
     */
    private boolean isSingleScan = true;
    /**
     * 流程、报废相关参数
     */
    private int flowTag = 0;
    private String intentStr = "";
    private String intentStrAdd = "";
    // 登记-新增条码
    private int addBarCode = 0;
    private boolean isLightOpen = false;

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(),
                    scaleFactor * a.getY(),
                    scaleFactor * b.getX(),
                    scaleFactor * b.getY(),
                    paint);
        }
    }

    /**
     * 由"发起器具流程-数据获取、展示界面"进入扫描界面
     */
    public static void startForResult(Activity context, int flowScanRequestCode, boolean isSingleScan) {
        Intent starter = new Intent(context, CaptureActivity.class);
        starter.putExtra("is_single_scan", isSingleScan);
        // 由回调扫描界面 传递的区别参数
        starter.putExtra("start_tag", flowScanRequestCode);
        context.startActivityForResult(starter, flowScanRequestCode);
    }

    /**
     * 由"报废"进入扫描界面
     */
    public static void start(Context context, int flowTag) {
        Intent starter = new Intent(context, CaptureActivity.class);
        starter.putExtra("start_tag", flowTag);
        context.startActivity(starter);
    }

    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // 保持屏幕常亮
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setStatusBarTransparent();
        setContentView(R.layout.capture);
        isSingleScan = getIntent().getBooleanExtra("is_single_scan", true);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
        // 设置选项
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getMateData();
        scanResultList.clear();
    }

    /**
     * 设置透明状态栏
     */
    private void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取流程、报废界面 相关数据
     */
    private void getMateData() {
        flowTag = getIntent().getIntExtra("start_tag", 0x01);
        try {
            Class<?> aClass = Class.forName("com.pansoft.gasguard.Utils.ConstantGasUtils");
            flowBaofei = aClass.getField("FLOW_BAOFEI").getInt("FLOW_BAOFEI");
            mainScan = aClass.getField("MAIN_SCAN").getInt("MAIN_SCAN");
            // 登记相关扫描结果返回
            addBarCode = aClass.getField("ADD_BARCODE_SCAN").getInt("ADD_BARCODE_SCAN");
            intentStr = aClass.getField("FLOW_SCAN_INTENT_STR").get("FLOW_SCAN_INTENT_STR").toString();
            // 登记相关扫描结果返回
            intentStrAdd = aClass.getField("ADD_BARCODE_SCAN_INTENT_STR").get("ADD_BARCODE_SCAN_INTENT_STR").toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        viewfinderView.setVisibility(View.VISIBLE);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        // 闪光灯开、关
        setCameraLight();
        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);
        inactivityTimer.onResume();
        decodeFormats = null;
        characterSet = null;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    private void setCameraLight() {
        final TextView lightView = (TextView) findViewById(R.id.flash_light);
        //final ImageView lightImage = (ImageView)findViewById(R.id.iv_light);
        lightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOpen) {
                    isLightOpen = false;
                    cameraManager.setTorch(false);
                    lightView.setText("开启闪光灯");
                    //lightImage.setBackgroundResource(R.drawable.flash_off);
                } else {
                    isLightOpen = true;
                    cameraManager.setTorch(true);
                    lightView.setText("关闭闪光灯");
                    //lightImage.setBackgroundResource(R.drawable.flash_on);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        beepManager.playBeepSoundAndVibrate();
        drawResultPoints(barcode, scaleFactor, rawResult);
        handleDecodeInternally(resultHandler);
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
     *
     * @param barcode     A bitmap of the captured image.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param rawResult   The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4 &&
                    (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
                            rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    // Put up our own UI for how to handle the decoded contents.
    private void handleDecodeInternally(ResultHandler resultHandler) {
        if (flowTag == flowBaofei) {
            // 报废界面
            setStartIntentContent(resultHandler, "com.pansoft.gasguard.ui.registration.InvalidShowActivityGas");
        } else if (flowTag == mainScan) {
            // 设备扫描界面
            setStartIntentContent(resultHandler, "com.pansoft.gasguard.ui.scan_appliacnce_info.ScanApplianceInfoActivity");
        } else {
            if (isSingleScan) {
                // 单次扫描结束
                setIntentData(resultHandler.getDisplayContents());
            } else {
                // 连续扫描
                dialogNotice(resultHandler);
            }
        }
    }

    private void setStartIntentContent(ResultHandler resultHandler, String s) {
        Class activityClass = null;
        try {
            activityClass = Class.forName(s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent starter = new Intent(CaptureActivity.this, activityClass);
        starter.putExtra("data", resultHandler.getDisplayContents());
        startActivity(starter);
        finish();
    }


    /**
     * 连续扫描dialog提示
     *
     * @param resultHandler 单次扫描结果
     */
    public void dialogNotice(final ResultHandler resultHandler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("扫描结果：" + resultHandler.getDisplayContents());
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //取消操作
                if (handler != null) {
                    handler.sendEmptyMessageDelayed(R.id.restart_preview, 0L);
                }
            }
        });
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 添加操作
                scanResultList.add(resultHandler.getDisplayContents().toString());
                if (handler != null) {
                    handler.sendEmptyMessageDelayed(R.id.restart_preview, 0L);
                }
            }
        }).create();
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 流程：数据展示、操作界面
     */
    private void setIntentData(CharSequence displayContents) {
        Intent intent = new Intent();
        if (displayContents != null) {
            if (flowTag == addBarCode) {
                // 登记相关扫描结果返回
                intent.putExtra(intentStrAdd, displayContents);
            } else {
                intent.putExtra(intentStr, displayContents);
            }
            setResult(2, intent);
        } else {
            setResult(2, null);
        }
        finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.scan_title));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public void onClick(View v) {
        backClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                backClick();
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回 监听
     */
    public void backClick() {
        if (flowTag == flowBaofei) {
            finish();
        } else {
            setIntentData(null);
        }
    }
}
