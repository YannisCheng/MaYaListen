package com.yannis.mayalisten.adapter

import android.widget.RadioButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.TimeCloseBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class TimeCloseAdapter(data: List<TimeCloseBean>?) :
    BaseQuickAdapter<TimeCloseBean, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_time_close_layout
    }

    override fun convert(helper: BaseViewHolder?, item: TimeCloseBean?) {
        helper?.let {
            it.addOnClickListener(R.id.rb_time_close)
            item?.let {
                helper.getView<TextView>(R.id.tv_item_content).text = item.itemDescribtion
                helper.getView<RadioButton>(R.id.rb_time_close).isChecked = it.isChecked
            }
        }
    }
}