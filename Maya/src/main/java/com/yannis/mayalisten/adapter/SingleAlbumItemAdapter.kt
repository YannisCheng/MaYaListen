package com.yannis.mayalisten.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
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
                when (it.adapterPosition) {
                    0 -> {
                        setTopView(it, R.drawable.live_fanlist_top1)
                    }
                    1 -> {
                        setTopView(it, R.drawable.live_fanlist_top2)
                    }
                    2 -> {
                        setTopView(it, R.drawable.live_fanlist_top3)
                    }
                    else -> {
                        it.getView<ImageView>(R.id.iv_fanlist).visibility = GONE
                        it.getView<TextView>(R.id.tv_order).visibility = VISIBLE
                        it.getView<TextView>(R.id.tv_order).text =
                            (it.adapterPosition + 1).toString()
                    }
                }
                it.getView<TextView>(R.id.tv_download_left)
                it.getView<TextView>(R.id.tv_time).text = format
                it.getView<TextView>(R.id.tv_title).text = itemBean.title
                it.getView<TextView>(R.id.tv_play_count).text = itemBean.playtimes.toString()
                it.getView<TextView>(R.id.tv_play_duration).text = itemBean.duration.toString()
                it.getView<TextView>(R.id.tv_play_msg).text = itemBean.comments.toString()
            }
        }
    }

    private fun setTopView(it: BaseViewHolder, imgId: Int) {
        it.getView<ImageView>(R.id.iv_fanlist).visibility = VISIBLE
        it.getView<TextView>(R.id.tv_order).visibility = GONE
        it.getView<ImageView>(R.id.iv_fanlist)
            .background = mContext.resources.getDrawable(imgId)
    }
}