package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.net.RetrofitManager2
import com.yannis.baselib.net.RunOn
import com.yannis.mayalisten.bean.ConcreteRankListBean
import com.yannis.mayalisten.net.MaYaApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

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
        pageId: Int? = 1,
        pageSize: Int? = 20,
        rankingListId: Int
    ) {
        RetrofitManager2.getInstance().getApi(MaYaApi::class.java).getConcreteRankList(
            categoryId,
            clusterType,
            1,
            20,
            rankingListId
        )
            .compose(RunOn<BaseResultBean<ConcreteRankListBean>>())
            .subscribe(object : Observer<BaseResultBean<ConcreteRankListBean>> {
                override fun onComplete() {
                    Log.e("TAG", " requestConcreteRankList onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    //addDispose(d)
                }

                override fun onNext(t: BaseResultBean<ConcreteRankListBean>) {
                    println(" index 0 item value is : ${t.data.list.get(0).toString()}")
                    //requestSingleAlbumContent(t)
                    liveData.value = t.data
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", e.toString())
                }
            })
    }
}