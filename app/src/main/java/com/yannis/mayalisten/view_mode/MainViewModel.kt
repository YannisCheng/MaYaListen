package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.net.RetrofitManager2
import com.yannis.baselib.net.RunOn
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.net.MaYaApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * MainViewModel 单个Tab下的排名 VM
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 13:51
 */
class MainViewModel : ViewModel() {

    var beanList: MutableLiveData<ArrayList<AggregateRankListTabsBean>> = MutableLiveData()

    fun getLoadData(clusterType: Int?) {
        if (clusterType != null) {
            RetrofitManager2.getInstance().getApi(MaYaApi::class.java)
                .getAggregateRankListTabs(clusterType)
                .compose(RunOn<BaseResultBean<ArrayList<AggregateRankListTabsBean>>>())
                .subscribe(
                    object : Observer<BaseResultBean<ArrayList<AggregateRankListTabsBean>>> {
                        override fun onComplete() {
                            Log.e("TAG", "AggregateRankListTabs onComplete")
                        }

                        override fun onSubscribe(d: Disposable) {
                            //addDispose(d)
                        }

                        override fun onNext(t: BaseResultBean<ArrayList<AggregateRankListTabsBean>>) {
                            println(" index 0 item value is : ${t.data.size}")
                            beanList.value = t.data
                        }

                        override fun onError(e: Throwable) {
                            Log.e("TAG", e.toString())
                        }
                    })
        }
    }
}