package com.yannis.maplib.baidu;

import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yannis.maplib.R;

import java.util.List;

/**
 * BaiduMarkerPointAdapter 百度地图 搜索地址适配器
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-30
 */
public class BaiduMarkerPointAdapter extends BaseQuickAdapter<PoiInfoWrap, BaseViewHolder> {


    public BaiduMarkerPointAdapter(int layoutResId, @Nullable List<PoiInfoWrap> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, PoiInfoWrap item) {

        helper.addOnClickListener(R.id.rb_baidu_item_tag);
        RadioButton checkBox = helper.getView(R.id.rb_baidu_item_tag);
        checkBox.setChecked(item.isSelected());
        //Log.e(TAG, "ヽ(｀Д´)ﾉ -> convert : " +item.getPoiInfo().toString());
        helper.setText(R.id.tv_baidu_item_title_main, item.getPoiInfo().getName());
        helper.setText(R.id.tv_baidu_item_title_second, item.getPoiInfo().getAddress());
    }
}
