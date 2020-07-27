package com.yannis.mayalisten.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.baselib.base.BaseFragment
import com.yannis.mayalisten.R
import com.yannis.mayalisten.activity.AlbumContentPlayActivity
import com.yannis.mayalisten.adapter.SingleAlbumItemAdapter
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.SingleAlbumContentFragmentBinding
import com.yannis.mayalisten.view_mode.SingleAlbumContentViewModel
import com.yannis.mayalisten.widget.IndexChoosePopupWindow


private const val ALBUM_ID = "albumId"
private const val ASC = "asc"
private const val TRACK_ID = "trackId"

/**
 * SingleAlbumContentFragment 单张专辑内容
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 14:18
 */
class SingleAlbumContentFragment :
    BaseFragment<SingleAlbumContentViewModel, SingleAlbumContentFragmentBinding>() {

    private var albumId: Int = 0
    private var trackId: Int = 0
    private var asc: Boolean = false
    private lateinit var singleAlbumItemAdapter: SingleAlbumItemAdapter

    companion object {

        @JvmStatic
        fun newInstance(albumId: Int, asc: Boolean, trackId: Int) =
            SingleAlbumContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALBUM_ID, albumId)
                    putBoolean(ASC, asc)
                    putInt(TRACK_ID, trackId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            albumId = it.getInt(ALBUM_ID)
            trackId = it.getInt(TRACK_ID)
            asc = it.getBoolean(ASC)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var itemBeans: ArrayList<AlbumItemBean> = ArrayList()

        viewModel.mutableLiveData
            .observe(viewLifecycleOwner, Observer {
                binding.tvCountTotal.text = "共${it.album.tracks.toString()}集"
                singleAlbumItemAdapter.setNewData(it.tracks.list)
                itemBeans = it.tracks.list as ArrayList<AlbumItemBean>

                binding.tvCountTotal.setOnClickListener {
                    this@SingleAlbumContentFragment.context?.let { context ->
                        run {
                            val indexChoosePopupWindow = IndexChoosePopupWindow(context, itemBeans)
                            indexChoosePopupWindow.showAsDropDown(
                                binding.rlTop,
                                0,
                                0,
                                Gravity.BOTTOM
                            )
                        }
                    }
                }
            })

        singleAlbumItemAdapter.setOnItemClickListener { adapter, view, position ->
            this@SingleAlbumContentFragment.context?.let {
                AlbumContentPlayActivity.starter(
                    it,
                    adapter.data[position] as AlbumItemBean,
                    itemBeans
                )
            }
        }
    }

    override fun initView() {
        binding.apply {
            singleAlbumItemAdapter = SingleAlbumItemAdapter(null)
            recyclerView.adapter = singleAlbumItemAdapter
            recyclerView.layoutManager =
                LinearLayoutManager(this@SingleAlbumContentFragment.context)
        }
    }

    override fun setBindViewModel(): Class<SingleAlbumContentViewModel> {
        return SingleAlbumContentViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.single_album_content_fragment
    }

    override fun loadData() {

    }

    override fun refreshData() {
        viewModel.getSingleAlbumContent(albumId, true, trackId)
    }

}