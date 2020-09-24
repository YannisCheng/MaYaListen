package com.yannis.mayalisten.activity

import android.graphics.drawable.AnimationDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yannis.baselib.base.BaseActivity
import com.yannis.maplib.baidu.BaiduMapMultiTaskActivity
import com.yannis.maplib.utils.ThirdMapConstants
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.databinding.ActivityMainBinding
import com.yannis.mayalisten.fragment.MainFragment
import com.yannis.mayalisten.view_mode.AggregateRankFirstPageVM

/**
 * MainActivity 主页
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 14:16
 */
class MainActivity : BaseActivity<AggregateRankFirstPageVM, ActivityMainBinding>() {

    var mdiator: TabLayoutMediator? = null
    val tabsTitle = ArrayList<AggregateRankFirstPageBean>()

    override fun onDestroy() {
        super.onDestroy()
        mdiator?.detach()
    }

    override fun setBindViewModel(): Class<AggregateRankFirstPageVM> {
        return AggregateRankFirstPageVM::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun loadData() {
        viewModel.getLoadData()
    }

    override fun dataToView() {
        viewModel.listBean.observe(this, Observer { it ->
            tabsTitle.addAll(it)

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

    override fun initView() {
        val mIvFrame: AnimationDrawable = binding.ivRotate.background as AnimationDrawable
        mIvFrame.start()

        /*val loadAnimation = AnimationUtils.loadAnimation(this, R.drawable.maya_small_loading)
        binding.ivRotate.animation = loadAnimation
        binding.ivRotate.startAnimation(loadAnimation)*/

        //showLoading("")

        binding.ivShare.setOnClickListener {
            /*val intent: Intent = Intent(this, TestMainActivity::class.java)
            startActivity(intent)*/
            BaiduMapMultiTaskActivity.start(
                this,
                ThirdMapConstants.TASK_TYPE_CHOOSE_POINT,
                null,
                false
            )
        }
    }
}