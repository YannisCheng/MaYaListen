package com.yannis.mayalisten.adapter

import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.AggregateRankListTabsBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
class RankOfItemTabAdapter(data: MutableList<AggregateRankListTabsBean>?) :
    BaseQuickAdapter<AggregateRankListTabsBean, BaseViewHolder>(data) {
    init {
        mLayoutResId = R.layout.item_rank_of_item_tab_layout
    }

    override fun convert(helper: BaseViewHolder?, item: AggregateRankListTabsBean?) {

        helper?.let {
            helper.addOnClickListener(R.id.cb_content)
            item?.let {
                val checkBox = helper.getView<CheckBox>(R.id.cb_content)
                checkBox.text = item.displayName
                checkBox.isChecked = item.isChecked
            }
        }

    }
}