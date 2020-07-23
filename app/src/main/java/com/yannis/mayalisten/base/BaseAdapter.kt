package com.yannis.mayalisten.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * BaseAdapter RecyclerView的Adapter基类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/15
 */
abstract class BaseAdapter<T, VDB : ViewDataBinding>(val context: Context, var data: ArrayList<T>) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private lateinit var singleClickListener: OnSingleClickListener<T>
    private lateinit var longClickListener: OnRvLongClickListener<T>
    private lateinit var removeListener: OnRemoveListener<T>

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflate =
            DataBindingUtil.inflate<VDB>(LayoutInflater.from(context), getLayoutId(), parent, false)
        return BaseViewHolder(inflate.root)
    }

    /**
     * ViewHolder
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 数量
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 绑定数据toView + 点击事件
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<VDB>(holder.itemView)
        bindItemDataToView(binding, data[position])
        singleClickListener.setOnSingleClickListener(data[position], position, data)
        longClickListener.setOnLongClickListener(data[position], position, data)
        removeListener.setOnRemoveListener(data[position], position, data)
    }

    /**
     * 刷新、第一次添加数据
     */
    fun setNewData(newData: ArrayList<T>) {
        this.data = newData
        notifyDataSetChanged()
    }

    /**
     * 加载更多数据
     */
    fun addData(newData: ArrayList<T>) {
        this.data.addAll(newData)
        notifyItemRangeChanged(this.data.size - newData.size, newData.size)
    }

    /**
     * 将itemData的值绑定至ItemView
     */
    abstract fun bindItemDataToView(binding: VDB?, itemData: T)

    /**
     * 设置layout
     */
    abstract fun getLayoutId(): Int

    fun setOnSingleClickListener(onSingleClickListener: OnSingleClickListener<T>) {
        this.singleClickListener = onSingleClickListener
    }

    fun setOnLongClickListener(onLongClickListener: OnRvLongClickListener<T>) {
        this.longClickListener = onLongClickListener
    }

    fun setOnRemoveListener(onRemoveListener: OnRemoveListener<T>) {
        this.removeListener = onRemoveListener
    }

    interface OnSingleClickListener<T> {
        // 单击
        fun setOnSingleClickListener(itemData: T, position: Int, data: ArrayList<T>): Void
    }

    interface OnRvLongClickListener<T> {
        // 长按
        fun setOnLongClickListener(itemData: T, position: Int, data: ArrayList<T>): Void
    }

    interface OnRemoveListener<T> {
        // 删除item
        fun setOnRemoveListener(itemData: T, position: Int, data: ArrayList<T>)
    }


}
