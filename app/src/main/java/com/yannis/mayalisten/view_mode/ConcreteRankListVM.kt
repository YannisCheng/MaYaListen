package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonParseException
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.net.RetrofitManager2
import com.yannis.baselib.net.RunOn
import com.yannis.mayalisten.bean.ConcreteRankListBean
import com.yannis.mayalisten.net.MaYaApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okio.EOFException
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * ConcreteRankListVM 单个Tab下的某一个排名类别中的具体数据 VM
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class ConcreteRankListVM : ViewModel() {

    var liveData: MutableLiveData<ConcreteRankListBean> = MutableLiveData()

    fun getRequestData(
        categoryId: Int,
        clusterType: Int,
        pageId: Int = 1,
        rankingListId: Int
    ) {
        RetrofitManager2.getInstance().getApi(MaYaApi::class.java).getConcreteRankList(
            categoryId,
            clusterType,
            pageId,
            2,
            rankingListId
        )
            .compose(RunOn<BaseResultBean<ConcreteRankListBean>>())
            .subscribe(object : Observer<BaseResultBean<ConcreteRankListBean>> {
                override fun onComplete() {
                    //Log.e("TAG", " requestConcreteRankList onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    //addDispose(d)
                }

                override fun onNext(t: BaseResultBean<ConcreteRankListBean>) {
                    println(" index 0 item value is : ${t.toString()}")
                    //requestSingleAlbumContent(t)
                    liveData.value = t.data
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", e.toString())
                    if (e is EOFException) {
                        // 连接丢失
                    } else if (e is SocketTimeoutException) {
                        // 请求超时
                    } else if (e is SSLHandshakeException) {
                        // 安全证书异常
                    } else if (e is ConnectException) {
                        // 网络连接超时
                    } else if (e is UnknownHostException) {
                        // 域名解析失败
                    } else if (e is JsonParseException || e is JSONException/* || e is ParseException*/) {
                        // 数据解析异常
                    } else if (e is InterruptedIOException) {
                        //
                    } else if (e is HttpException) {
                        val code = (e as HttpException).code()
                        when (code) {
                            // 参考：https://blog.csdn.net/csdn1844295154/article/details/78980174?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.nonecase
                            500 -> "服务器内部错误"
                            501 -> "服务器不具备完成请求的功能"
                            502 -> "错误网关"
                            503 -> "服务不可用"
                            504 -> "网关超时"
                            //505 -> "HTTP版本不支持"
                            400 -> "错误请求"
                            401 -> "未经授权，请求需要身份验证"
                            403 -> "服务器拒绝请求"
                            404 -> "服务器找不到请求的网页"
                            //405 -> "方法不允许"
                            //406 -> "不可接受"
                            //407 -> "需要代理身份验证"
                            408 -> "请求超时"
                            //410 -> "文档永久地离开了指定的位置"
                            //411 -> "需要Content-Length头请求"
                            //413 -> "请求实体太大"
                            //414 -> "请求URI太长"
                            //415 -> "不支持的媒体类型"
                        }
                    } else {
                        // 未知异常
                    }
                }
            })
    }
}