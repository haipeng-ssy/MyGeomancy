package com.haipeng.geomancy.sensor;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.HighFiveMetersChange;
import com.haipeng.geomancy.MyInterface.XYFiveMetersChange;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class GPSLocation {
    ActionBarActivity mContext;
    LocationManager locationManager;
    Location mLocation;
    double longitude,latitude,altitude;
    XYFiveMetersChange mXYFiveMetersChange;
    HighFiveMetersChange mHighFiveMetersChange;
    public GPSLocation(ActionBarActivity context,XYFiveMetersChange xyFiveMetersChange,HighFiveMetersChange highFiveMetersChange){
        mContext = context;
        mXYFiveMetersChange = xyFiveMetersChange;
        mHighFiveMetersChange = highFiveMetersChange;
        getMyLocation();
    }
    public final Location getMyLocation(){

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(mContext,"GPS已关闭，请手动打开GPS后再试...",Toast.LENGTH_LONG).show();
            mContext.finish();
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider =locationManager.getBestProvider(criteria,true);
        mLocation = locationManager.getLastKnownLocation(provider);
        if(mLocation == null)
        {

        }else {
            longitude = mLocation.getLongitude();
            latitude = mLocation.getLatitude();
            altitude = mLocation.getAltitude();
            Log.i("TAG","longitude"+longitude);
            Log.i("TAG","latitude"+latitude);
            Log.i("TAG","altitude"+altitude);

        }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);
        return mLocation;
    }
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Log.d("TAG","时间"+location.getTime());
            Log.d("TAG","经度"+location.getLongitude());
            Log.d("TAG","纬度"+location.getLatitude());
            Log.d("TAG","海拔"+location.getAltitude());
//            mXYFiveMetersChange.isXYFiveMetersChange();

            if(mLocation == null)
            {
                mLocation = location;
                longitude = mLocation.getLongitude();
                latitude = mLocation.getLatitude();
                altitude = mLocation.getAltitude();
            }else {

                double a = location.getLatitude() - longitude;
                double b = location.getLatitude() - latitude;
                double high_distance = location.getAltitude() - altitude;
                double xy_distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
                if (high_distance > 1) {

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    Toast.makeText(mContext,"走的距离大与5米了",Toast.LENGTH_LONG).show();
                    mXYFiveMetersChange.isXYFiveMetersChange();
                }
                if (xy_distance > 1) {
                    altitude = location.getAltitude();
                    mHighFiveMetersChange.highFiveMetersChange();
                    Toast.makeText(mContext,"走的高度大与5米了",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
             switch (status){
                 case LocationProvider.AVAILABLE:
                     Log.d("TAG","当前GPS状态为可见状态");
                     break;
                 case LocationProvider.OUT_OF_SERVICE:
                     Log.d("TAG","当前GPS状态为服务区外状态");
                     break;
                 case LocationProvider.TEMPORARILY_UNAVAILABLE:
                     Log.d("TAG","当前GPS状态为暂停服务状态");
                     break;
             }
        }

        @Override
        public void onProviderEnabled(String provider) {
//             location = locationManager.getLastKnownLocation(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private Location getLocation(){
        return mLocation;
    }

    private Double getLocationLongitude(){
        return longitude;
    }

    private Double getLocationLatitude(){
        return latitude;
    }
    private Double getLocationAltitude(){
        return altitude;
    }

}
