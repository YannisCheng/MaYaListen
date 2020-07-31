package com.yannis.mayalisten.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import com.yannis.baselib.base.BaseAdapter
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.databinding.ItemPlayListLayoutBinding

/**
 * PlayListAdapter 播放页list item适配器
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class PlayListAdapter(context: Context, data: ArrayList<AlbumItemBean>?) :
    BaseAdapter<AlbumItemBean, ItemPlayListLayoutBinding>(context, data) {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bindItemDataToView(
        binding: ItemPlayListLayoutBinding,
        itemData: AlbumItemBean,
        position: Int
    ) {
        binding.apply {
            val mIvFrame: AnimationDrawable = ivFangStatus.background as AnimationDrawable

            tvTitle.text = itemData.title
            if (itemData.isChecked) {
                mIvFrame.start()
                ivFangStatus.visibility = VISIBLE
                tvTitle.setTextColor(mContext.getColor(R.color.maya_color_theme))
            } else {
                ivFangStatus.visibility = GONE
                tvTitle.setTextColor(mContext.getColor(R.color.maya_play_list_item_text))
                if (mIvFrame.isRunning) {
                    mIvFrame.stop()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_play_list_layout
    }
}