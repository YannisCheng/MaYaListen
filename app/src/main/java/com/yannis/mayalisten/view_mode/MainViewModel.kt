package com.yannis.mayalisten.view_mode

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.RunOn
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainViewModel(clusterType: Int? = 0) : ViewModel() {


    class ViewModeFactory(var clusterType: Int?) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(clusterType) as T
        }

    }

    var beanList = MutableLiveData<ArrayList<AggregateRankListTabsBean>>();

    init {
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
    }
}