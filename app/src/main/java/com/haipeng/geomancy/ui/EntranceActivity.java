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

    int key_back = 0;
    public static boolean isNeedToPay = true;

    Button btn_bottom_center;
    CompassView mCompassView;
    PopupWindow popupWindow;
    ScrollView mSV;

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
        }, "请倾斜屏幕，并360度旋转手机，以矫正指南针", "确定", null);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_entrance);
        mSV = (ScrollView) findViewById(R.id.activity_entrance_sv);
        btn_bottom_center = (Button) findViewById(R.id.entrance_activity_btn_bottom_center);
        mCompassView = (CompassView) findViewById(R.id.entrance_activity_comparessView);
    }

    @Override
    public void setUpView() {
        btn_bottom_center.setOnClickListener(this);
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

}

