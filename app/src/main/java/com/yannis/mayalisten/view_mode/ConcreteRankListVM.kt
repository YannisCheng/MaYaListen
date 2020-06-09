package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.ConcreteRankListBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class ConcreteRankListVM(

) : ViewModel() {

    val liveData = MutableLiveData<ConcreteRankListBean>()

    /*class ViewModeProvider(
        var categoryId: Int,
        var clusterType: Int,
        var pageId: Int? = 1,
        var pageSize: Int? = 20,
        var rankingListId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ConcreteRankListVM(categoryId, clusterType, 1, 20, rankingListId) as T
        }
    }*/


    fun getRequestData(
        categoryId: Int,
        clusterType: Int,
        pageId: Int? = 1,
        pageSize: Int? = 20,
        rankingListId: Int
    ) {
        RetrofitManager.getInstance().getApi().getConcreteRankList(
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