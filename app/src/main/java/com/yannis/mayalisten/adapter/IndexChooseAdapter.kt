package com.yannis.mayalisten.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.IndexChooseBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/17
 */
class IndexChooseAdapter(data: MutableList<IndexChooseBean>) :
    BaseQuickAdapter<IndexChooseBean, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_index_choose_play_list_layout
    }

    override fun convert(helper: BaseViewHolder, item: IndexChooseBean) {
        helper.addOnClickListener(R.id.tv_index_show)
        helper.getView<TextView>(R.id.tv_index_show).text = item.indexInterval
        if (item.isCheckEd) {
            helper.getView<TextView>(R.id.tv_index_show).background =
                mContext.resources.getDrawable(R.drawable.bg_selected_index_choose_item)
        } else {
            helper.getView<TextView>(R.id.tv_index_show).background =
                mContext.resources.getDrawable(R.drawable.bg_index_choose_item)
        }
    }
}