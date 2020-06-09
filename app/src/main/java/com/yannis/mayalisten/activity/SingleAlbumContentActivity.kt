package com.yannis.mayalisten.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.yannis.mayalisten.R
import com.yannis.mayalisten.base.BaseActivity
import com.yannis.mayalisten.bean.ItemBean
import com.yannis.mayalisten.databinding.ActivitySingleAlbumContentBinding
import com.yannis.mayalisten.fragment.SingleAlbumContentFragment

class SingleAlbumContentActivity : BaseActivity() {


    private lateinit var binding: ActivitySingleAlbumContentBinding
    private lateinit var itemBean: ItemBean
    private lateinit var fragmentTransaction: FragmentTransaction

    companion object {
        fun start(context: Context, itemBean: ItemBean) {
            val intent: Intent = Intent(context, SingleAlbumContentActivity::class.java)
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
        binding.apply {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(
                R.id.fl,
                SingleAlbumContentFragment.newInstance(itemBean.albumId, true, itemBean.trackId)
            )
            fragmentTransaction.commit()
        }

    }
}