package com.haipeng.geomancy.ui;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.BasePayGetData;
import com.haipeng.geomancy.data.BasePayPostData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.data.MyStaticData;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PaidActivity extends BaseActivity implements View.OnClickListener {

    Button btn;
    WebView webView;
    String UserId = "";
    String payTitle = "手机测风水";
    String payMoney = "0.02";
    String pay_uri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_paid);
        SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
        UserId = sp.getString("homeOwnerId", "");
        btn = (Button) findViewById(R.id.paid_activity_btn);
        webView = (WebView) findViewById(R.id.paid_activity_webview);
        payMoney = UserDataSharedPreferences.getPayMoney(PaidActivity.this);

        pay_uri = HttpPostUri.pay_uri + "UserID=" + UserId + "&payTitle=" + payTitle + "&payMoney=" + payMoney;
    }

    @Override
    public void setUpView() {
        btn.setOnClickListener(this);
    }

    @Override
    public void execute() {

        webView.addJavascriptInterface(this,"android");
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("GBK");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webView.loadUrl(pay_uri);
        //进入付款界面
        BasePayGetData bd = new BasePayGetData(new DataGetFinish() {
                            @Override
                            public void dataGetFinish(JSONObject jsonObject) {

                            }
                            @Override
                            public void IOException() {
                                isLianjieWangluo();
                            }
                        }, pay_uri);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

    }

    @JavascriptInterface
    public void PaidCompeleted(){
        finish();
        isNeedChoiceQuestion();
    }

    @JavascriptInterface
    public void notPaidCompeleted() {
        finish();
        Intent intent = new Intent();
        startActivity("MainActivity", intent);
  }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_paid, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paid_activity_btn:
                //检查是否付款成功
                BasePayPostData bap = new BasePayPostData(PaidActivity.this,new DataGetFinish() {
                    @Override
                    public void dataGetFinish(JSONObject jsonObjectInit) {
                        boolean result = false;
                        if (jsonObjectInit == null) {
                            return;
                        }
                        result = GetPayData(jsonObjectInit);
                        if (result) {
                             isNeedChoiceQuestion();
                        } else {
                            //付款失败
                            showUserChoiceAndExe();
                        }
                    }

                    @Override
                    public void IOException() {
                        isLianjieWangluo();
                    }
                }, HttpPostUri.get_pay_uri, UserId);
                break;

        }

    }
   //判断是否需要进入环境选择,如果是跟换家庭成员就不需要,
    //跟换家庭成员也只需进入注册界面即可
    public void isNeedChoiceQuestion(){
        if(MyStaticData.isGHJTCY)
        {//取出之前保存的选择题的每一个答案
            Map<String,String> map = UserDataSharedPreferences.getChoiceMap(PaidActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                //获取用户ID
                SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
                String homeOwnerId = sp.getString("homeOwnerId", "");
                jsonObject.put("homeOwnerId", homeOwnerId);
                jsonObject.put("1", (map.get("0").toString()));
                jsonObject.put("2", (map.get("1").toString()));
                jsonObject.put("3", (map.get("2").toString()));
                jsonObject.put("4", (map.get("3").toString()));
                jsonObject.put("5", (map.get("4").toString()));
                jsonObject.put("6", (map.get("5").toString()));
                jsonObject.put("7", (map.get("6").toString()));
                jsonObject.put("8", (map.get("7").toString()));
                jsonObject.put("9", (map.get("8").toString()));

                //提交选择题信息
                BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
                    @Override
                    public void dataGetFinish(JSONObject jsonObject) {
                        if(jsonObject == null)
                        {
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("json",jsonObject.toString());
                        startActivity("TotalEvaluate",intent);
                    }
                    @Override
                    public void IOException() {
                        isLianjieWangluo();
                    }
                }, HttpPostUri.home_chose_question_uri, jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Intent intent = new Intent();
            startActivity("ChoiceQuestion", intent);
            EntranceActivity.isNeedToPay = false;
            finish();
        }
    }
    public void showUserChoiceAndExe() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showMyAlterDialog(new EnsureCancel() {
                    @Override
                    //重新付款
                    public void ensure() {
                    }
                    //回到主页
                    @Override
                    public void cancel() {
                    }
                }, "正在处理中请稍等", "确定",null);
            }
        });
    }

    public boolean GetPayData(JSONObject jsonObjectInit) {
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
                if (_tradeno == null || _tradeno.equals("") || _tradeno.equals("null")) {
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
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

    int i = 0;
    long first;
    long second;
    Handler mHandler;
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
                            showMyAlterDialog(new EnsureCancel() {
                                @Override
                                public void ensure() {
                                    finish();
                                }
                                @Override
                                public void cancel() {
                                    i = 0;
                                }
                            },"确认要放弃本次付款吗","确定","取消");
                        }
                    }
                },1000);
                first = System.currentTimeMillis();
                Log.i("first", first + "");
            }
            if(i == 2)
            {
                second = System.currentTimeMillis();
                Log.i("second",second+"");
                if(second - first<1000)
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
                    i = 1;
                    first = System.currentTimeMillis();
                }
            }
        }
        return true;
    }

}
