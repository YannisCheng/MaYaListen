package com.yannis.mayalisten.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.mayalisten.activity.SingleAlbumContentActivity
import com.yannis.mayalisten.adapter.ConcreteRankListAdapter
import com.yannis.mayalisten.adapter.RankOfItemTabAdapter
import com.yannis.mayalisten.base.BaseFragment
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.bean.ItemBean
import com.yannis.mayalisten.databinding.MainFragmentBinding
import com.yannis.mayalisten.view_mode.ConcreteRankListVM
import com.yannis.mayalisten.view_mode.MainViewModel

class MainFragment : BaseFragment() {

    private var itemBean: AggregateRankFirstPageBean? = null
    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var modeOfRankItem: ConcreteRankListVM
    private var rankOfItemTabAdapter: RankOfItemTabAdapter? = null
    private var concreteRankListAdapter: ConcreteRankListAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(mItemBean: AggregateRankFirstPageBean) = MainFragment().apply {
            Bundle().apply {
                putSerializable("bean", mItemBean)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemBean = it.getSerializable("bean") as AggregateRankFirstPageBean?
        }
        modeOfRankItem = ViewModelProvider(this)[ConcreteRankListVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        rankOfItemTabAdapter = RankOfItemTabAdapter(null)
        concreteRankListAdapter = ConcreteRankListAdapter(null)
        binding.apply {
            leftRecycler.layoutManager = LinearLayoutManager(this@MainFragment.activity)
            leftRecycler.adapter = rankOfItemTabAdapter
            contentRecycler.layoutManager = LinearLayoutManager(this@MainFragment.activity)
            contentRecycler.adapter = concreteRankListAdapter
        }
        setData2View()
        onClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClick() {
        rankOfItemTabAdapter?.setOnItemChildClickListener { adapter, _, position ->
            val itemBean = adapter.getItem(position) as AggregateRankListTabsBean
            if (!itemBean.isChecked) {
                itemBean.isChecked = true
                (adapter.data as List<AggregateRankListTabsBean>).forEach {
                    if (it.rankClusterId != itemBean.rankClusterId) it.isChecked = false
                }
            }
            rankOfItemTabAdapter?.notifyDataSetChanged()
            getRankListOfItemTab(itemBean)
        }

        concreteRankListAdapter?.setOnItemClickListener { adapter, _, position ->
            this@MainFragment.context?.let {
                SingleAlbumContentActivity.start(
                    it,
                    adapter.data[position] as ItemBean
                )
            }
        }
    }

    private fun setData2View() {

        viewModel = ViewModelProvider(
            this,
            MainViewModel.ViewModeFactory(itemBean?.aggregateListConfig?.clusterType)
        )[MainViewModel::class.java]
        viewModel.beanList.observe(viewLifecycleOwner, Observer {

            binding.apply {
                if (it.size == 1) leftRecycler.visibility = GONE else leftRecycler.visibility =
                    VISIBLE
            }

            it[0].isChecked = true
            rankOfItemTabAdapter?.setNewData(it)
            getRankListOfItemTab(it[0])
        })
    }

    private fun getRankListOfItemTab(it: AggregateRankListTabsBean) {
        modeOfRankItem.getRequestData(it.categoryId, it.rankClusterId, 1, 20, it.rankingListId)
        modeOfRankItem.liveData.observe(viewLifecycleOwner, Observer {
            concreteRankListAdapter?.setNewData(it.list)
        })
    }
}