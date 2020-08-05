/*
package com.yannis.baselib.utils.net_status

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.blankj.utilcode.util.ToastUtils


*/
/**
 * NetBroadCastReceiver网络状态广播监听器
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/5
 *//*

const val TAG = "NetBroadCastReceiver"

class NetBroadCastReceiver(context: Context) {
    val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(NetworkCallbackImpl)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.requestNetwork(
                NetworkRequest.Builder().build(),
                NetworkCallbackImpl()
            )
        } else {
            isNetworkAvailable(context)

            isWifiConnected(context)
        }
    }

    */
/**
 * 网络是否连接
 *//*

    fun isNetworkAvailable(context: Context): Boolean {
        val infos = connectivityManager.allNetworkInfo
        if (infos != null) {
            for (i in infos.indices) {
                if (infos[i].isConnected == true) {

                    return true
                }
            }
        }
        return false
    }

    */
/**
 * WiFi是否连接
 *//*

    fun isWifiConnected(context: Context): Boolean {
        val mWiFiNetworkInfo =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWiFiNetworkInfo?.isConnected ?: false
    }

    */
/**
 * 移动网络是否连接
 *//*

    fun isMobleConnected(context: Context): Boolean {
        val mMobleNetworkInfo =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return mMobleNetworkInfo?.isConnected ?: false
    }

    */
/**
 * 获取当前连接网络类型
 *//*

    fun getType(context: Context) {
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null) {
            Log.i(
                TAG,
                "yyy-type: " + networkInfo.isAvailable + " " + networkInfo.isConnected + " " + networkInfo.toString()
            )
            Log.i(
                TAG,
                "yyy-type: " + networkInfo.subtypeName + " " + networkInfo.typeName + " " + networkInfo.extraInfo
            )
        }
    }

    class NetworkCallbackImpl : NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            ToastUtils.showShort("网络不可用")
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            ToastUtils.showShort("网络可用")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    ToastUtils.showShort("wifi")
                    Log.e("TAG", "onCapabilitiesChanged: wifi")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    ToastUtils.showShort("蜂窝")
                    Log.e("TAG", "onCapabilitiesChanged: 蜂窝")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    ToastUtils.showShort("VPN")
                    Log.e("TAG", "onCapabilitiesChanged: VPN")
                }
            }
        }
    }
}*/
