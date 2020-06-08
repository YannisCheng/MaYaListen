package com.yannis.mayalisten.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yannis.mayalisten.base.BaseFragment
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.databinding.MainFragmentBinding

class MainFragment : BaseFragment() {

    private var itemBean: AggregateRankFirstPageBean? = null
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance(mItemBean: AggregateRankFirstPageBean): MainFragment {
            val args = Bundle()
            args.putSerializable("bean", mItemBean)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemBean = it.getSerializable("bean") as AggregateRankFirstPageBean?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        setData2View()
        return binding.root
    }

    private fun setData2View() {

        binding.showContent.text = itemBean.toString()
    }


}