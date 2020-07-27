package com.yannis.mayalisten.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.baselib.widget.BasePopupWindow
import com.yannis.mayalisten.R
import com.yannis.mayalisten.adapter.TimeCloseAdapter
import com.yannis.mayalisten.bean.TimeCloseBean
import com.yannis.mayalisten.constant.TimeCloseConstants
import com.yannis.mayalisten.databinding.TimeCloseLayoutBinding

/**
 * TimeClosePopupWindow 定时关闭弹窗
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class TimeClosePopupWindow(private var mContext: Context) : BasePopupWindow(mContext) {

    private var timeCloseAdapter: TimeCloseAdapter = TimeCloseAdapter(TimeCloseConstants.list)
    private var binding: TimeCloseLayoutBinding =
        TimeCloseLayoutBinding.inflate(LayoutInflater.from(mContext), null, false)

    init {
        binding.apply {
            root.measure(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            tvClose.setOnClickListener {
                dismiss()
            }
            recyclerView.layoutManager = LinearLayoutManager(mContext)
            recyclerView.adapter = timeCloseAdapter
        }

        setBaseDialogSetting(binding)

        timeCloseAdapter.setOnItemClickListener { adapter, _, position ->
            changeState(adapter, position)
        }

        timeCloseAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.rb_time_close -> changeState(adapter, position)
            }
        }
    }

    private fun changeState(
        adapter: BaseQuickAdapter<Any, BaseViewHolder>,
        position: Int
    ) {
        val timeCloseBean = adapter.data[position] as TimeCloseBean
        timeCloseBean.isChecked = true
        (adapter.data as List<TimeCloseBean>).forEach {
            if (timeCloseBean.itemDescribtion != it.itemDescribtion) {
                it.isChecked = false
            }
        }
        adapter.notifyDataSetChanged()
    }

}