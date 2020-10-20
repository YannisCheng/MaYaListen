package com.yannis.mayalisten.adapter

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yannis.baselib.widget.TextShrinkExpansion
import com.yannis.mayalisten.R
import com.yannis.mayalisten.bean.Evaluation

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
class SingleAlbumEvaluationAdapter(data: MutableList<Evaluation>?) :
    BaseQuickAdapter<Evaluation, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_single_album_evaluation_layout
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun convert(helper: BaseViewHolder?, item: Evaluation?) {

        item?.let {

            helper?.let {
                Glide.with(mContext).load(item.smallHeader)
                    .into(it.getView<ImageView>(R.id.iv_avator))
                //it.getView<TextView>(R.id.tv_content).text = item.content
                TextShrinkExpansion(it.getView<TextView>(R.id.tv_content), item.content, 2)
                it.getView<TextView>(R.id.tv_date).text = item.commentId.toString()
                it.getView<TextView>(R.id.tv_repet).text = item.replyCount.toString()
                it.getView<TextView>(R.id.tv_good).text = item.likes.toString()
                it.getView<TextView>(R.id.tv_nick_name).text = item.nickname
                it.getView<TextView>(R.id.tv_nick_name).text = item.nickname

                item.takeIf { it.isHighQuality }?.let {
                    helper.getView<ImageView>(R.id.iv_tag).background =
                        mContext.resources.getDrawable(R.drawable.main_ic_high_quality)
                }

            }
        }
    }
}