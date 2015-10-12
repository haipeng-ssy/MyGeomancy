package com.haipeng.geomancy.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.MyInterface.HasDgressChange;
import com.haipeng.geomancy.MyInterface.HighFiveMetersChange;
import com.haipeng.geomancy.MyInterface.XYFiveMetersChange;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.myView.CompassView;
import com.haipeng.geomancy.sensor.GPSLocation;
import com.haipeng.geomancy.util.HanziToPinyin;

import org.w3c.dom.Text;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sunyiyan on 2015/1/24.
 */
public abstract class BaseActivity extends ActionBarActivity {

    HasDgressChange mHDC;
    ActionBar actionBar;
    View actionbarView = null;
    public String point = "";

    //指南针
    protected final Handler handler = new Handler();
    float mDirection;
    float mTargetDirection;

    //  ImageView mIV;
    CompassView mIV;
    AccelerateInterpolator mInterpolator;
    CompassView mPointer;

    SensorManager sm;
    Sensor magnetic;
    Sensor accelator;
    Sensor mOrientationSensor;
    float[] accelatorValues = new float[3];
    float[] magneticValues = new float[3];
    boolean showLog = false;
    String TAG = "方向";
    float dgress;
    float currentDegree;
    float accelaValueX;
    float accelaValueY;
    //...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionbarView = LayoutInflater.from(this).inflate(R.layout.actionbarview, null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(actionbarView);
        actionBar.hide();

        needExecute();//这个方法执行完了才会执行setContentView(R.layout....)，
        // 父类onCreate里的方法执行完了，才会执行子类onCreate里的方法
        MyApplication.getInstance().addActionBarActivity(this);
    }

    //ActionBar
    public void hideActionBar() {
        actionBar.hide();
    }
    public void showActionBar() {
        actionBar.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void needExecute() {
        initView();
        setUpView();
        execute();
    }

    public void testGPS(final TextView view) {
        GPSLocation gpsLocation = new GPSLocation(this, new XYFiveMetersChange() {
            @Override
            public void isXYFiveMetersChange() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setText("已经移动1米远了");
                    }
                });
            }
        }, new HighFiveMetersChange() {
            @Override
            public void highFiveMetersChange() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setText("已经移动1米高了");
                    }
                });

            }
        });
    }

    //初始化的三个函数
    /**
     * UI初始化
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void setUpView();

    /**
     * 函数的执行
     */
    public abstract void execute();

    //
    public void testLocationOneMeter(double longitude, double latitude, double altitude) {
    }
    public void testLocationFiveMeters(double longitude, double latitude, double altitude) {
    }
    public void startActivity(String className, Intent intent) {
        try {
            Class clazz = Class.forName("com.haipeng.geomancy.ui." + className);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(this, clazz);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //弹出的窗口
    AlertDialog dlg = null;
    public void showMyAlterDialog(final EnsureCancel ec, String title, String txt_ensure, String txt_cancel) {
        dlg = new AlertDialog.Builder(this).create();
        if(!dlg.isShowing())
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.my_alterdialog);
        TextView tv_title = (TextView) window.findViewById(R.id.alter_dialog_txt);
        Button btn_ensure = (Button) window.findViewById(R.id.alter_dialog_ensure);
        Button btn_cancel = (Button) window.findViewById(R.id.alter_dialog_cancel);
        tv_title.setText(title);
        btn_ensure.setText(txt_ensure);

        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
                ec.ensure();
            }
        });
        if(txt_cancel == null)
        {
            btn_cancel.setVisibility(View.GONE);
        }else {
            btn_cancel.setText(txt_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.cancel();
                    ec.cancel();
                }
            });
        }
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

    }

    int i = 0;
    long first;
    long second;
    Handler mHandler;


    //指南针
    public void GetCompass(final CompassView imageView, HasDgressChange hdc) {
//        mPointer = compassView;
        if (hdc == null) {

        } else {
            mHDC = hdc;
        }

        if (imageView == null) {
            mIV = null;

        } else {
            mIV = imageView;
        }
        mInterpolator = new AccelerateInterpolator();
        sm = (SensorManager) this.getSystemService(this.getApplicationContext().SENSOR_SERVICE);

        List<Sensor> deviceSensors = sm.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor:deviceSensors)
        {
            Log.i("sensor type",sensor.getType()+"");
            Log.i("sensor name",sensor.getName()+"");
        }
        //方向传感器
        mOrientationSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

//        magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        accelator = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        accelator = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Log.i("magnetic",magnetic+"");
        Log.i("accelator",accelator+"");
//        sm.registerListener(sensorListener, accelator, SensorManager.SENSOR_DELAY_NORMAL);
//        sm.registerListener(sensorListener, magnetic, SensorManager.SENSOR_DELAY_NORMAL);

//        sm.registerListener(sensorListener, accelator, SensorManager.SENSOR_DELAY_GAME);
//        sm.registerListener(sensorListener, magnetic, SensorManager.SENSOR_DELAY_GAME);

        //方向传感器
        sm.registerListener(sensorListener,mOrientationSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    boolean just_once = false;
    long end_time = 0l;
    long start_time = 0l;
    long speed = 0l;

    long dgress_D_value = 0l;
    long time_D_value = 0l;

    //指南针实时监听器
    SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            float f[] ={0};
            if( event.sensor.getType() == Sensor.TYPE_ORIENTATION){
                        f =event.values;
            }
//            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                accelatorValues = event.values;
//                accelaValueX = accelatorValues[0];
//                accelaValueY = accelatorValues[1];
//            }
//            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                magneticValues = event.values;
//                System.out.println(event.values[0]);
//            }
//            float f[] = calculateOrientation();

//                 System.out.println(f[0]);
            if (Math.round(f[0]) == 0) {

            } else {
                dgress = f[0] % 360;
//               point = f[0]+"";

                point = (float) (Math.round((f[0]) * 10)) / 10 + "";
//                point = (float) (Math.round((f[0] + 180) * 10)) / 10 + "";
//
//            Log.i("tag",dgress+"current"+currentDegree);
//            Log.i("tag",accelaValueX+"current"+accelaValueY);
                //当x轴或者y轴的加速度小于0.4的时候执行
//
//                if (just_once == false) {
//                    start_time = System.currentTimeMillis();
//                    just_once = true;
//                }
//                end_time = System.currentTimeMillis();
//                ;
//                time_D_value = end_time - start_time;
//                if (time_D_value == 0) {
//
//                } else {
//                    dgress_D_value = Long.parseLong(String.valueOf(Math.round((currentDegree + dgress) * 100f)));
//                    speed = dgress_D_value / time_D_value;
//                  Log.i("tag",speed+"");
//                }
//

                //左边反转还是右边反转大于0.4的
//                if ((accelaValueX < 0.4f && accelaValueX > -0.4f) || (accelaValueY < 0.4f && accelaValueY > -0.4f)) {
//
//                    //但是角度有变化了5度，让其执行
//                    if (Math.abs(currentDegree + dgress) > 5) {
//                        if (mIV == null) {
//
//                        } else {
//                            exeRotate();
//                        }
//                        currentDegree = -dgress;
//                    }
//                } else {

                    //当x轴或者y轴的加速度大于0.4且变化的角度需要大于1度
                    if (Math.abs(currentDegree + dgress) > 0.1) {
                        if (mIV == null) {

                        } else {
                            exeRotate();
//                            mIV.updateDirection(currentDegree);
                        }
                    }
                    currentDegree = -dgress;
                }
                if (mHDC != null) {
                    //接口回调
                    mHDC.hadgressChange(-currentDegree);
                }
//            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

//    public synchronized void exeRotate() {
    public void exeRotate() {

        RotateAnimation ra = new RotateAnimation(currentDegree,
                -dgress, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画的持续时间
        ra.setDuration(200);
        ra.setFillAfter(true);
        //运行动画
        mIV.startAnimation(ra);
    }

    private float[] calculateOrientation() {

        float[] values = new float[3];
        float[] R = new float[9];

        SensorManager.getRotationMatrix(R, null, accelatorValues, magneticValues);

        SensorManager.getOrientation(R, values);
//        SensorManager.getOrientation();
        if (R.length == 9) {
//            Log.i("A",""+R[1]+"\t"+R[4]+"\t");
        } else {
//            Log.i("",""+R[1]+"\t"+R[5]+"\t");
        }
//        double b =

//        Log.i(TAG," accelatorValues"+accelatorValues[0]+"\t"+accelatorValues[1]+"\t"+accelatorValues[2]+
//                "\n magneticValues"+magneticValues[0]+"\t"+magneticValues[1]+"\t"+magneticValues[2]+
//                "\n Values"+(float) Math.toDegrees(values[0])+"\t"+(float) Math.toDegrees(values[1])+"\t"+(float) Math.toDegrees(values[2]));

        values[0] = (float) Math.toDegrees(values[0]);
        if (showLog)
//            Log.i(TAG, values[0] + "");
            values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        if (values[0] >= -5 && values[0] < 5) {
            if (showLog)
                Log.i(TAG, "正北");
        } else if (values[0] >= 5 && values[0] < 85) {
            if (showLog)
                Log.i(TAG, "东北");
        } else if (values[0] >= 85 && values[0] <= 95) {
            if (showLog)
                Log.i(TAG, "正东");
        } else if (values[0] >= 95 && values[0] < 175) {
            if (showLog)
                Log.i(TAG, "东南");
        } else if ((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175) {
            if (showLog)
                Log.i(TAG, "正南");
        } else if (values[0] >= -175 && values[0] < -95) {
            if (showLog)
                Log.i(TAG, "西南");
        } else if (values[0] >= -95 && values[0] < -85) {
            if (showLog)
                Log.i(TAG, "正西");
        } else if (values[0] >= -85 && values[0] < -5) {
            if (showLog)
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

    public String getPhoneId() {
        String imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return imei;
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

    @Override
    protected void onPause() {
        super.onPause();
        if (sm != null) {
            sm.unregisterListener(sensorListener);
            sm = null;
        }
    }

    public void isLianjieWangluo() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        final boolean boo_wifi = wifi;
        final boolean boo_internet = internet;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (boo_wifi | boo_internet) {
                    //执行相关操作
                    Toast.makeText(getApplicationContext(),
                            "网络异常，稍后再试", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "请链接网络", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void clearSPHomeOwnerId() {
        SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        String homeOwnerId = sp.getString("homeOwnerId", "");
        if (!homeOwnerId.equals(""))
            editor.remove("homeOwnerId");
        editor.commit();
    }

    public String getFullPinYin(String source){
        if (!Arrays.asList(Collator.getAvailableLocales()).contains(Locale.CHINA)) {
            return source;
        }
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(source);
        if (tokens == null || tokens.size() == 0) {
            return source;
        }
        StringBuffer result = new StringBuffer();
        for (HanziToPinyin.Token token : tokens) {
            if (token.type == HanziToPinyin.Token.PINYIN) {
                result.append(token.target);
            } else {
                result.append(token.source);
            }
        }
            return result.toString();
        }

    private float normalizeDegree(float dgree) {
        return (dgree + 720) % 360;
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActionBarActivity(this);
        super.onDestroy();
    }

    //返回键的控制
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            i +=1;
            if(i == 1)
            {
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i==1)
                        {

                            finish();

                        }
                    }
                },500);
                first = System.currentTimeMillis();
                Log.i("first",first+"");
            }
            if(i == 2)
            {
                second = System.currentTimeMillis();
                Log.i("second",second+"");
                if(second - first<500)
                {
                    if(mHandler!=null)
                    {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    showMyAlterDialog(new EnsureCancel() {
                        @Override
                        public void ensure() {
                            MyApplication.getInstance().killActivitys();
                            finish();
                        }
                        @Override
                        public void cancel() {
                            i = 0;
                        }
                    },"确认要退出本应用吗","确定","取消");
                }else{
                    i = 0;
                    first = System.currentTimeMillis();
                }
            }
            if(i==3)
            {
                i = 1;
                first = System.currentTimeMillis();
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i==1)
                        {
                            finish();
                        }
                    }
                },500);
            }
            Log.i("i=",i+"");
        }
        return true;
    }
}