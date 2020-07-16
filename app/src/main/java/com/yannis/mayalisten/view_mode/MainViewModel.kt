package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainViewModel : ViewModel() {

    var beanList = MutableLiveData<ArrayList<AggregateRankListTabsBean>>();
    fun getLoadData(clusterType: Int?): MutableLiveData<ArrayList<AggregateRankListTabsBean>> {
        if (clusterType != null) {
            RetrofitManager.getInstance().getApi()
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
        return beanList;
    }
}