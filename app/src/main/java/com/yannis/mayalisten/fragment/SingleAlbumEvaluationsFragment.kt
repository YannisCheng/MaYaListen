package com.yannis.mayalisten.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.mayalisten.R
import com.yannis.mayalisten.adapter.SingleAlbumEvaluationAdapter
import com.yannis.mayalisten.base.BaseFragment
import com.yannis.mayalisten.databinding.FragmentSingleAlbumEvaluationsBinding
import com.yannis.mayalisten.view_mode.SingleAlbumEvaluationsMV


private const val ALBUM_ID = "albumId"

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

        param1?.let {
            viewModel.getSingleAlbumEvaluations(it).observe(viewLifecycleOwner, Observer {
                evaluationAdapter.setNewData(it.comments.list)
            })
        }
    }

    override fun setBindViewModel(): Class<SingleAlbumEvaluationsMV> {
        return SingleAlbumEvaluationsMV::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_single_album_evaluations
    }
}