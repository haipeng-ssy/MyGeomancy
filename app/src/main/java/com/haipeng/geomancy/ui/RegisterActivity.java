package com.haipeng.geomancy.ui;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.BasePayPostData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.data.MyConstants;
import com.haipeng.geomancy.data.MyStaticData;
import com.haipeng.geomancy.lunarDatePicker.LunarDatePicker;
import com.haipeng.geomancy.lunarDatePicker.OnClickLunarDateListener;
import com.haipeng.geomancy.region.HasCompletedCity;
import com.haipeng.geomancy.region.HasCompletedProvince;
import com.haipeng.geomancy.region.RegionsDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, HasCompletedProvince, HasCompletedCity {

    String home_name;
    String home_sex = "男";
    String home_region_province = "北京";
    String home_region_city = "北京市";
    String home_region_county = "东城区";
    String home_birthdy_date = "";
    String home_birthdy_start_time = "";
    String home_birthdy_end_time = "";
    boolean has_xialingshi = false;
    boolean isKnowBirthdayTimeDetail = true;

    EditText et_home_owner_name, et_home_owner_birthday;
    Button btn_sign_up;
    Dialog dialog;
    Dialog dialog_time;

    //地域
    Spinner spinner_province, spinner_city, spinner_county;
    String[] provinces = null;
    String[] citys = null;
    String[] countys = null;
    RegionsDB rdb;

    RadioGroup rg_birthday;
    RadioButton rb_birthday_world, rb_birthday_china;
    RadioGroup rg_common_time;
    RadioGroup rg_sex;

    PopupWindow pw;
    boolean IsBirthdayWorld = true;

    EditText et_birthday_time;
    LinearLayout ll_common_time;
    LinearLayout ll_time_picker;

    //具体时间里显示的夏令时
    TextView tv_xialinshi;
    /**
     * 大概时间里的显示夏令时
     */
    TextView tv_xialinshi_same;
    Spinner spinner_common_time;
    String detailTime = "00:00-00:59早子时";
    boolean isTest = false;
    boolean hasDatePicker = false;
    int odd = 0;
    TextView tv_title;
    boolean onCreate = false;
    boolean notCanClick = false;
    //普通时间
    String commonTimes[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate = true;
        notCanClick = getIntent().getBooleanExtra("notCanClick", false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        panduanIsHaidPaidNotGetServiced();
    }

    //判断是否付款还没有获得服务的
    public void panduanIsHaidPaidNotGetServiced(){
        //判断homeownerID是否存在，即用户是否已经注册
        if (!"".equals(UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_HOMEOWNERID))) {
            String homeOwnerId = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_HOMEOWNERID);
            //检查是否付款
            BasePayPostData bap = new BasePayPostData(RegisterActivity.this,new DataGetFinish() {
                @Override
                public void dataGetFinish(JSONObject jsonObjectInit) {
                    boolean result = false;
                    if (jsonObjectInit == null) {
                        return;
                    }
                    result = GetPayData(jsonObjectInit);

                    //查询用户是否已经获取服务
                    String hasServiced = UserDataSharedPreferences.querySPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_HADSERVICED);
                    if (result) {
                        if ("true".equals(hasServiced)) {
                            notCanClick = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initView();
                                    setUpView();
                                    execute();
                                }
                            });
                        }
                        if ("false".equals(hasServiced)) {
                            notCanClick = true;
                            setUpView();
                            execute();
                        }
                    }
                }
                @Override
                public void IOException() {
                    isLianjieWangluo();
                }
            }, HttpPostUri.get_pay_uri, homeOwnerId);
        }else{
            if(notCanClick)
            {
                notCanClick = false;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        initView();
                        setUpView();
                        execute();
                    }
                });
            }
        }
    }

    @Override
    public void initView() {
        System.out.println("initView");
        setContentView(R.layout.activity_rigester);
        tv_title = (TextView) findViewById(R.id.tv_activity_rigester_title);
        btn_sign_up = (Button) findViewById(R.id.rigister_sign_up);
        et_home_owner_name = (EditText) findViewById(R.id.rigister_homer_owner_name);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        et_home_owner_birthday = (EditText) findViewById(R.id.rigister_homer_owner_birthday);
        spinner_province = (Spinner) findViewById(R.id.spinner_province);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_county = (Spinner) findViewById(R.id.spinner_county);
        rg_birthday = (RadioGroup) findViewById(R.id.birthday_Date);
        rb_birthday_world = (RadioButton) findViewById(R.id.birthday_Date_World);
        rb_birthday_china = (RadioButton) findViewById(R.id.birthday_Date_China);
        et_birthday_time = (EditText) findViewById(R.id.my_birth_time);
        ll_common_time = (LinearLayout) findViewById(R.id.ll_common_time);
        ll_time_picker = (LinearLayout) findViewById(R.id.ll_time_picker);
        rg_common_time = (RadioGroup) findViewById(R.id.rg_common_time);
        tv_xialinshi = (TextView) findViewById(R.id.tv_xialinshi);
        tv_xialinshi_same = (TextView) findViewById(R.id.tv_xialinshi_same);
        spinner_common_time = (Spinner) findViewById(R.id.spinner_common_time);
    }

    @Override
    public void setUpView() {
        btn_sign_up.setOnClickListener(this);
        rdb = new RegionsDB(this);
        provinces = rdb.queryProvince();
        final ArrayAdapter<String> adapter_provice = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item
                , provinces);
        spinner_province.setAdapter(adapter_provice);
        if (notCanClick) {
            //省，市,县
            String provinceStr = UserDataSharedPreferences.querySPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERPROVICE);
            for (int i = 0; i < provinces.length; i++) {
                if (provinces[i].equals(provinceStr)) {
                    hasSelectProvince(provinceStr);
                    spinner_province.setSelection(i);
                    break;
                }
            }
            spinner_province.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            String cityStr = UserDataSharedPreferences.querySPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERCITY);
            for (int i = 0; i < citys.length; i++) {
                if (citys[i].equals(cityStr)) {
                    hasCompletedSelectCity(cityStr);
                    spinner_city.setSelection(i);
                    break;
                }
            }
            String countryStr = UserDataSharedPreferences.querySPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERCOUNTRY);
            for (int i = 0; i < countys.length; i++) {
                if (countys[i].equals(countryStr)) {
                    spinner_county.setSelection(i);
                    break;
                }
            }

            spinner_province.setEnabled(false);
            spinner_city.setEnabled(false);
            spinner_county.setEnabled(false);

        } else {
            spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    hasSelectProvince(provinces[position]);
                    home_region_province = provinces[position];
                    if (!home_birthdy_date.equals("")) {
                        setIsXialingshi();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hasCompletedSelectCity(citys[position]);
                    home_region_city = citys[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (notCanClick) {
            //用户名，性别，出生日期
            et_home_owner_name.setText(UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERNAME));
            et_home_owner_name.setEnabled(false);
            String sex = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERSEX);
            if (sex.equals("男"))
                commonRadioGroup(R.id.radio_btn_man, R.id.radio_btn_woman, true, false);
            if (sex.equals("女"))
                commonRadioGroup(R.id.radio_btn_man, R.id.radio_btn_woman, false, true);
            String lunar = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERBIRLUAR);
            if (lunar.equals("0"))
                commonRadioGroup(R.id.birthday_Date_World, R.id.birthday_Date_China, true, false);
            if (lunar.equals("1"))
                commonRadioGroup(R.id.birthday_Date_World, R.id.birthday_Date_China, false, true);
            et_home_owner_birthday.setText(UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERBIR));
            et_home_owner_birthday.setEnabled(false);
        } else {
            //弹出农历
            initPopWindow();
            et_home_owner_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        home_name = et_home_owner_name.getText().toString();
                        HideInputManaget(et_home_owner_name);
                    } else {
                        home_name = et_home_owner_name.getText().toString();
                    }
                    return true;
                }
            });

            rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int id = group.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(id);
                    home_sex = radioButton.getText().toString();
                }
            });
            DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    odd++;
                    if (odd % 2 == 0) {
                        if (odd == 100) {
                            odd = 0;
                        }
                    } else {
                        setBirthdayDateAndGetXiaLingshi(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                    }
                    hasDatePicker = true;
                }
            };
            dialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, odsl, 1980, 1, 1);
            et_home_owner_birthday.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (IsBirthdayWorld) {
                        dialog.show();
                    } else {
                        pw.showAtLocation(et_home_owner_birthday, Gravity.CENTER, 0, 0);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void execute() {
        commonTimes = getResources().getStringArray(R.array.common_time);
        final ArrayAdapter<String> adapter_common_time = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item
                , commonTimes);
        spinner_common_time.setAdapter(adapter_common_time);
        panduanCommontime(0);
        spinner_common_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                detailTime = commonTimes[position];
                panduanCommontime(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (notCanClick) {
            //具体时间，是否知道具体时间，是否是夏令时
            //是否知道具体时间，和具体时间
            String isKnowDetail = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERKONWDETAILTIME);
            String viewDetailTime = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERBIRDETAILTIME);
            if (isKnowDetail.equals("0")) {
                ll_time_picker.setVisibility(View.GONE);
                ll_common_time.setVisibility(View.VISIBLE);
                commonRadioGroup(R.id.rb_common_time_no, R.id.rb_common_time_yes, true, false);
                for (int i = 0; i < commonTimes.length; i++) {
                    if (commonTimes[i].equals(viewDetailTime)) {
                        spinner_common_time.setSelection(i);
                        spinner_common_time.setEnabled(false);
                        panduanCommontime(i);
                        break;
                    }
                }
            }
            if (isKnowDetail.equals("1")) {
                ll_time_picker.setVisibility(View.VISIBLE);
                ll_common_time.setVisibility(View.GONE);
                commonRadioGroup(R.id.rb_common_time_no,R.id.rb_common_time_yes,false,true);
                et_birthday_time.setText(viewDetailTime);
                et_birthday_time.setEnabled(false);
                home_birthdy_start_time = et_birthday_time.getText().toString();
                home_birthdy_end_time = et_birthday_time.getText().toString();
            }
            ll_time_picker.setEnabled(false);
            ll_common_time.setEnabled(false);
            spinner_common_time.setEnabled(false);
            //是有夏令时
            String isKnowXia = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_USERXIA);
            if (isKnowXia.equals("0")) {
                tv_xialinshi.setVisibility(View.INVISIBLE);
                tv_xialinshi_same.setVisibility(View.GONE);
            }
            if (isKnowXia.equals("1")) {
                tv_xialinshi.setVisibility(View.VISIBLE);
                tv_xialinshi_same.setVisibility(View.GONE);
            }
        } else {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    et_birthday_time.setText(hourOfDay + "时" + minute + "分");
                    if (isKnowBirthdayTimeDetail) {
                        String et_detailTime = et_birthday_time.getText().toString();
                        detailTime = et_detailTime;
                        home_birthdy_start_time = et_birthday_time.getText().toString();
                        home_birthdy_end_time = et_birthday_time.getText().toString();
                    }
                }
            };
            dialog_time = new TimePickerDialog(this, onTimeSetListener, 0, 0, true);
            rg_birthday.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int radionButtonId = group.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(radionButtonId);
                    if (rb.getText().toString().equals("公历(阳历)")) {
                        IsBirthdayWorld = true;
                    } else {
                        IsBirthdayWorld = false;
                    }
                }
            });
            et_birthday_time.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dialog_time.show();
                    return true;
                }
            });
            rg_common_time.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int id = group.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(id);
                    if (rb.getText().equals("是")) {
                        ll_time_picker.setVisibility(View.VISIBLE);
                        ll_common_time.setVisibility(View.GONE);
                        isKnowBirthdayTimeDetail = true;

                    } else {
                        ll_time_picker.setVisibility(View.GONE);
                        ll_common_time.setVisibility(View.VISIBLE);
                        isKnowBirthdayTimeDetail = false;
                        home_birthdy_start_time = "00:25";
                        home_birthdy_end_time = "00:25";
                    }
                }
            });

        }
        if (MyConstants.ToRegisterFlag.equals(MyConstants.HomeOwnerToRegister)) {
            tv_title.setText("请输入房主信息");
        }

        if (MyConstants.ToRegisterFlag.equals(MyConstants.HomeOtherToRegister)) {
            tv_title.setText("请输入家庭成员信息");
        }
    }
    public void commonRadioGroup(int a, int b, boolean bool_a, boolean bool_b) {
        RadioButton rb_a = (RadioButton) findViewById(a);
        rb_a.setChecked(bool_a);
        RadioButton rb_b = (RadioButton) findViewById(b);
        rb_b.setChecked(bool_b);
        rb_a.setEnabled(false);
        rb_b.setEnabled(false);
    }
    public void setBirthdayDateAndGetXiaLingshi(String str) {
        et_home_owner_birthday.setText(str);
        home_birthdy_date = str;
        setIsXialingshi();
    }
    public String isBoolean(String what) {
        String result = "";
        if (what.equals("isLunar")) {
            if (IsBirthdayWorld) {
                result = "0";
            } else {
                result = "1";
            }
        } else if (what.equals("isDetailTime")) {
            if (isKnowBirthdayTimeDetail) {
                result = "1";
            } else {
                result = "0";
            }
        } else if (what.equals("isDayLightTime")) {
            if (has_xialingshi) {
                result = "1";
            } else {
                result = "0";
            }
        }
        return result;
    }
    public void setIsXialingshi() {
        if (isTest)
            return;
        String region = "";
        if (home_region_province.equals("台湾")) {
            region = "台湾";
        }
        if (home_region_province.equals("香港")) {
            region = "香港";
        } else {
            region = "中国";
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("region", region);
            jo.put("isLunar", isBoolean("isLunar"));
            jo.put("birthday", home_birthdy_date);
            BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
                @Override
                public void dataGetFinish(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        return;
                    }
                    try {
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    has_xialingshi = true;
                                    tv_xialinshi.setVisibility(View.VISIBLE);
                                    tv_xialinshi_same.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    has_xialingshi = false;
                                    tv_xialinshi.setVisibility(View.INVISIBLE);
                                    tv_xialinshi_same.setVisibility(View.GONE);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void IOException() {
                    isLianjieWangluo();
                }
            }, HttpPostUri.home_xialingshi_uri, jo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setBirTime(String startTime, String endTime) {
        home_birthdy_start_time = startTime;
        home_birthdy_end_time = endTime;
    }
    public void panduanCommontime(int position) {
        switch (position) {
            case 0:
                setBirTime("00:00", "00:59");
                break;
            case 1:
                setBirTime("01:00", "02:59");
                break;
            case 2:
                setBirTime("03:00", "05:00");
                break;
            case 3:
                setBirTime("05:00", "06:59");
                break;
            case 4:
                setBirTime("07:00", "08:59");
                break;
            case 5:
                setBirTime("09:00", "10:59");
                break;
            case 6:
                setBirTime("11:00", "12:59");
                break;
            case 7:
                setBirTime("13:00", "14:59");
                break;
            case 8:
                setBirTime("15:00", "16:59");
                break;
            case 9:
                setBirTime("17:00", "18:59");
                break;
            case 10:
                setBirTime("19:00", "20:59");
                break;
            case 11:
                setBirTime("21:00", "22:59");
                break;
            case 12:
                setBirTime("23:00", "23:59");
                break;
        }
    }
    //弹出农历DatePicker
    public void initPopWindow() {
        LunarDatePicker ldp = new LunarDatePicker(this, new OnClickLunarDateListener() {
            @Override
            public void onClickComplete(String luarDate) {
                // TODO Auto-generated method stub
                pw.dismiss();
                setBirthdayDateAndGetXiaLingshi(luarDate);
            }
        });
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = ldp;
        pw = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
    }


    @Override
    public void hasSelectProvince(String province) {
        citys = rdb.queryCity(province);
        final ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item
                , citys);
        spinner_city.setAdapter(adapter_city);
        home_region_city = citys[0];
        hasCompletedSelectCity(home_region_city);
    }

    @Override
    public void hasCompletedSelectCity(String city) {
        countys = rdb.queryCounty(city);
        final ArrayAdapter<String> adapter_county = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item
                , countys);
        spinner_county.setAdapter(adapter_county);
        home_region_county = countys[0];
    }
    public void HideInputManaget(View view) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.toggleSoftInput(0, InputMethodManager);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
    public boolean isNullPanduan(String n, String t) {
        if (n == null || n.equals("")) {
            Toast.makeText(RegisterActivity.this, "您没有填写" + t, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("onNewIntent");
        super.onNewIntent(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigister_sign_up:
                if (notCanClick) {//不能点击
                    if (MyStaticData.isGHJTCY) {
                         executeIsGHJTCY();
                    }else {
                    Intent intent = new Intent();
                    startActivity("ChoiceQuestion", intent);
                    }
                } else {
                    showMyAlterDialog(new EnsureCancel() {
                        @Override
                        public void ensure() {
                            signUp();
                        }
                        @Override
                        public void cancel() {

                        }
                    }, "请您核对所填信息，一经确认无法更改", "确定", "返回检查");
                }
                break;
        }
    }

    public void executeIsGHJTCY(){

        //取出之前保存的选择题的每一个答案，更换家庭成员，不用再次选择外部环境
        Map<String, String> map = UserDataSharedPreferences.getChoiceMap(RegisterActivity.this);

        JSONObject jsonObject = new JSONObject();
        try {
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

            //提交选择题的答案
            BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
                @Override
                public void dataGetFinish(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("json", jsonObject.toString());
                    startActivity("TotalEvaluate", intent);
                }
                @Override
                public void IOException() {
                    isLianjieWangluo();
                }
            }, HttpPostUri.home_chose_question_uri, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void signUp(){

        final JSONObject obj = new JSONObject();
        home_name = et_home_owner_name.getText().toString();

        /**
         * 注册信息相关的空判断
         * */
        if (isNullPanduan(home_name, "姓名")) {
            return;
        }
        if (isNullPanduan(home_birthdy_date, "日期")) {
            return;
        }

        if (isKnowBirthdayTimeDetail&&isNullPanduan(et_birthday_time.getText().toString(), "时间")) {
            return;
        }
        if (isNullPanduan(home_birthdy_start_time, "时间")) {
            return;
        }
        if (isNullPanduan(home_birthdy_end_time, "时间")) {
            return;
        }

        try {
            obj.put("phoneHomeId", getPhoneId());
            obj.put("name", home_name);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERNAME, home_name);
            obj.put("sex", home_sex);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERSEX, home_sex);
            obj.put("province", home_region_province);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERPROVICE, home_region_province);
            obj.put("city", home_region_city);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERCITY, home_region_city);
            obj.put("county", home_region_county);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERCOUNTRY, home_region_county);
            obj.put("isLunar", isBoolean("isLunar"));
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERBIRLUAR, isBoolean("isLunar"));
            obj.put("birthday", home_birthdy_date);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERBIR, home_birthdy_date);
            obj.put("isDetailTime", isBoolean("isDetailTime"));
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERKONWDETAILTIME, isBoolean("isDetailTime"));
            obj.put("isDayLightTime", isBoolean("isDayLightTime"));
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERXIA, isBoolean("isDayLightTime"));
            obj.put("birStartTime", home_birthdy_start_time);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERSTARTTIME, home_birthdy_start_time);
            obj.put("birEndTime", home_birthdy_end_time);

            //保存用户注册的信息
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERENDTIME, home_birthdy_end_time);

            String homeOwnerId = getPhoneId() + System.currentTimeMillis();
            Log.i("homeOwnerId_pinyin", homeOwnerId);
            obj.put("homeOwnerId", homeOwnerId);
            if ("0".equals(isBoolean("isDetailTime"))) {
                UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERBIRDETAILTIME, home_birthdy_start_time);
            }
            if ("1".equals(isBoolean("isDetailTime"))) {
                UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_USERBIRDETAILTIME, detailTime);
            }
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_HOMEOWNERID, homeOwnerId);
            UserDataSharedPreferences.insertSPUserInfoByStr(RegisterActivity.this, UserDataSharedPreferences.SP_HADSERVICED, "false");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //提交注册信息进行注册
        BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
            @Override
            public void dataGetFinish(JSONObject jsonObject) {
                if (jsonObject == null) {
                    return;
                }
                JSONObject getJsonData = jsonObject;

                //保存用户id,即是homeOwnerId
                SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
                SharedPreferences.Editor editor = sp.edit();
                if (sp.getAll().size() == 0) {
                } else {
                    editor.clear();
                }
                String result = "";
                String homeOwnerId = "";
                try {
                    result = getJsonData.getString("result");
                    if (result.equals("0")) {
                        editor.putString("homeOwnerId", "");
                        editor.commit();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        homeOwnerId = getJsonData.getString("homeOwnerId");
                        editor.putString("homeOwnerId", homeOwnerId);
                        editor.commit();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                                String money = UserDataSharedPreferences.getPayMoney(RegisterActivity.this);
                                if(money.equals("0")||money.equals("0.0")||money.equals("0.00")) {
                                    Intent intent = new Intent();
                                    if(MyStaticData.isGHJTCY)
                                    {
                                        executeIsGHJTCY();
                                    }else {
                                        startActivity("ChoiceQuestion", intent);
                                    }
                                }else{
                                    Intent intent = new Intent();
                                    startActivity("PaidActivity", intent);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void IOException() {
                isLianjieWangluo();
            }
        }, HttpPostUri.register_uri, obj.toString());
    }

    //查询该用户是否已经付款，根据homeownerID
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rigester, menu);
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
