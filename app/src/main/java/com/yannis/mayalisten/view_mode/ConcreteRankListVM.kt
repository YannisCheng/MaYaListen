package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.base.BaseViewMode
import com.yannis.mayalisten.bean.ConcreteRankListBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * ConcreteRankListVM 单个Tab下的某一个排名类别中的具体数据 VM
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class ConcreteRankListVM : BaseViewMode<DepositoryConcreteRankList>() {

    var liveData: MutableLiveData<ConcreteRankListBean> = MutableLiveData()

    fun getRequestData(
        categoryId: Int,
        clusterType: Int,
        pageId: Int,
        rankingListId: Int
    ){
        depository = DepositoryConcreteRankList()
        depository.requestConcreteRankList(categoryId,clusterType,pageId,rankingListId, object : Observer<BaseResultBean<ConcreteRankListBean>> {
            override fun onComplete() {
                //Log.e("TAG", " requestConcreteRankList onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                addDispose(d)
            }

            override fun onNext(t: BaseResultBean<ConcreteRankListBean>) {
                println(" index 0 item value is : ${t.toString()}")
                //requestSingleAlbumContent(t)
                liveData.value = t.data
            }

            override fun onError(e: Throwable) {
                Log.e("TAG", e.toString())

            }
        }   )
    }
}