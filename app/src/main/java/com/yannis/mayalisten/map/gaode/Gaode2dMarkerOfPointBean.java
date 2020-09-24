package com.yannis.mayalisten.map.gaode;

import com.amap.api.services.core.PoiItem;

import java.io.Serializable;

/**
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-31
 */
public class Gaode2dMarkerOfPointBean implements Serializable {

    private PoiItem mPoiItem;
    private boolean isChecked;

    public Gaode2dMarkerOfPointBean(PoiItem poiItem, boolean isChecked) {
        mPoiItem = poiItem;
        this.isChecked = isChecked;
    }

    public PoiItem getPoiItem() {
        return mPoiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        mPoiItem = poiItem;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Gaode2dMarkerOfPointBean{" +
                "mPoiItem=" + mPoiItem +
                ", isChecked=" + isChecked +
                '}';
    }
}
