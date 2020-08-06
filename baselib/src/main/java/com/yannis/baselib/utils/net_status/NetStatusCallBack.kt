package com.yannis.baselib.utils.net_status

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import java.lang.reflect.Method

/**
 * NetStatusCallBack 网络回调
 *
 * 参考：https://blog.csdn.net/sinat_38184748/article/details/102684881?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/8/5
 */
class NetStatusCallBack(application: Application) : ConnectivityManager.NetworkCallback() {
    // 观察者，key=类、value=方法
    private var registerMap = HashMap<Any, Method>()

    // 网络状态记录
    @Volatile
    private var netStatus: @NetStatus String = NetStatus.NET_UNKNOWN

    // 网络状态广播监听
    private val receiver = NetStatusReceiver()

    init {
        //val filter = IntentFilter()
        //filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        //application.registerReceiver(receiver, filter)
        netStatus = NetStatusUtils.getNetStatus(application)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        postStatus(NetStatus.NONE)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        postStatus(NetStatus.OK)
    }

    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        // 表明此网络连接成功验证
        val type = NetStatusUtils.getNetType(networkCapabilities)
        if (type == netStatus) return
        postStatus(type)
    }

    /**
     * 向观察者的订阅方法 发送状态更新
     */
    private fun postStatus(status: @NetStatus String) {
        netStatus = status
        val set: Set<Any> = registerMap.keys
        if (set.isNotEmpty()) {
            for (obj in set) {
                val method: Method = registerMap[obj] ?: continue
                invokePost(obj, method, status)
            }
        }
    }

    /**
     * 通过反射执行观察者对应的方法
     */
    private fun invokePost(obj: Any, method: Method, status: @NetStatus String) {
        method.invoke(obj, status)
    }

    /**
     * 注册
     */
    fun register(obj: Any) {
        val clazz = obj.javaClass
        if (!registerMap.containsKey(clazz)) {
            val method: Method = findAnnotationMethod(clazz) ?: return
            registerMap[obj] = method
        }
    }

    private fun findAnnotationMethod(clz: Class<*>): Method? {
        val method = clz.methods
        for (m in method) {
            // 看是否有注解
            m.getAnnotation(NetStatusChange::class.java) ?: continue
            // 判断返回类型
            val genericReturnType = m.genericReturnType.toString()
            if ("void" != genericReturnType) {
                // 方法的返回类型必须为void
                throw RuntimeException("The return type of the method【${m.name}】 must be void!")
            }
            // 检查参数，必须有一个String型的参数
            val parameterTypes = m.genericParameterTypes
            if (parameterTypes.size != 1 || parameterTypes[0].toString() != "class ${String::class.java.name}") {
                throw RuntimeException("The parameter types size of the method【${m.name}】must be one (type name must be java.lang.String)!")
            }
            return m
        }
        return null
    }

    /**
     * 取消单个注册
     */
    fun unRegister(obj: Any) {
        registerMap.remove(obj)
    }

    /**
     * 取消所有注册
     */
    fun unRegisterAll() {
        registerMap.clear()
    }

    fun getStatus(): @NetStatus String {
        return netStatus
    }

    inner class NetStatusReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            intent ?: return
            val type = NetStatusUtils.getNetStatus(context)
            if (type == netStatus) return
            postStatus(type)
        }
    }
}