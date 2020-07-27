package com.yannis.mayalisten.view_mode

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.net.RetrofitManager2
import com.yannis.baselib.net.RunOn
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.net.MaYaApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * AggregateRankFirstPageVM 顶部Tab VM
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class AggregateRankFirstPageVM : ViewModel() {

    var listBean: MutableLiveData<MutableList<AggregateRankFirstPageBean>> = MutableLiveData()

    fun getLoadData() {
        RetrofitManager2.getInstance().getApi(MaYaApi::class.java).getAggregateRankFirstPage()
            .compose(RunOn<BaseResultBean<ArrayList<AggregateRankFirstPageBean>>>())
            .subscribe(object : Observer<BaseResultBean<ArrayList<AggregateRankFirstPageBean>>> {
                override fun onComplete() {
                    Log.e(TAG, "onComplete: ")
                }

                override fun onSubscribe(d: Disposable) {
                    //TODO("Not yet implemented")
                }

                override fun onNext(t: BaseResultBean<ArrayList<AggregateRankFirstPageBean>>) {
                    listBean.value = t.data
                }

                override fun onError(e: Throwable) {
                    //TODO("Not yet implemented")
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "onCleared: ")

    }


}