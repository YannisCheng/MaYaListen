package com.yannis.mayalisten.map.gaode;

import java.io.Serializable;

/**
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-08-01
 */
public class Gaode2dTransferBean implements Serializable {

    private String country = null;
    private String province = null;
    private String city = null;
    private String district = null;
    private String addressName = null;
    private String addressSnippet = null;
    private double latitude = -1;
    private double longitude = -1;

    public Gaode2dTransferBean(String country, String province, String city, String district, String addressName, String addressSnippet, double latitude, double longitude) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.addressName = addressName;
        this.addressSnippet = addressSnippet;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressSnippet() {
        return addressSnippet;
    }

    public void setAddressSnippet(String addressSnippet) {
        this.addressSnippet = addressSnippet;
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

    @Override
    public String toString() {
        return "Gaode2dTransferBean{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", addressName='" + addressName + '\'' +
                ", addressSnippet='" + addressSnippet + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
