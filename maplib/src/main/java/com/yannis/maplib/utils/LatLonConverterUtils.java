package com.yannis.maplib.utils;

/**
 * LatLonConverterUtils："不同地图坐标系" 的 "经纬度" 转换工具类
 * <p>
 * 涉及到的地图坐标系：
 * 1、WGS84: Google Earth、Google Map除了中国之外的国家使用；
 * 2、GCJ02: 火星坐标系。Google Map中国、高德使用。中国国家测绘局制定的坐标系统，WGS84加密后的坐标。
 * 3、BD09:  百度坐标。GCJ02加密后的坐标
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-06-19
 */
public class LatLonConverterUtils {

    private static double pi = 3.1415926535897932384626;
    private static double a = 6378245.0;
    private static double ee = 0.00669342162296594323;

    /**
     * WGS8484 --> 火星坐标系(GCJ-02)
     *
     * @param lat
     * @param lon
     */
    public static LatLonBean wgs84ToGcj02(double lat, double lon) {
        if (isOutOfChina(lat, lon)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLonBean(mgLat, mgLon);
    }

    /**
     * 火星坐标系 (GCJ-02) -->  WGS84
     *
     * @param lon
     * @param lat
     * @return
     */
    public static LatLonBean gcj02ToWgs84(double lat, double lon) {
        LatLonBean wgs = transform(lat, lon);
        double lontitude = lon * 2 - wgs.getLon();
        double latitude = lat * 2 - wgs.getLat();
        return new LatLonBean(latitude, lontitude);
    }

    /**
     * 火星坐标系 (GCJ-02) --> 百度坐标系 (BD-09)
     *
     * @param lat
     * @param lon
     */
    public static LatLonBean gcj02ToBd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new LatLonBean(bdLat, bdLon);
    }

    /**
     * 百度坐标系 (BD-09) --> 火星坐标系 (GCJ-02)
     *
     * @param
     * @param
     * @return
     */
    public static LatLonBean bd09ToGcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gcjLon = z * Math.cos(theta);
        double gcjLat = z * Math.sin(theta);
        return new LatLonBean(gcjLat, gcjLon);
    }

    /**
     * 百度坐标系 (BD-09) --> WGS84
     *
     * @param lat
     * @param lon
     * @return
     */
    public static LatLonBean bd09ToWgs84(double lat, double lon) {
        LatLonBean gcj02 = bd09ToGcj02(lat, lon);
        LatLonBean wgs84 = gcj02ToWgs84(gcj02.getLat(), gcj02.getLon());
        return wgs84;
    }

    /**
     * WGS84 --> 百度坐标系 (BD-09)
     *
     * @param lat
     * @param lon
     * @return
     */
    public static LatLonBean wgs84ToBd09(double lat, double lon) {
        LatLonBean gcj02 = wgs84ToGcj02(lat, lon);
        LatLonBean bd09 = gcj02ToBd09(gcj02.getLat(), gcj02.getLon());
        return bd09;
    }

    /**
     * 当前坐标是否为"中国之外的国家"
     *
     * @param lat
     * @param lon
     * @return
     */
    public static boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static LatLonBean transform(double lat, double lon) {
        if (isOutOfChina(lat, lon)) {
            return new LatLonBean(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new LatLonBean(mgLat, mgLon);
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
}
