package com.yannis.mayalisten.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yannis.baselib.base.BaseActivity
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

    @RequiresApi(Build.VERSION_CODES.M)
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
            /*BaiduMapMultiTaskActivity.start(
                this,
                ThirdMapConstants.TASK_TYPE_CHOOSE_POINT,
                null,
                false
            )*/
            // 让自己的应用程序独立控制使用浅色主题还是深色主题
            // 注意1：手动动态开启/关闭暗黑模式，此时需要搭配 onConfigurationChanged（）
            // 注意2：手动动态开启/关闭暗黑模式，此时需要 移除 android:configChanges="uiMode" 设置
            /*if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                // 关闭暗黑模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                // 开启暗黑模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // 动画
                //getWindow().setWindowAnimations(R.style.OutInAnimation);
                // 只作用于当前组件
                //delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }*/

            // 进入测试用例界面
            TestCaseActivity.start(this)
        }
    }

    override fun permissionOk() {
        TODO("Not yet implemented")
    }
}