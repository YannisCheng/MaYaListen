package com.yannis.ijkplayerlib.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yannis.ijkplayerlib.R
import com.yannis.ijkplayerlib.widget.media.IjkVideoView

class IjkPlayerActivity : AppCompatActivity() {


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, IjkPlayerActivity::class.java)
            context.startActivity(starter)
        }
    }

    lateinit var videoView: IjkVideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijk_player)
        // https://vd3.bdstatic.com/mda-kjcmvbfncruwmern/sc/cae_h264_clips/mda-kjcmvbfncruwmern.mp4
        // http://aod.cos.tx.xmcdn.com/storages/85c4-audiofreehighqps/B2/DB/CMCoOSADYKhdAAVhBgBeCnSc.mp3
        //val uri:String = "android.resource://"+packageName+"/"+R.raw.img;
        videoView = findViewById(R.id.ijk_player)
        videoView.setVideoURI(Uri.parse("https://vd3.bdstatic.com/mda-kjcmvbfncruwmern/sc/cae_h264_clips/mda-kjcmvbfncruwmern.mp4"))
        videoView.start();
    }
}