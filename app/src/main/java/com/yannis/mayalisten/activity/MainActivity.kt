package com.yannis.mayalisten.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yannis.mayalisten.R
import com.yannis.mayalisten.base.BaseActivity
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.databinding.ActivityMainBinding
import com.yannis.mayalisten.fragment.MainFragment
import com.yannis.mayalisten.view_mode.AggregateRankFirstPageVM
import com.yannis.mayalisten.widget.WebViewActivity

/**
 * 首页
 */
class MainActivity : BaseActivity<AggregateRankFirstPageVM, ActivityMainBinding>() {

    var mdiator: TabLayoutMediator? = null
    val tabsTitle = ArrayList<AggregateRankFirstPageBean>()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mIvFrame: AnimationDrawable = binding.ivRotate.background as AnimationDrawable
        mIvFrame.start()

        /*val loadAnimation = AnimationUtils.loadAnimation(this, R.drawable.maya_small_loading)
        binding.ivRotate.animation = loadAnimation
        binding.ivRotate.startAnimation(loadAnimation)*/

        //showLoading("")
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

        binding.ivShare.setOnClickListener {
            val intent: Intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }

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
}