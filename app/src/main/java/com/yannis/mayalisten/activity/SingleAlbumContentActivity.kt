package com.yannis.mayalisten.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yannis.mayalisten.R
import com.yannis.mayalisten.base.BaseActivity
import com.yannis.mayalisten.bean.ItemBean
import com.yannis.mayalisten.databinding.ActivitySingleAlbumContentBinding
import com.yannis.mayalisten.fragment.SingleAlbumContentFragment
import com.yannis.mayalisten.fragment.SingleAlbumEvaluationsFragment

/**
 * 专辑->内容 界面
 */
class SingleAlbumContentActivity : BaseActivity<ViewModel, ActivitySingleAlbumContentBinding>() {

    private lateinit var itemBean: ItemBean
    private lateinit var mdiator: TabLayoutMediator

    companion object {
        @JvmStatic
        fun start(context: Context, itemBean: ItemBean) {
            val intent = Intent(context, SingleAlbumContentActivity::class.java)
            intent.putExtra("item_bean", itemBean)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleAlbumContentBinding.inflate(layoutInflater)
        intent?.let {
            itemBean = it.getSerializableExtra("item_bean") as ItemBean
        }
        setContentView(binding.root)

        val arrayList: ArrayList<Fragment> = ArrayList()
        arrayList.add(
            SingleAlbumContentFragment.newInstance(
                itemBean.albumId,
                true,
                itemBean.trackId
            )
        )
        arrayList.add(SingleAlbumEvaluationsFragment.newInstance(itemBean.albumId))
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText("节目"))
            tabLayout.addTab(tabLayout.newTab().setText("评价"))
            viewPager.adapter = object : FragmentStateAdapter(this@SingleAlbumContentActivity) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return arrayList[position]
                }
            }
        }
        mdiator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "节目"
                    1 -> tab.text = "评论"
                }
            })
        mdiator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        mdiator.detach()
    }

    override fun setBindViewModel(): Class<ViewModel> {
        return ViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_single_album_content
    }
}