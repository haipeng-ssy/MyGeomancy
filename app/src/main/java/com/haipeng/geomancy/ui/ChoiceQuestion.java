package com.haipeng.geomancy.ui;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.adapter.ChoiceAdapter;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.BasePayPostData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.entity.ChoiceQuestionInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChoiceQuestion extends BaseActivity implements View.OnClickListener {

    ListView lv_choice;
    Button btn_choice;
    ChoiceAdapter choiceAdapter;
    Map<String, String> map = new HashMap<String, String>();

    private static int MYT = 2;
    private static int MYTH =3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void hideActionBar() {
        super.hideActionBar();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_choice_question);
        lv_choice = (ListView) findViewById(R.id.choice_question_lv);
        btn_choice = (Button) findViewById(R.id.choice_question_btn);
    }

    @Override
    public void setUpView() {
        btn_choice.setOnClickListener(this);
        List<ChoiceQuestionInfo> list = new ArrayList<ChoiceQuestionInfo>();
        String[] choiceQuestionTitles = getResources().getStringArray(R.array.choice_question);
        for (int i = 0; i < choiceQuestionTitles.length; i++) {
            ChoiceQuestionInfo choiceQuestionInfo = new ChoiceQuestionInfo();
            choiceQuestionInfo.setTitle(choiceQuestionTitles[i]);
            switch (i) {
                case 0:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 1:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 2:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 3:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 4:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 5:
                    setHaveNotHave(choiceQuestionInfo);
                    break;
                case 6:
                    setYesNot(choiceQuestionInfo);
                    break;
                case 7:
                    setYesNot(choiceQuestionInfo);
                    break;

            }
            list.add(choiceQuestionInfo);
        }
        choiceAdapter = new ChoiceAdapter(this, list);
        lv_choice.setAdapter(choiceAdapter);

//        setListViewHight(lv_choice);
    }

    @Override
    public void execute() {

    }

    public void setHaveNotHave(ChoiceQuestionInfo choiceQuestionInfo) {
        choiceQuestionInfo.setChoice_first("有");
        choiceQuestionInfo.setChoice_second("无");
    }

    public void setYesNot(ChoiceQuestionInfo choiceQuestionInfo) {
        choiceQuestionInfo.setChoice_first("是");
        choiceQuestionInfo.setChoice_second("否");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) {
//            map.clear();
            if (choiceAdapter != null) {
                choiceAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setListViewHight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String getMyF(){
        int  temp =MYT+MYTH;
        return String.valueOf(temp);
    }
    public static String getMyH(){
        int  temp = MYTH*2;
        return String.valueOf(temp);
    }

    public String setJosonValue(String str) {
        String mStr = "";
        if (str.equals("a")) {
            mStr = "a";
        } else if (str.equals("b")) {
            mStr = "b";
        } else if (str.equals("c")) {
            mStr = "c";
        } else if (str.equals("有")) {
            mStr = "1";
        } else if (str.equals("是")) {
            mStr = "1";
        } else if (str.equals("无")) {
            mStr = "0";
        } else if (str.equals("否")) {
            mStr = "0";
        }
        return mStr;
    }

    Map<String, String> choiceMap = new HashMap<String, String>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choice_question_btn:
                map = choiceAdapter.getMap();
                JSONObject jsonObject = new JSONObject();
                try {

                    SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
                    String homeOwnerId = sp.getString("homeOwnerId", "");
                    jsonObject.put("homeOwnerId", homeOwnerId);

                    jsonObject.put("1", setJosonValue(map.get("0").toString()));
                    choiceMap.put("0", setJosonValue(map.get("0").toString()));

                    jsonObject.put("2", setJosonValue(map.get("1").toString()));
                    choiceMap.put("1", setJosonValue(map.get("1").toString()));

                    jsonObject.put("3", setJosonValue(map.get("2").toString()));
                    choiceMap.put("2", setJosonValue(map.get("2").toString()));

                    jsonObject.put("4", setJosonValue(map.get("3").toString()));
                    choiceMap.put("3", setJosonValue(map.get("3").toString()));

                    jsonObject.put("5", setJosonValue(map.get("4").toString()));
                    choiceMap.put("4", setJosonValue(map.get("4").toString()));

                    jsonObject.put("6", setJosonValue(map.get("5").toString()));
                    choiceMap.put("5", setJosonValue(map.get("5").toString()));

                    jsonObject.put("7", setJosonValue(map.get("6").toString()));
                    choiceMap.put("6", setJosonValue(map.get("6").toString()));

                    jsonObject.put("8", setJosonValue(map.get("7").toString()));
                    choiceMap.put("7", setJosonValue(map.get("7").toString()));

                    jsonObject.put("9", setJosonValue(map.get("8").toString()));
                    choiceMap.put("8", setJosonValue(map.get("8").toString()));

                    //保存选择题的答案
                    UserDataSharedPreferences.setChoiceMap(ChoiceQuestion.this, choiceMap);

                    BaseGetData baseGetData = new BaseGetData(new DataGetFinish() {
                        @Override
                        public void dataGetFinish(JSONObject jsonObject) {
                            if (jsonObject == null) {
                                return;
                            }
                            testIsHadPaid();
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
                    Toast.makeText(this, "您有选择题未选", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public void testIsHadPaid() {
        String homeOwnerId = UserDataSharedPreferences.querySPUserInfoByStr(this, UserDataSharedPreferences.SP_HOMEOWNERID);
        //检查是否付款
        BasePayPostData bap = new BasePayPostData(ChoiceQuestion.this,new DataGetFinish() {
            @Override
            public void dataGetFinish(JSONObject jsonObjectInit) {
                boolean result = false;
                if (jsonObjectInit == null) {
                    return;
                }
                result = GetPayData(jsonObjectInit);
                String hasServiced = UserDataSharedPreferences.querySPUserInfoByStr(ChoiceQuestion.this, UserDataSharedPreferences.SP_HADSERVICED);
                if (result) {
                    if ("true".equals(hasServiced)) {
                    }
                    if ("false".equals(hasServiced)) {
                    }
                }
            }

            @Override
            public void IOException() {
                isLianjieWangluo();
            }
        }, HttpPostUri.get_pay_uri, homeOwnerId);


}

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choice_question, menu);
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
