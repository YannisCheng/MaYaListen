package com.yannis.mayalisten.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AlbumItemBean
import java.text.SimpleDateFormat

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/9
 */
class SingleAlbumItemAdapter(data: MutableList<AlbumItemBean>?) :
    BaseQuickAdapter<AlbumItemBean, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_album_content_lsit_layout
    }


    @SuppressLint("SimpleDateFormat")
    override fun convert(helper: BaseViewHolder?, item: AlbumItemBean?) {
        item?.let { itemBean ->
            helper?.let {
                val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM")
                val create: Long = itemBean.createdAt
                val format = simpleDateFormat.format(create / 1000)
                Log.e(TAG, "convert: ${format}")
                it.getView<TextView>(R.id.tv_order).text = (it.adapterPosition + 1).toString()
                it.getView<TextView>(R.id.tv_download_left)
                it.getView<TextView>(R.id.tv_time).text = format
                it.getView<TextView>(R.id.tv_title).text = itemBean.title
                it.getView<TextView>(R.id.tv_play_count).text = itemBean.playtimes.toString()
                it.getView<TextView>(R.id.tv_play_duration).text = itemBean.duration.toString()
                it.getView<TextView>(R.id.tv_play_msg).text = itemBean.comments.toString()
            }
        }

    }
}