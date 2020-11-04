package com.yannis.cameraxdemo

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat


/**
 * GestureTouchSimpleActivity.kt
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/10/26 - 20:02
 */
class GestureTouchSimpleActivity : AppCompatActivity()/*, GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener*/ {

    private val TAG = "GestureTouchSimpleActiv"
    private lateinit var gestureDetector: GestureDetectorCompat
    private var velocityTracker: VelocityTracker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_touch_simple)
        /*gestureDetector = GestureDetectorCompat(this, this)
        gestureDetector.setOnDoubleTapListener(this)*/
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /*return if (gestureDetector.onTouchEvent(event))
            true else {*/
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker?.clear()
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                velocityTracker?.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(1000)
                    Log.e(TAG, "onTouchEvent x velocity : ${getXVelocity(pointerId)}")
                    Log.e(TAG, "onTouchEvent y velocity : ${getYVelocity(pointerId)}")
                }
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }
        return true

        /*}*/

    }

/*    override fun onDown(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDown: $e")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.e(TAG, "onShowPress: $e")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.e(TAG, "onSingleTapUp: $e")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.e(TAG, "onScroll: $e1,$e2")
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.e(TAG, "onLongPress: $e")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.e(TAG, "onFling: $e1,$e2")
        return true
    }

    */
    /**
     * 单击事件实现
     *//*
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Log.e(TAG, "onSingleTapConfirmed: $e")
        return true
    }

    */
    /**
     * 双击事件实现
     *//*
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDoubleTap: $e")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Log.e(TAG, "onDoubleTapEvent: $e")
        return true
    }*/

}