package com.haipeng.geomancy.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.myView.CompassView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Calendar;

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
    ImageView iv_layl_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryNet();
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

        iv_layl_url = (ImageView) findViewById(R.id.entrance_layl_url);
    }

    public void queryNet(){
        config();
    }

    @Override
    public void setUpView() {
        btn_bottom_center.setOnClickListener(this);
    }

    @Override
    public void execute() {
        //获取后台的金额

//        public string GetMoney()
//        {
//            Stream s = System.Web.HttpContext.Current.Request.InputStream;
//            StreamReader sRead = new StreamReader(s);
//            string postContent = sRead.ReadToEnd();
//            sRead.Close();
//
//            JObject result = new JObject();
//            result.Add("paymoney", 0.2);
//            return JsonConvert.SerializeObject(result);
//        }

        //获取后台金额
        BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
            @Override
            public void dataGetFinish(JSONObject jsonObject) {
                    if(jsonObject==null)
                    {
                        System.out.println("后台返回的数据为null");
                        return;
                    }
                String money = null;
                try {
                    money = jsonObject.getString("paymoney");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UserDataSharedPreferences.setPayMoney(EntranceActivity.this,money);
            }
            @Override
            public void IOException() {

            }
        }, HttpPostUri.get_pay_money,"");

        iv_layl_url.setOnClickListener(this);
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
                goNext();
                break;
            case R.id.entrance_layl_url:
                Uri uri = Uri.parse("http://www.layl.cn");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
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

