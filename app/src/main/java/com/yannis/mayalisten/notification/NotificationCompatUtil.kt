package com.yannis.mayalisten.notification

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


/**
 * NotificationCompat 通知栏实现
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/10/14
 */
class NotificationCompatUtil(val context: Context, val notificationManager: NotificationManager) {

    fun createNotification() {
        // 判断系统
        if (Build.VERSION.SDK_INT >= O) {
            var channelId = "chat"
            var channelName = "聊天消息"
            // 等级
            var importanceHigh = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importanceHigh)

            channelId = "subscribe"
            channelName = "订阅消息"
            // 等级
            importanceHigh = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importanceHigh)
        }
    }

    /**
     * 创建通知
     */
    @RequiresApi(O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        importanceHigh: Int
    ) {
        val notificationChannel = NotificationChannel(channelId, channelName, importanceHigh)
        notificationManager.createNotificationChannel(notificationChannel)
        /*notificationManager.createNotificationChannels(
            listOf(
                NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT)
            )
        )*/
    }

    /**
     * 显示通知
     */
    open fun sendChatMsg() {
        val notification = NotificationCompat.Builder(context, "chat")
            .setContentTitle("收到一条聊天消息")
            .setContentText("今天中午吃什么？")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.btn_default)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.dialog_holo_dark_frame
                )
            )
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }

    fun sendSubscribeMsg() {
        val notification: Notification = NotificationCompat.Builder(context, "subscribe")
            .setContentTitle("收到一条订阅消息")
            .setContentText("地铁沿线30万商铺抢购中！")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.btn_star)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.alert_dark_frame
                )
            )
            .setAutoCancel(true)
            .build()
        notificationManager.notify(2, notification)
    }


}