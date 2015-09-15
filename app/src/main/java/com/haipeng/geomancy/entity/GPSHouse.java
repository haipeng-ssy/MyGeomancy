package com.haipeng.geomancy.entity;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class GPSHouse {
    double longitude;//gps的经度
    double latitude;//gps的纬度
    double altitude;//gps的高度

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
