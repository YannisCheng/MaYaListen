package com.yannis.mayalisten.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.yannis.baselib.base.BaseAdapter
import com.yannis.baselib.utils.glidex.GlideX
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.ItemBean
import com.yannis.mayalisten.databinding.ItemConcreteRankListLayoutBinding

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class ConcreteRankListAdapter(context: Context, data: ArrayList<ItemBean>?) :
    BaseAdapter<ItemBean, ItemConcreteRankListLayoutBinding>(context, data) {

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setRankIndicator(
        imageView: ImageView,
        item: ItemBean?
    ) {
        when (item?.positionChange) {
            0 -> imageView.setImageDrawable(mContext.getDrawable(R.drawable.draw_shape_line))
            1 -> {
                imageView.setImageResource(R.drawable.cartoon_ic_arrow_up)
                imageView.imageTintList =
                    ColorStateList.valueOf(mContext.getColor(R.color.maya_color_theme))
            }
            2 -> {
                imageView.setImageResource(R.drawable.cartoon_ic_arrow_down)
                imageView.imageTintList =
                    ColorStateList.valueOf(mContext.getColor(R.color.maya_green))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bindItemDataToView(
        binding: ItemConcreteRankListLayoutBinding,
        itemData: ItemBean,
        position: Int
    ) {
        binding.apply {
            when (position) {
                0 -> setRankView(ivFanlist, tvRank, R.drawable.live_fanlist_top1)
                1 -> setRankView(ivFanlist, tvRank, R.drawable.live_fanlist_top2)
                2 -> setRankView(ivFanlist, tvRank, R.drawable.live_fanlist_top3)
                else -> {
                    ivFanlist.visibility = GONE
                    tvRank.visibility = View.VISIBLE
                    val typeface: Typeface =
                        Typeface.createFromAsset(mContext.assets, "fonts/futuraLT.ttf")
                    tvRank.typeface = typeface
                    tvRank.text = (position + 1).toString()
                    //setRankIndicator(ivRankNotice, itemData)
                }
            }

            GlideX.getInstance().rectF(mContext, itemData.coverMiddle, ivAlbumImg, 8)
            tvRank.text = "${position + 1}"
            tvContentTitle.text = itemData.title ?: ""
            if (itemData.albumIntro != null && itemData.albumIntro.equals("")) tvTitleSecond.visibility =
                GONE else tvTitleSecond.text = itemData.albumIntro
            tvHot.text = itemData.popularity ?: ""
        }
    }

    private fun setRankView(
        ivFan: ImageView,
        tvRank: TextView,
        topDraw: Int
    ) {
        ivFan.visibility = VISIBLE
        tvRank.visibility = GONE
        ivFan.background = mContext.resources.getDrawable(topDraw)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_concrete_rank_list_layout
    }
}