package com.yannis.mayalisten.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.PaintDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.mayalisten.R
import com.yannis.mayalisten.adapter.TimeCloseAdapter
import com.yannis.mayalisten.bean.TimeCloseBean
import com.yannis.mayalisten.constant.TimeCloseConstants
import com.yannis.mayalisten.databinding.TimeCloseLayoutBinding

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class TimeClosePopupWindow(private var mContext: Context) : PopupWindow(),
    PopupWindow.OnDismissListener {

    private var window: Window
    private var timeCloseAdapter: TimeCloseAdapter
    private var binding: TimeCloseLayoutBinding


    init {
        binding = TimeCloseLayoutBinding.inflate(LayoutInflater.from(mContext), null, false)
        window = (mContext as Activity).window
        setWindowAlpha(0.4f)
        timeCloseAdapter = TimeCloseAdapter(TimeCloseConstants.list)
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


        apply {
            contentView = binding.root
            isOutsideTouchable = true
            isFocusable = true
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            setBackgroundDrawable(PaintDrawable())
            //设置popupWindow消失时的监听
            setOnDismissListener(this@TimeClosePopupWindow)
            // 更新
            update()
        }

        timeCloseAdapter.setOnItemClickListener { adapter, view, position ->
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

    override fun onDismiss() {
        setWindowAlpha(1f)
    }

    private fun setWindowAlpha(fl: Float) {
        window.let {
            val lp = it.attributes
            lp.alpha = fl
            it.attributes = lp
        }
    }

}