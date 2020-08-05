package com.yannis.baselib.utils.net_status

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.blankj.utilcode.util.ToastUtils

/**
 * NetStatusCallBack 网络状态回调
 *
 * 参考：https://blog.csdn.net/sinat_38184748/article/details/102684881?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/5
 */
class NetStatusCallBack : ConnectivityManager.NetworkCallback() {

    private var requestMap = HashMap<Any, String>()
    private var netType: @NetType String = NetType.NET_UNKNOWN

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