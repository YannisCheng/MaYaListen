package com.yannis.mayalisten.adapter

import android.content.Context
import com.yannis.baselib.base.BaseAdapter
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.IndexChooseBean
import com.yannis.mayalisten.databinding.ItemIndexChoosePlayListLayoutBinding

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/17
 */
class IndexChooseAdapter(context: Context, data: ArrayList<IndexChooseBean>) :
    BaseAdapter<IndexChooseBean, ItemIndexChoosePlayListLayoutBinding>(context, data) {

    override fun bindItemDataToView(
        binding: ItemIndexChoosePlayListLayoutBinding,
        itemData: IndexChooseBean,
        position: Int
    ) {
        binding.apply {
            tvIndexShow.text = itemData.indexInterval
            if (itemData.isCheckEd) {
                tvIndexShow.background =
                    mContext.resources.getDrawable(R.drawable.bg_selected_index_choose_item)
            } else {
                tvIndexShow.background =
                    mContext.resources.getDrawable(R.drawable.bg_index_choose_item)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_index_choose_play_list_layout
    }
}