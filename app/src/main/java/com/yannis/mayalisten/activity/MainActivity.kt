package com.yannis.mayalisten.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yannis.mayalisten.R
import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.net.RetrofitManager
import com.yannis.mayalisten.net.runOn
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compositeDisposable = CompositeDisposable()

        addDispose(
            RetrofitManager.getInstance().getApi().getAggregateRankFirstPage()
                .compose(runOn<BaseResultBean<ArrayList<AggregateRankFirstPageBean>>>())
                .subscribe({ resultBean ->
                    doRequest(resultBean)
                }, { error ->
                    println("请求异常：${error.toString()}")
                }, { println("结束！") })
        )

        /*RetrofitManager.getInstance().getApi().getAggregateRankFirstPage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()*/


    }

    private fun doRequest(resultBean: BaseResultBean<ArrayList<AggregateRankFirstPageBean>>) {
        RetrofitManager.getInstance().getApi()
            .getAggregateRankListTabs(resultBean.data.get(0).aggregateListConfig.clusterType.toString())
            .compose(runOn<BaseResultBean<ArrayList<AggregateRankListTabsBean>>>())
            .subscribe(
                object : Observer<BaseResultBean<ArrayList<AggregateRankListTabsBean>>> {
                    override fun onComplete() {
                        Log.e("TAG", "onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }

                    override fun onNext(t: BaseResultBean<ArrayList<AggregateRankListTabsBean>>) {
                        println(" index 0 item value is : ${t.data.get(0).toString()}")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG", "${e.toString()}")
                    }
                })

    }

    fun addDispose(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (compositeDisposable.size() != 0) {
            compositeDisposable.clear()
        }
    }

}