package com.yannis.mayalisten.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.baselib.base.BaseAdapter
import com.yannis.baselib.widget.BasePopupWindow
import com.yannis.mayalisten.adapter.PlayListAdapter
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.PlayListLayoutBinding

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
/**
 * PlayListPopupWindow 播放列表弹窗
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/6/11 - 14:21
 */
class PlayListPopupWindow(mContext: Context, beans: ArrayList<AlbumItemBean>) :
    BasePopupWindow(mContext) {

    private var playListAdapter: PlayListAdapter
    private var binding: PlayListLayoutBinding

    init {
        binding = PlayListLayoutBinding.inflate(LayoutInflater.from(mContext), null, false)
        playListAdapter = PlayListAdapter(mContext, beans)
        binding.apply {
            root.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            tvClose.setOnClickListener {
                dismiss()
            }

            tvOrderPlay.setOnClickListener { Log.e("TAG", "顺序修改") }
            tvOrderOver.setOnClickListener { Log.e("TAG", "倒序") }

            recyclerView.layoutManager = LinearLayoutManager(mContext)
            recyclerView.adapter = playListAdapter
        }

        setBaseDialogSetting(binding)

        playListAdapter.setOnItemClickListener(object :
            BaseAdapter.OnItemClickCallBack<AlbumItemBean> {
            override fun onItemClickListener(
                itemData: AlbumItemBean,
                position: Int,
                data: ArrayList<AlbumItemBean>
            ) {
                val timeCloseBean = data[position] as AlbumItemBean
                timeCloseBean.isChecked = true
                (data as List<AlbumItemBean>).forEach {
                    if (timeCloseBean.trackId != it.trackId) {
                        it.isChecked = false
                    }
                }
                playListAdapter.notifyDataSetChanged()
            }
        })

        /*playListAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_download_left -> Log.e("TAG", adapter.data[position].toString())
            }
        }*/
    }

    private fun changeState(
        adapter: BaseQuickAdapter<Any, BaseViewHolder>,
        position: Int
    ) {

    }
}