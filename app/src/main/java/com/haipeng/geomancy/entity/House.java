package com.haipeng.geomancy.entity;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class House {
    long id; //房屋的ID,根据
    GPSHouse gpsHouse;//房屋的gps信息

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setGpsHouse(GPSHouse gpsHouse) {
        this.gpsHouse = gpsHouse;
    }

    public GPSHouse getGpsHouse() {
        return gpsHouse;
    }

}
