package com.yannis.mayalisten.widget

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.yannis.baselib.widget.BasePopupWindow
import com.yannis.mayalisten.adapter.IndexChooseAdapter
import com.yannis.mayalisten.bean.AlbumItemBean
import com.yannis.mayalisten.bean.IndexChooseBean
import com.yannis.mayalisten.databinding.WidgetIndexChooseItemDialogLayoutBinding

/**
 * IndexChoosePopupWindow 选择专辑范围PopupWindow
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/16 - 14:20
 */
class IndexChoosePopupWindow(var context: Context, var beans: List<AlbumItemBean>) :
    BasePopupWindow(context) {

    private var indexChooseAdapter: IndexChooseAdapter
    private var binding: WidgetIndexChooseItemDialogLayoutBinding =
        WidgetIndexChooseItemDialogLayoutBinding.inflate(
            LayoutInflater.from(context), null, false
        )

    init {
        val arrayList = ArrayList<IndexChooseBean>()
        arrayList.add(IndexChooseBean("1~20", true))
        arrayList.add(IndexChooseBean("21~40", false))
        arrayList.add(IndexChooseBean("41~60", false))
        arrayList.add(IndexChooseBean("61~80", false))
        arrayList.add(IndexChooseBean("81~100", false))
        arrayList.add(IndexChooseBean("101~120", false))
        arrayList.add(IndexChooseBean("121~140", false))
        arrayList.add(IndexChooseBean("141~160", false))
        arrayList.add(IndexChooseBean("1~20", false))
        arrayList.add(IndexChooseBean("21~40", false))
        arrayList.add(IndexChooseBean("41~60", false))

        indexChooseAdapter = IndexChooseAdapter(arrayList)
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
            recyclerView.adapter = indexChooseAdapter
        }

        indexChooseAdapter.setOnItemChildClickListener { adapter, view, position ->

            if (view.id == com.yannis.mayalisten.R.id.tv_index_show) {
                Log.e("TAG", "内容为：" + adapter.data[position].toString())
                val bean = adapter.data[position] as IndexChooseBean
                bean.isCheckEd = true
                (adapter.data as ArrayList<IndexChooseBean>).forEach {
                    if (it.indexInterval != bean.indexInterval) {
                        it.isCheckEd = false
                    }
                }
                indexChooseAdapter.notifyDataSetChanged()
                this.dismiss()
            }

        }

        setBaseDialogSettingNoBg(binding)
    }
}