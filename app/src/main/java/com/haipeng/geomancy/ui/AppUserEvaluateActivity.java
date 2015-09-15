package com.haipeng.geomancy.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.data.MyConstants;
import com.haipeng.geomancy.myView.CompassView;

import org.json.JSONObject;

public class AppUserEvaluateActivity extends BaseActivity implements View.OnClickListener{
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button btn_evaluate;
    LinearLayout ll_evaluate;
    CompassView mCompassView;
    RadioGroup user_evaluate_rg;
    String     satisfaction;
    String     comment;
    EditText   et_user_comment ;

    @Override
    public void initView() {

        setContentView(R.layout.activity_user_evaluate);
        btn_evaluate = (Button) findViewById(R.id.user_evaluate_activity_btn_submit);
        ll_evaluate  = (LinearLayout) findViewById(R.id.user_evaluate_activity_ll_user_evaluate);
        mCompassView = (CompassView) findViewById(R.id.entrance_activity_comparessView);
        user_evaluate_rg = (RadioGroup) findViewById(R.id.user_evaluate_activity_rg_evaluate);
        et_user_comment  = (EditText) findViewById(R.id.user_evaluate_et_comment);

    }

    @Override
    public void setUpView() {

        btn_evaluate.setOnClickListener(this);
        user_evaluate_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 RadioButton rb = (RadioButton) findViewById(checkedId);
                 satisfaction   = rb.getText().toString();

            }
        });

        et_user_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    HideInputManaget(et_user_comment);
                }
                return true;
            }
        });
//        HideInputManaget(et_user_comment);
        findViewById(R.id.config_hidden).requestFocus();
    }

    @Override
    public void execute() {

    }
    public void HideInputManaget(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.toggleSoftInput(0, InputMethodManager);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCompass(mCompassView,null);
//        Toast.makeText(this, "请倾斜屏幕，并360度旋转手机，以矫正指南针", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.user_evaluate_activity_btn_submit:
                JSONObject jsonObject = new JSONObject();

                try {
                    SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
                    String homeOwnerId = sp.getString("homeOwnerId","");
                    jsonObject.put("homeOwnerId",homeOwnerId);
                    jsonObject.put("satisfaction",satisfaction);
                    jsonObject.put("comment",et_user_comment.getText().toString());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                BaseGetData bgd = new BaseGetData(new DataGetFinish() {
                    @Override
                    public void dataGetFinish(JSONObject jsonObject) {
                          Intent intent = new Intent();
                          startActivity("OverActivity",intent);
                    }

                    @Override
                    public void IOException() {
                        isLianjieWangluo();
                    }
                }, HttpPostUri.user_eva_app,jsonObject.toString());

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_evaluate, menu);
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