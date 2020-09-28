package com.yannis.baselib.base

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
abstract class BaseAdapter<T, VDB : ViewDataBinding>(
    val mContext: Context,
    var data: ArrayList<T>?
) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private lateinit var itemClickCallBack: OnItemClickCallBack<T>
    private lateinit var itemLongClickCallBack: OnItemLongClickCallBack<T>
    private var viewId: Int = 0


    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflate =
            DataBindingUtil.inflate<VDB>(
                LayoutInflater.from(mContext),
                getLayoutId(),
                parent,
                false
            )
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
        if (data == null) {
            return 0
        } else {
            return data!!.size
        }
    }

    /**
     * 绑定数据toView + 点击事件
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<VDB>(holder.itemView)
        data?.let {
            binding?.let {
                bindItemDataToView(it, data!![position], position)
            }
        }

        holder.itemView.setOnLongClickListener {
            itemLongClickCallBack.onItemLongClickListener(data!![position], position, data!!)
        }

        holder.itemView.setOnClickListener {
            itemClickCallBack.onItemClickListener(data!![position], position, data!!)
        }
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
        this.data?.addAll(newData)
        notifyItemRangeChanged((this.data?.size ?: 0) - newData.size, newData.size)
    }

    /**
     * 将itemData的值绑定至ItemView
     */
    abstract fun bindItemDataToView(binding: VDB, itemData: T, position: Int)

    /**
     * 设置layout
     */
    abstract fun getLayoutId(): Int

    fun setOnItemClickListener(onItemClickCallBack: OnItemClickCallBack<T>) {
        this.itemClickCallBack = onItemClickCallBack
    }

    fun setOnItemLongClickListener(onItemLongClickCallBack: OnItemLongClickCallBack<T>) {
        this.itemLongClickCallBack = onItemLongClickCallBack
    }

    interface OnItemClickCallBack<T> {
        // 单击
        fun onItemClickListener(itemData: T, position: Int, data: ArrayList<T>)
    }

    interface OnItemLongClickCallBack<T> {
        // 长按
        fun onItemLongClickListener(itemData: T, position: Int, data: ArrayList<T>): Boolean
    }

}
