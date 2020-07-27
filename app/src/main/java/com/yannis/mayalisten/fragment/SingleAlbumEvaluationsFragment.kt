package com.yannis.mayalisten.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.baselib.base.BaseFragment
import com.yannis.mayalisten.R
import com.yannis.mayalisten.adapter.SingleAlbumEvaluationAdapter
import com.yannis.mayalisten.databinding.FragmentSingleAlbumEvaluationsBinding
import com.yannis.mayalisten.view_mode.SingleAlbumEvaluationsMV


private const val ALBUM_ID = "albumId"

/**
 * SingleAlbumEvaluationsFragment 单张专辑评价
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 14:18
 */
class SingleAlbumEvaluationsFragment :
    BaseFragment<SingleAlbumEvaluationsMV, FragmentSingleAlbumEvaluationsBinding>() {

    private var param1: Int? = 0
    private lateinit var evaluationAdapter: SingleAlbumEvaluationAdapter

    companion object {
        @JvmStatic
        fun newInstance(albumId: Int) =
            SingleAlbumEvaluationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALBUM_ID, albumId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ALBUM_ID, 0)
        }
    }

    override fun initView() {
        evaluationAdapter = SingleAlbumEvaluationAdapter(null)
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(this@SingleAlbumEvaluationsFragment.context)
            recyclerView.adapter = evaluationAdapter
        }

        viewModel.bean.observe(viewLifecycleOwner, Observer {
            evaluationAdapter.setNewData(it.comments.list)
        })
    }

    override fun setBindViewModel(): Class<SingleAlbumEvaluationsMV> {
        return SingleAlbumEvaluationsMV::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_single_album_evaluations
    }

    override fun loadData() {

    }

    override fun refreshData() {
        param1?.let {
            viewModel.getSingleAlbumEvaluations(it)
        }
    }
}