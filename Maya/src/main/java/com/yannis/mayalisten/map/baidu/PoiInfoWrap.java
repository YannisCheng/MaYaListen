package com.yannis.mayalisten.map.baidu;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * PoiInfoWrap PoiInfo包裹bean类
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-30
 */
public class PoiInfoWrap implements Parcelable {

    public static final Creator<PoiInfoWrap> CREATOR = new Creator<PoiInfoWrap>() {
        @Override
        public PoiInfoWrap createFromParcel(Parcel in) {
            return new PoiInfoWrap(in);
        }

        @Override
        public PoiInfoWrap[] newArray(int size) {
            return new PoiInfoWrap[size];
        }
    };
    private PoiInfo mPoiInfo;
    private boolean isSelected;

    public PoiInfoWrap(PoiInfo poiInfo, boolean isSelected) {
        mPoiInfo = poiInfo;
        this.isSelected = isSelected;
    }

    protected PoiInfoWrap(Parcel in) {
        mPoiInfo = in.readParcelable(PoiInfo.class.getClassLoader());
        isSelected = in.readByte() != 0;
    }

    public PoiInfo getPoiInfo() {
        return mPoiInfo;
    }

    public void setPoiInfo(PoiInfo poiInfo) {
        mPoiInfo = poiInfo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "PoiInfoWrap{" +
                "mPoiInfo=" + mPoiInfo +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPoiInfo, flags);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
