package com.haipeng.geomancy.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.haipeng.geomancy.MyInterface.HasDgressChange;

/**
 * Created by Administrator on 2015/2/8.
 */
public class Compass {

    SensorManager sm;
    Sensor magnetic;
    Sensor accelator;
    float[] accelatorValues = new float[3];
    float[] magneticValues  = new float[3];
    String TAG ="sensor";
    float currentDegree = 0f;
    ActionBarActivity mActivity;
    HasDgressChange mHasDgressChange=null;
    public Compass(ActionBarActivity activity){
        mActivity =activity;
        sm = (SensorManager) mActivity.getSystemService(mActivity.getApplicationContext().SENSOR_SERVICE);
        magnetic = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelator = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(sensorListener, accelator, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(sensorListener, magnetic,SensorManager.SENSOR_DELAY_NORMAL);
    }
    public Compass(ActionBarActivity activity,HasDgressChange hasDgressChange){
        mHasDgressChange = hasDgressChange;
        mActivity =activity;
        sm = (SensorManager) mActivity.getSystemService(mActivity.getApplicationContext().SENSOR_SERVICE);
        magnetic = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelator = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(sensorListener, accelator, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(sensorListener, magnetic,SensorManager.SENSOR_DELAY_NORMAL);
    }

    SensorEventListener sensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                accelatorValues =event.values;
            }if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                magneticValues = event.values;
//                System.out.println(event.values[0]);
            }
            if(mHasDgressChange == null)
            {

            }else {
                float f[] = calculateOrientation();
                mHasDgressChange.hadgressChange(currentDegree);
                currentDegree = f[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

    private  float[] calculateOrientation() {
        boolean showLog = false;
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelatorValues, magneticValues);
        SensorManager.getOrientation(R, values);

        values[0] = (float) Math.toDegrees(values[0]);
        if(showLog)
        Log.i(TAG, values[0] + "");
        //values[1] = (float) Math.toDegrees(values[1]);
        //values[2] = (float) Math.toDegrees(values[2]);

        if(values[0] >= -5 && values[0] < 5){
            if(showLog)
            Log.i(TAG, "正北");
        }
        else if(values[0] >= 5 && values[0] < 85){
            if(showLog)
            Log.i(TAG, "东北");
        }
        else if(values[0] >= 85 && values[0] <=95){
            if(showLog)
            Log.i(TAG, "正东");
        }
        else if(values[0] >= 95 && values[0] <175){
            if(showLog)
            Log.i(TAG, "东南");
        }
        else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
            if(showLog)
            Log.i(TAG, "正南");
        }
        else if(values[0] >= -175 && values[0] <-95){
            if(showLog)
            Log.i(TAG, "西南");
        }
        else if(values[0] >= -95 && values[0] < -85){
            if(showLog)
            Log.i(TAG, "正西");
        }
        else if(values[0] >= -85 && values[0] <-5){
            if(showLog)
            Log.i(TAG, "西北");
        }
        float f[] = new float[3];
        f[0] = values[0];
        f[1] = values[1];
        f[2] = values[2];
//        System.out.println("up_down"+f[1]);
//        System.out.println("left_right"+f[2]);
        return f;
    }
/*
    values[0]  表示Z轴的角度：方向角，我们平时判断的东西南北就是看这个数据的，
    经过我的实验，发现了一个有意思的事情，
    也就是说使用第一种方式获得方向（磁场+加速度）得到的数据范围是（-180～180）,
    也就是说，0表示正北，90表示正东，180/-180表示正南，-90表示正西。
    而第二种方式（直接通过方向感应器）数据范围是（0～360）360/0表示正北，
    90表示正东，180表示正南，270表示正西。

    values[1]  表示X轴的角度：俯仰角   即由静止状态开始，前后翻转

    values[2]  表示Y轴的角度：翻转角  即由静止状态开始，左右翻转*/

}
