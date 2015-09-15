package com.haipeng.geomancy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.myView.CompassView;

/**
 * Created by Sunyiyan on 2015/1/24.
 */
public class EntranceActivity extends BaseActivity implements View.OnClickListener {

    Button btn_bottom_center;
//    ImageView iv_compass;

    float a = -1f;
    long a_time;
    float b = -1f;
    float c = -1f;

//    ImageView rl_box_compass;

//    ImageView mCompassView;
    CompassView mCompassView;
    PopupWindow popupWindow;
    int key_back = 0;

    ScrollView mSV;

    public static boolean isNeedToPay = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        showMyAlterDialog(new EnsureCancel() {
            @Override
            public void ensure() {

            }

            @Override
            public void cancel() {

            }
        },"请倾斜屏幕，并360度旋转手机，以矫正指南针","确定",null);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_entrance);
        mSV = (ScrollView) findViewById(R.id.activity_entrance_sv);
        btn_bottom_center = (Button) findViewById(R.id.entrance_activity_btn_bottom_center);
//        mCompassView = (ImageView) findViewById(R.id.entrance_activity_comparessView);
        mCompassView = (CompassView)findViewById(R.id.entrance_activity_comparessView);
    }

    @Override
    public void setUpView() {

        btn_bottom_center.setOnClickListener(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap_bg = BitmapFactory.decodeResource(getResources(),R.drawable.background_xiangyun,options);
//        mSV.setBa

    }

    @Override
    public void execute() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCompass(mCompassView, null);
        key_back = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.entrance_activity_btn_bottom_center:
                Intent intent = new Intent();
                startActivity("MainActivity", intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    boolean isTouchInterceptor = false;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        key_back += 1;
//        showPopupWindow(btn_bottom_center);
//
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                popupWindow.dismiss();
//                isTouchInterceptor = true;
//                popupWindow.dismiss();
//                return true;
//            }
//        });
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                if(isTouchInterceptor) {
//                    isTouchInterceptor = false;
//                }else{
//                    MyApplication.getInstance().killActivitys();
//                    finish();
//                }
//            }
//        });
////        if (key_back==2)
////        {
////            MyApplication.getInstance().killActivitys();
////            finish();
////            popupWindow.dismiss();
////
////        }
//        return true;
//    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.exit_alterdialog, null);

       popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));

        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);


    }
}

//
//new Handler().postDelayed(new Runnable() {
//@Override
//public void run() {
//        SharedPreferences sp = getSharedPreferences("Installed", Activity.MODE_APPEND);
//        isInstalledResult = sp.getBoolean("isInstall",false);
//        if(isInstalledResult==null||isInstalledResult==false)
//        {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean("isInstall", true);
//        editor.commit();
//        }
//        if(isInstalledResult) {
////                    finish();
////                   Intent intent = new Intent();
////                   startActivity("entranceActivity",intent);
////                    startActivity("entranceActivity");
//        }else {
//        finish();
//        Intent intent = new Intent();
//        startActivity("StreetCenter",intent);
//        }
//        }
//        },500);
