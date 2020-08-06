package com.yannis.baselib.utils.net_status

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build

/**
 * NetStatusUtils 网络类型、具体连接信息
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/8/6 - 09:45
 */
object NetStatusUtils {

    /**
     * 获取网络类型名称
     */
    fun getNetType(ties: NetworkCapabilities): @NetStatus String {
        return if (ties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            ties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        ) {
            // 使用WI-FI
            NetStatus.WIFI
        } else if (ties.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            ties.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        ) {
            // 使用蜂窝网络
            NetStatus.CELLULAR
        } else {
            // 未知网络，包括蓝牙、VPN、LoWPAN
            NetStatus.NET_UNKNOWN
        }
    }

    /**
     * 获取连接中的网络
     */
    fun getActiveNetwork(context: Context): Network? {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return manager.activeNetwork
        }
        return null
    }

    /**
     * 获取网络状态
     */
    fun getNetStatus(context: Context): @NetStatus String {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val net = manager.activeNetwork
            val ties = manager.getNetworkCapabilities(net) ?: return NetStatus.NONE
            return getNetType(ties)
        } else {
            val netInfo = manager.activeNetworkInfo
            if (netInfo == null || !netInfo.isAvailable) {
                return NetStatus.NONE
            }
            return when (netInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> NetStatus.CELLULAR
                ConnectivityManager.TYPE_WIFI -> NetStatus.WIFI
                else -> NetStatus.NET_UNKNOWN
            }
        }
    }

    /**
     * 检查当前连接的网络是否为5G WI-FI
     */
    fun is5GWifiConnected(context: Context): Boolean {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo ?: return false
        // 频段
        var frequency = 0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            frequency = wifiInfo.frequency
        } else {
            val ssid = wifiInfo.ssid ?: return false
            if (ssid.length < 2) return false
            val sid = ssid.substring(1, ssid.length - 1)
            for (scan in wifiManager.scanResults) {
                if (scan.SSID == sid) {
                    frequency = scan.frequency
                    break
                }
            }
        }
        return frequency in 4900..5900
    }

    /**
     * 获取当前连接Wi-Fi名
     * 如果没有定位权限，获取到的名字将为  unknown ssid
     */
    fun getConnectedWifiSSID(context: Context): String {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo ?: return ""
        return wifiInfo.ssid.substring(1, wifiInfo.ssid.length - 1)
    }
}