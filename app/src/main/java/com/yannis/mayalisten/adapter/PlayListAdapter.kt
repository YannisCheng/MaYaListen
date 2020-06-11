package com.yannis.mayalisten.adapter

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AlbumItemBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class PlayListAdapter(data: List<AlbumItemBean>?) :
    BaseQuickAdapter<AlbumItemBean, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_play_list_layout
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun convert(helper: BaseViewHolder?, item: AlbumItemBean?) {
        helper?.let {
            it.addOnClickListener(R.id.iv_download_left)
            item?.let {
                val mIvFrame: AnimationDrawable =
                    helper.getView<ImageView>(R.id.iv_fang_status).background as AnimationDrawable

                helper.getView<TextView>(R.id.tv_title).text = item.title
                if (it.isChecked) {
                    mIvFrame.start()
                    helper.getView<ImageView>(R.id.iv_fang_status).visibility = VISIBLE
                    helper.getView<TextView>(R.id.tv_title)
                        .setTextColor(mContext.getColor(R.color.maya_color_theme))
                } else {
                    helper.getView<ImageView>(R.id.iv_fang_status).visibility = GONE
                    helper.getView<TextView>(R.id.tv_title)
                        .setTextColor(mContext.getColor(R.color.maya_play_list_item_text))
                    stopAnima(mIvFrame)
                }
            }
        }
    }

    private fun stopAnima(mIvFrame: AnimationDrawable) {
        if (mIvFrame.isRunning) {
            mIvFrame.stop()
        }
    }
}