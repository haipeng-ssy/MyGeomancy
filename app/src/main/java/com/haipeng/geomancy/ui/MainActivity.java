package com.haipeng.geomancy.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.MyInterface.EnterMainActivitySelect;
import com.haipeng.geomancy.MyInterface.HasDgressChange;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.BasePayGetData;
import com.haipeng.geomancy.data.BasePayPostData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.data.MyConstants;
import com.haipeng.geomancy.data.MyStaticData;
import com.haipeng.geomancy.myView.CompassView;
import com.haipeng.geomancy.sensor.Compass;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Sunyiyan on 2015/1/24.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, EnterMainActivitySelect {


    boolean notCanClick = false;
    boolean result = false;

    CompassView compassView;
    Button btn_sv_one, btn_sv_two, btn_sv_three, btn_sv_four;
    ImageView iv_sv_one, iv_sv_two, iv_sv_three, iv_sv_four;
    Bitmap bitmap_home_center;
    ScrollView scrollView_introduce;

    JSONObject jsonObject = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        compassView = (CompassView) findViewById(R.id.main_activity_compassView);
        scrollView_introduce = (ScrollView) findViewById(R.id.main_activity_sv);

        iv_sv_one = (ImageView) findViewById(R.id.main_activity_sv_iv_one);
        iv_sv_two = (ImageView) findViewById(R.id.main_activity_sv_iv_two);
        iv_sv_three = (ImageView) findViewById(R.id.main_activity_sv_iv_three);
        iv_sv_four = (ImageView) findViewById(R.id.main_activity_sv_iv_four);

        btn_sv_one = (Button) findViewById(R.id.main_activity_sv_btn_one);
        btn_sv_two = (Button) findViewById(R.id.main_activity_sv_btn_two);
        btn_sv_three = (Button) findViewById(R.id.main_activity_sv_btn_three);
        btn_sv_four = (Button) findViewById(R.id.main_activity_sv_btn_four);
    }


    @Override
    public void setUpView() {
        Bitmap orientation_one, orientation_two, orientation_three, orientation_four;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bitmap_home_center = BitmapFactory.decodeResource(getResources(), R.drawable.orientation_one, options);
        orientation_one = BitmapFactory.decodeResource(getResources(), R.drawable.orientation_one, options);
        orientation_two = BitmapFactory.decodeResource(getResources(), R.drawable.orientation_two, options);
        orientation_three = BitmapFactory.decodeResource(getResources(), R.drawable.orientation_three, options);
        orientation_four = BitmapFactory.decodeResource(getResources(), R.drawable.orientation_four, options);
        iv_sv_one.setImageBitmap(orientation_one);
        iv_sv_two.setImageBitmap(orientation_two);
        iv_sv_three.setImageBitmap(orientation_three);
        iv_sv_four.setImageBitmap(orientation_four);
        btn_sv_one.setOnClickListener(this);
        btn_sv_two.setOnClickListener(this);
        btn_sv_three.setOnClickListener(this);
        btn_sv_four.setOnClickListener(this);
    }

    @Override
    public void execute() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        result = false;
        String hasServiced = UserDataSharedPreferences.querySPUserInfoByStr(this,UserDataSharedPreferences.SP_HADSERVICED);
        if(!"".equals(hasServiced)&&"false".equals(hasServiced))
        {

        }
        else
        {
            clearSPHomeOwnerId();
            UserDataSharedPreferences.clearAllSPUserInfo(MainActivity.this);
        }
        GetCompass(compassView, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void startCameraContentActivity() {
        Intent intent = new Intent();
        intent.setClass(this, CameraContentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_sv_btn_one:
                send("1");
                break;
            case R.id.main_activity_sv_btn_two:
                send("2");
                break;
            case R.id.main_activity_sv_btn_three:
                send("3");
                break;
            case R.id.main_activity_sv_btn_four:
                send("4");
                break;
            default:
                break;
        }
    }

    public void send(String number) {

        try {
                jsonObject.put("phoneHomeId", getPhoneId() + "");
                jsonObject.put("homeChoose", number);
                jsonObject.put("point", point);
                String jsonStr = jsonObject.toString();

            BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
                    @Override
                    public void dataGetFinish(JSONObject jsonObject) {
                        if(jsonObject == null)
                        {
                            return;
                        }else {
                            sendSuccess(jsonObject);
                            panduanIsHaidPaidNotGetServiced();
                        }
                    }
                    @Override
                    public void IOException() {
                                isLianjieWangluo();

                    }
                }, HttpPostUri.home_chose_uri, jsonStr);

        } catch (Exception e) {
                e.printStackTrace();
        }

    }

    //判断是否付款还没有获得服务的
    public void panduanIsHaidPaidNotGetServiced(){
        //判断homeownerID是否存在，即用户是否已经注册
        if (!"".equals(UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_HOMEOWNERID))) {
            String homeOwnerId = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_HOMEOWNERID);

            //检查是否付款
            BasePayPostData bap = new BasePayPostData(MainActivity.this,new DataGetFinish() {
                @Override
                public void dataGetFinish(JSONObject jsonObjectInit) {
                    boolean result = false;
                    if (jsonObjectInit == null) {
                        startRegisterActivity(false);
                        return;
                    }
                    result = GetPayData(jsonObjectInit);
                    String hasServiced = UserDataSharedPreferences.querySPUserInfoByStr(MainActivity.this, UserDataSharedPreferences.SP_HADSERVICED);
                    if (result) {
                        if ("true".equals(hasServiced)) {
                        }
                        if ("false".equals(hasServiced)) {
                            notCanClick = true;
                        }
                    }
                    startRegisterActivity(notCanClick);
                }

                @Override
                public void IOException() {
                    isLianjieWangluo();
                }
            }, HttpPostUri.get_pay_uri, homeOwnerId);

        }else{ //用户homeownerID为空/未注册/用户不存在
            startRegisterActivity(false);
        }
    }

    public void startRegisterActivity(boolean result){
        Intent intent = new Intent();
        intent.putExtra("notCanClick",result);
        startActivity("RegisterActivity", intent);
    }
    //坐向选择，并发送成功
    public void sendSuccess(JSONObject jsonObject) {
        JSONObject jo = jsonObject;
        String result = "0";
        String phoneHomeId = "";
        String point;
        try {
            result = jo.getString("result");// 1代表发送成功
            phoneHomeId = jo.getString("phoneHomeId");
            point = jo.getString("point");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //查询该用户是否已经付款，根据homeownerID
    public boolean GetPayData(JSONObject jsonObjectInit){
        boolean result = false;
        try {
            String state = jsonObjectInit.getString("state");
            if (state.equals("0")) {
                result = false;
            } else if (state.equals("1")) {
                JSONObject jsonObject = jsonObjectInit.getJSONObject("model");
                String _adddate = jsonObject.getString("_adddate");
                String _id = jsonObject.getString("_id");
                String _paymoney = jsonObject.getString("_paymoney");
                String _paytitle = jsonObject.getString("_paytitle");
                //交易号（支付宝交易，如为空则没有支付成功）
                String _tradeno = jsonObject.getString("_tradeno");
                //订单号
                String _orderNo = jsonObject.getString("_orderNo");
                //用户ID
                String _userid = jsonObject.getString("_userid");
                if (_tradeno == null || _tradeno.equals("")|| _tradeno.equals("null")) {
                    result = false;
                } else {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void IsSure(String mStr) {
    }
}
