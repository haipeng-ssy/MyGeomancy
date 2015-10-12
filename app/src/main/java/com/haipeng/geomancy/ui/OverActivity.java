package com.haipeng.geomancy.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haipeng.geomancy.R;
import com.haipeng.geomancy.data.MyConstants;
import com.haipeng.geomancy.data.MyStaticData;
import com.haipeng.geomancy.util.TextUtil;

/**
 * 最后结束界面
 * */
public class OverActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_layl_href;
    TextView tv_one,tv_two,tv_three,tv_five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_over);
        iv_layl_href  = (ImageView) findViewById(R.id.iv_layl_href);
        tv_one = (TextView) findViewById(R.id.user_evaluate_tv_one);
        tv_two = (TextView) findViewById(R.id.user_evaluate_tv_two);
        tv_three = (TextView) findViewById(R.id.user_evaluate_tv_three);
        tv_five = (TextView) findViewById(R.id.user_evaluate_tv_five);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/lishu.ttf");
        tv_one.setTypeface(typeface);
        tv_two.setTypeface(typeface);
        tv_three.setTypeface(typeface);
        tv_five.setTypeface(typeface);
    }

    @Override
    public void setUpView() {
        findViewById(R.id.survey_another_home).setOnClickListener(this);
        findViewById(R.id.change_another_home_owner).setOnClickListener(this);
        String txt_one = getResources().getString(R.string.txt_user_evaluate_tv_one);
        String txt_two = getResources().getString(R.string.txt_user_evaluate_tv_two);
        String txt_three = getResources().getString(R.string.txt_user_evaluate_tv_three);
        String txt_five = getResources().getString(R.string.txt_user_evaluate_tv_five);
        txt_one = TextUtil.ToDBC(TextUtil.stringFilter(txt_one));
        txt_two = TextUtil.ToDBC(TextUtil.stringFilter(txt_two));
        txt_three = TextUtil.ToDBC(TextUtil.stringFilter(txt_three));
        txt_five = TextUtil.ToDBC(TextUtil.stringFilter(txt_five));
        tv_one.setText(txt_one);
        tv_two.setText(txt_two);
        tv_three.setText(txt_three);
        tv_five.setText(txt_five);
    }

    @Override
    public void execute() {
        iv_layl_href.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.layl.cn");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.survey_another_home:
               MyConstants.ToRegisterFlag = MyConstants.HomeOwnerToRegister;
                Intent intent = new Intent();
                startActivity("EntranceActivity", intent);
                clearSPHomeOwnerId();
              break;
            case R.id.change_another_home_owner:
                MyConstants.ToRegisterFlag = MyConstants.HomeOtherToRegister;
                Intent intent2 = new Intent();
                startActivity("RegisterActivity", intent2);
                MyStaticData.isGHJTCY = true;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_over, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
