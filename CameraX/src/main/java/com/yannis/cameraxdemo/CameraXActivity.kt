package com.yannis.cameraxdemo

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.yannis.cameraxdemo.utils.FLAGS_FULLSCREEN
import kotlinx.android.synthetic.main.activity_main_camerax.*

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

/**
 * CameraXActivity.kt  CameraX的实现依赖于 Fragment
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/10/21 - 14:01
 */
class CameraXActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camerax)

        // 在设置全屏标志之前，我们必须稍等片刻以让UI稳定下来；
        // 否则，我们可能会尝试将应用设置为沉浸式模式，然后再准备就绪且标志不会黏住
        fragment_container.postDelayed(
            { fragment_container.systemUiVisibility = FLAGS_FULLSCREEN },
            IMMERSIVE_FLAG_TIMEOUT
        )
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CameraXFragment.newInstance())
            .commit()
    }

    /**
     * 触发按下事件时，可通过本地广播进行中继，以便片段可以处理它
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }
}