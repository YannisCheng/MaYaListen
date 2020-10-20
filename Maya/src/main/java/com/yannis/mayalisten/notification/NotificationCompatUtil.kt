package com.yannis.mayalisten.notification

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.yannis.mayalisten.activity.SplashActivity


/**
 * NotificationCompat 通知栏实现
 * 通知样式设置参考：https://blog.csdn.net/u011418943/article/details/105133041?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.add_param_isCf&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.add_param_isCf
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
     * 创建通知渠道
     */
    @RequiresApi(O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        importanceHigh: Int
    ) {
        val notificationChannel = NotificationChannel(channelId, channelName, importanceHigh)
        // 在App图标右上角允许这个渠道下的通知显示角标
        notificationChannel.setShowBadge(true)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)

        notificationChannel.lightColor = Color.RED
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationChannel.setShowBadge(true)
        notificationChannel.setBypassDnd(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400)
        notificationChannel.description = channelId
        //notificationChannel.group = "groupId"
        notificationManager.createNotificationChannel(notificationChannel)
        /*notificationManager.createNotificationChannels(
            listOf(
                NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel("", "", NotificationManager.IMPORTANCE_DEFAULT)
            )
        )*/
    }

    fun openChannelNotification(channelId: String) {
        // 如果用户已经关闭指定渠道的通知，可以要求用户手动开启
        if (Build.VERSION.SDK_INT >= O) {
            // 删除渠道
            //notificationManager.deleteNotificationChannel(channelId)
            val channel = notificationManager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                context.startActivity(intent)
                Toast.makeText(context, "请手动将通知打开", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 显示通知
     */
    fun sendChatMsg(msg: String) {
        if (Build.VERSION.SDK_INT >= O) {
            var channelId = "chat"
            var channelName = "聊天消息"
            // 等级
            var importanceHigh = NotificationManager.IMPORTANCE_HIGH
            createNotificationChannel(channelId, channelName, importanceHigh)

            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle("收到一条聊天消息")
                .setContentText("今天中午吃什么？${msg}")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.btn_default)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.dialog_holo_dark_frame
                    )
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent())
                .setVisibility(VISIBILITY_PUBLIC)
                .build()
            notificationManager.notify(1, notification)
        }
    }

    fun sendSubscribeMsg() {
        if (Build.VERSION.SDK_INT >= O) {
            val channelId = "subscribe"
            val channelName = "订阅消息"
            // 等级
            val importanceHigh = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importanceHigh)

            val notification: Notification = NotificationCompat.Builder(context, "subscribe")
                // 标题
                .setContentTitle("收到一条订阅消息")
                // 通知内容
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
                // 显示未读角标数量
                // 未读数量怎么没有显示出来呢？这个功能还需要我们对着图标进行长按才行
                .setNumber(4)
                // 自定义通知音效
                //.setSound()
                // 使用默认音效
                //.setDefaults(NotificationCompat.DEFAULT_SOUND)
                // 自定义振动效果
                //.setVibrate(longArrayOf(0,1000,1000))
                // 默认振动效果
                //.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                // 设置默认全部通知效果
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                //.setColor(R.color.holo_orange_dark)
                .build().apply {
                    // 是否锁屏显示
                    visibility = Notification.VISIBILITY_PUBLIC
                }
            notificationManager.notify(2, notification)
        }
    }

    /**
     * 跳转
     */
    private fun pendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            context,
            0,
            Intent(context, SplashActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

}