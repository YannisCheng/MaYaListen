package com.yannis.maplib.baidu;

import java.io.Serializable;

/**
 * MapMarkersData 坐标信息
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-29
 */
public class MapMarkersData implements Serializable {

    private double latitude;
    private double longitude;
    private String title;
    private int icon;

    public MapMarkersData(double latitude, double longitude, String title, int icon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.icon = icon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "MapMarkersData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", getAddressName='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}
