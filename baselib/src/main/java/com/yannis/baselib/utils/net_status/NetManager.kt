package com.yannis.baselib.utils.net_status

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build

class NetManager private constructor(private val application: Application) {

    private val netStatusCallBack: NetStatusCallBack = NetStatusCallBack(application)

    companion object {

        @JvmStatic
        fun getInstance(application: Application): NetManager {
            val instance: NetManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                NetManager(application)
            }
            return instance
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        val connyManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connyManager.registerDefaultNetworkCallback(netStatusCallBack)
        } else {
            connyManager.registerNetworkCallback(request, netStatusCallBack)
        }
    }

    fun register(obj: Any) {
        netStatusCallBack.register(obj)
    }

    fun unRegister(obj: Any) {
        netStatusCallBack.unRegister(obj)
    }

    fun unRegisterAll() {
        netStatusCallBack.unRegisterAll()
        val manager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.unregisterNetworkCallback(netStatusCallBack)
    }

    fun getNetType(): @NetType String {
        return netStatusCallBack.getStatus()
    }
}