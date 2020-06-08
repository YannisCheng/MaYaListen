package com.yannis.mayalisten.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yannis.mayalisten.base.BaseActivity
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.databinding.ActivityMainBinding
import com.yannis.mayalisten.fragment.MainFragment
import com.yannis.mayalisten.view_mode.AggregateRankFirstPageVM

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    var mdiator: TabLayoutMediator? = null
    val tabsTitle = ArrayList<AggregateRankFirstPageBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewModelProvider(this).get(AggregateRankFirstPageVM::class.java)
            .listBean.observe(this, Observer {
                tabsTitle.addAll(it);

                it?.forEach {
                    binding.tabLayout.addTab(
                        binding.tabLayout.newTab().setText(it.aggregateListConfig.aggregateName)
                    )
                }

                binding.viewPager.adapter = object : FragmentStateAdapter(this) {
                    override fun getItemCount(): Int {
                        return it.size
                    }

                    override fun createFragment(position: Int): Fragment {
                        return MainFragment.newInstance(it[position])
                    }
                }

                mdiator = TabLayoutMediator(
                    binding.tabLayout,
                    binding.viewPager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        tab.text = tabsTitle[position].aggregateListConfig.aggregateName

                    })

                mdiator?.attach()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mdiator?.detach()
    }

    /*private fun doRequest(resultBean: BaseResultBean<ArrayList<AggregateRankFirstPageBean>>) {
        RetrofitManager.getInstance().getApi()
            .getAggregateRankListTabs(resultBean.data.get(0).aggregateListConfig.clusterType.toString())
            .compose(RunOn<BaseResultBean<ArrayList<AggregateRankListTabsBean>>>())
            .subscribe(
                object : Observer<BaseResultBean<ArrayList<AggregateRankListTabsBean>>> {
                    override fun onComplete() {
                        Log.e("TAG", "AggregateRankListTabs onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        addDispose(d)
                    }

                    override fun onNext(t: BaseResultBean<ArrayList<AggregateRankListTabsBean>>) {
                        println(" index 0 item value is : ${t.data.get(0).toString()}")
                        requestConcreteRankList(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG", e.toString())
                    }
                })

    }

    private fun requestConcreteRankList(t: BaseResultBean<ArrayList<AggregateRankListTabsBean>>) {
        RetrofitManager.getInstance().getApi().getConcreteRankList(
            t.data.get(0).categoryId.toString(),
            t.data.get(0).rankClusterId.toString(),
            "1",
            "20",
            t.data.get(0).rankingListId.toString()
        )
            .compose(RunOn<BaseResultBean<ConcreteRankListBean>>())
            .subscribe(object : Observer<BaseResultBean<ConcreteRankListBean>> {
                override fun onComplete() {
                    Log.e("TAG", " requestConcreteRankList onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    addDispose(d)
                }

                override fun onNext(t: BaseResultBean<ConcreteRankListBean>) {
                    println(" index 0 item value is : ${t.data.list.get(0).toString()}")
                    requestSingleAlbumContent(t)
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", e.toString())
                }

            })
    }

    private fun requestSingleAlbumContent(t: BaseResultBean<ConcreteRankListBean>) {
        RetrofitManager.getInstance().getApi().getSingleAlbumContent(
            t.data.list[0].albumId.toString(),
            "true",
            t.data.list[0].trackId.toString()
        )
            .compose(RunOn<BaseResultBean<SingleAlbumContentBean>>())
            .subscribe(object : Observer<BaseResultBean<SingleAlbumContentBean>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    addDispose(d)
                }

                override fun onNext(t: BaseResultBean<SingleAlbumContentBean>) {
                    Log.e(
                        "TAG",
                        "SingleAlbumContentBean onNext: " + t.data.tracks.list[0].toString()
                    )
                }

                override fun onError(e: Throwable) {
                    Log.e("TAG", "onError: " + e.toString())
                }

            })
    }*/


}