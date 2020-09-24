package com.yannis.maplib.utils;

/**
 * LatLonBean： 坐标系"经度、纬度"bean类
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-06-19
 */
public class LatLonBean {

    private double lat;
    private double lon;

    public LatLonBean(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "LatLonBean{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
