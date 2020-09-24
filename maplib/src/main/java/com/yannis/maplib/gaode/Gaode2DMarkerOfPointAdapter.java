package com.yannis.maplib.gaode;

import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yannis.maplib.R;

import java.util.List;

/**
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-31
 */
public class Gaode2DMarkerOfPointAdapter extends BaseQuickAdapter<Gaode2dMarkerOfPointBean, BaseViewHolder> {

    public Gaode2DMarkerOfPointAdapter(int layoutResId, @Nullable List<Gaode2dMarkerOfPointBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Gaode2dMarkerOfPointBean item) {
        helper.addOnClickListener(R.id.rb_gaode_2d_item_tag);
        RadioButton checkBox = helper.getView(R.id.rb_gaode_2d_item_tag);
        checkBox.setChecked(item.isChecked());
        helper.setText(R.id.tv_gaode_2d_item_title_main, item.getPoiItem().getTitle());
        helper.setText(R.id.tv_gaode_2d_item_title_second, item.getPoiItem().getSnippet());
    }
}
