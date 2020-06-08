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
        if (helper != null) {
            if (item != null) {
                val checkBox = helper.getView<CheckBox>(R.id.cb_content)
                if (helper.adapterPosition == 0) {
                    checkBox.isChecked = true
                }
                checkBox.text = item.displayName
                if (item.isChecked) checkBox.isChecked = true else checkBox.isChecked = false
            }
        }
    }
}