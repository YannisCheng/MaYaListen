package com.yannis.mayalisten.adapter

import android.annotation.SuppressLint
import android.view.View.GONE
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.ItemBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class ConcreteRankListAdapter(data: MutableList<ItemBean>?) :
    BaseQuickAdapter<ItemBean, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_concrete_rank_list_layout
    }

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder?, item: ItemBean?) {

        helper?.let {
            val albumInfoView = it.getView<TextView>(R.id.tv_title_second)
            Glide.with(mContext).load(item?.coverMiddle).into(helper.getView(R.id.iv_album_img))
            it.getView<TextView>(R.id.tv_rank)?.text = "${helper.adapterPosition + 1}"
            it.getView<TextView>(R.id.tv_content_title)?.text = item?.title ?: ""
            if (item?.albumIntro.equals("")) albumInfoView.visibility =
                GONE else albumInfoView.text = item?.albumIntro
            it.getView<TextView>(R.id.tv_hot)?.text = item?.popularity ?: ""
        }

    }
}