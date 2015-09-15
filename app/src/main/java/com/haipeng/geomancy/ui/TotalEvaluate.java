package com.haipeng.geomancy.ui;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haipeng.geomancy.MyInterface.DataGetFinish;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.adapter.EvaluteAdapter;
import com.haipeng.geomancy.data.BaseGetData;
import com.haipeng.geomancy.data.HttpPostUri;
import com.haipeng.geomancy.entity.ChoiceQuestionInfo;
import com.haipeng.geomancy.entity.EvaluteInfo;
import com.haipeng.geomancy.util.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TotalEvaluate extends BaseActivity implements View.OnClickListener{

    Button   btn_evaluate;
    TextView tv_home_name,tv_home_sex,tv_home_bir_china,tv_home_bir_world,tv_home_bir_region
            ,tv_home_bir_sun,tv_home_towards;
    TextView tv_home_name_title,tv_home_sex_title,tv_home_bir_china_title,
            tv_home_bir_world_title,tv_home_bir_region_title
            ,tv_home_bir_sun_title,tv_home_towards_title,tv_total_evaluate_title;
    TextView tv_hometowards,tv_homeproperty,tv_homeownerproperty,tv_homelife;
    String   homeOwnerId;
    String  jsonString;

    String init_home_towads ;
    String home_towards ;
    String home_name    ;
    String home_sex     ;
    String bir_world    ;
    String bir_china    ;
    String bir_region  ;
    String bir_sun    ;
    String hometowards;
    String homeproperty;
    String homeownerproperty;
    String homelife;

    Typeface ttf_lishu,ttf_song;
    EvaluteAdapter evaluteAdapter;
    LinearLayout mLisView;

    boolean isHasExecuteCreate = false;
    @Override
    public void initView() {
        setContentView(R.layout.activity_total_evaluate);
        SharedPreferences sp = getSharedPreferences("homeOwnerInfo", Application.MODE_APPEND);
        homeOwnerId = sp.getString("homeOwnerId","");

        tv_home_name_title = (TextView) findViewById(R.id.tv_home_name_title);
        tv_home_sex_title = (TextView) findViewById(R.id.tv_home_sex_title);
        tv_home_bir_china_title = (TextView) findViewById(R.id.tv_home_bir_china_title);
        tv_home_bir_world_title = (TextView) findViewById(R.id.tv_home_bir_world_title);
        tv_home_bir_region_title = (TextView) findViewById(R.id.tv_home_bir_region_title);
        tv_home_bir_sun_title = (TextView) findViewById(R.id.tv_home_bir_sun_title);
        tv_home_towards_title = (TextView) findViewById(R.id.tv_home_towards_title);
        tv_total_evaluate_title = (TextView) findViewById(R.id.tv_total_evaluate_title);

        tv_home_name = (TextView) findViewById(R.id.tv_home_name);
        tv_home_sex = (TextView) findViewById(R.id.tv_home_sex);
        tv_home_bir_china = (TextView) findViewById(R.id.tv_home_bir_china);
        tv_home_bir_world = (TextView) findViewById(R.id.tv_home_bir_world);
        tv_home_bir_region = (TextView) findViewById(R.id.tv_home_bir_region);
        tv_home_bir_sun = (TextView) findViewById(R.id.tv_home_bir_sun);
        btn_evaluate = (Button) findViewById(R.id.total_evaluate_btn);

        mLisView = (LinearLayout) findViewById(R.id.total_evaluate_listView);

        tv_hometowards = (TextView) findViewById(R.id.tv_hometowards);
        tv_homeproperty = (TextView) findViewById(R.id.tv_homeproperty);
        tv_homeownerproperty = (TextView) findViewById(R.id.tv_homeownerproperty);
        tv_homelife = (TextView) findViewById(R.id.tv_homelife);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(MyListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    @Override
    public void setUpView() {
        btn_evaluate.setOnClickListener(this);
        jsonString = getIntent().getStringExtra("json");
        ttf_lishu    = Typeface.createFromAsset(getAssets(),"fonts/lishu.ttf");

//
//        tv_home_name.setTypeface(ttf_lishu);
//        tv_home_sex .setTypeface(ttf_lishu);
//        tv_home_bir_china .setTypeface(ttf_lishu);
//        tv_home_bir_world.setTypeface(ttf_lishu);
//        tv_home_bir_region.setTypeface(ttf_lishu);
//        tv_home_bir_sun .setTypeface(ttf_lishu);
//        tv_hometowards.setTypeface(ttf_lishu);
//        tv_homeproperty.setTypeface(ttf_lishu);
//        tv_homeownerproperty.setTypeface(ttf_lishu);
//        tv_homelife.setTypeface(ttf_lishu);
    }

    @Override
    public void execute() {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            List<EvaluteInfo> list= tidyChooseAnswers(jsonObject);
            LayoutInflater layoutInflater = this.getLayoutInflater().from(this);
            for(int i=0;i<list.size();i++) {
                if(list.get(i).getEvalute().equals(""))
                    continue;
                LinearLayout ll = (LinearLayout) layoutInflater.inflate(R.layout.item_totalevalute, null);
                TextView item_evalute_title = (TextView) ll.findViewById(R.id.item_evalute_title);
                TextView item_evalute_answer = (TextView) ll.findViewById(R.id.item_evalute_answer);
                TextView item_evalute_evalute = (TextView) ll.findViewById(R.id.item_evalute_evalute);
                item_evalute_title.setText(list.get(i).getTitle());
                item_evalute_title.setTypeface(ttf_lishu);
                item_evalute_answer.setText(list.get(i).getAnswer());
                item_evalute_answer.setTypeface(ttf_lishu);
                item_evalute_evalute.setText(list.get(i).getEvalute()+"\n");
                item_evalute_evalute.setTypeface(ttf_lishu);
                mLisView.addView(ll);
            }
//              evaluteAdapter = new EvaluteAdapter(this,list);
//              mLisView.setAdapter(evaluteAdapter);
//              setListViewHeightBasedOnChildren(mLisView);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("homeOwnerId",homeOwnerId);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        BaseGetData bgd = new BaseGetData(new DataGetFinish() {
            @Override
            public void dataGetFinish(JSONObject jsonObject) {
                Log.i("baseGet","dataGetFinish");
//                Log.i("baseGet",jsonObject.toString());
                if(jsonObject == null)
                {
                    Log.i("baseGet","jsonObject is null");
                    return;
                }
                try {
                    String result = jsonObject.getString("result");
                    Log.i("json",result);
                    if(result.equals("0"))
                    {
                        Toast.makeText(TotalEvaluate.this,"homeOwnerId error",Toast.LENGTH_SHORT).show();
                    }else{
                        String homeOwnerId  = jsonObject.getString("homeOwnerId");
                        Log.i("json homOwnerId",homeOwnerId);
                        home_name    = jsonObject.getString("name");
                        home_sex     = jsonObject.getString("sex");
                        bir_world    =  jsonObject.getString("birthday");
                        bir_china    = jsonObject.getString("birthdayL");
                        bir_region   = jsonObject.getString("province");
                        bir_sun      = jsonObject.getString("birsun");
                        hometowards  = jsonObject.getString("homewards");
                        homeproperty = jsonObject.getString("homeproperty");
                        homeownerproperty= jsonObject.getString("homeownerproperty");
                        homelife = jsonObject.getString("homelife");
                        Log.i("jsonObject",jsonObject.toString());
                        Log.i("home_name",home_name);
                                tv_home_name.setText(home_name);
                                tv_home_sex.setText(home_sex);
                                tv_home_bir_china.setText(bir_china);
                                tv_home_bir_world.setText(bir_world);
                                tv_home_bir_region.setText(bir_region);
                                tv_home_bir_sun.setText(bir_sun);
                                tv_hometowards.setText(hometowards);
                                tv_homeproperty.setText(homeproperty);
                                tv_homeownerproperty.setText(homeownerproperty);
                                tv_homelife.setText(homelife);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
             }

            @Override
            public void IOException() {
                Log.i("baseGet","dataGetFinishIOException");
                isLianjieWangluo();
            }
        },HttpPostUri.total_evaluate,jsonObject.toString());

    }
    //根据选择的选项找出答案，评价，和标题
    public List<EvaluteInfo> tidyChooseAnswers(JSONObject jsonObject){
        try {
            //choose每一道题的选项和评价
            String choose_one   = jsonObject.getString("1");
            String choose_two   = jsonObject.getString("2");
            String choose_three = jsonObject.getString("3");
            String choose_four  = jsonObject.getString("4");
            String choose_five  = jsonObject.getString("5");
            String choose_six   = jsonObject.getString("6");
            String choose_seven = jsonObject.getString("7");
            String choose_eight = jsonObject.getString("8");
            String choose_nine  = jsonObject.getString("9");
            String one="",two="",three="",four="",five="",six="",seven="",eight="",nine="";
            List<ChoiceQuestionInfo> list = getTtoN();
            List<EvaluteInfo>        evaluteInfoList= new ArrayList<EvaluteInfo>();
            //标题，答案，评价
            EvaluteInfo evaluteInfo = new EvaluteInfo();
            String title = "站在您手机头朝向的一面窗户、阳台或门口向远处眺望，视野可达的最远范围为 （只要没有建筑物、山体、遮挡住您的视线都算视野可达的范围）";
            one ="问题:"+title+"\n";
            evaluteInfo.setTitle(title);
            if(choose_one.substring(0,1).equals("a"))
            {
                String answer ="10米以内";
                one+="答案:"+answer+"\n";
                evaluteInfo.setAnswer(answer);
            }else if(choose_one.substring(0,1).equals("b"))
            {
                String answer ="10米到100米左右";
                one+="答案:"+answer+"\n";
                evaluteInfo.setAnswer(answer);
            }else if(choose_one.substring(0,1).equals("c"))
            {
                String answer ="100米以上,但并非四周旷野（并非左面、右面、前面都是旷野）";
                one+="答案:"+answer+"\n";
                evaluteInfo.setAnswer(answer);
            }
            one +="评价:"+choose_one.substring(choose_one.indexOf("|")+1)+"\n";
            String str = choose_one.trim().substring(choose_one.indexOf("|")+1).trim();
//            if(str.equals("")||str.equals("null")||str == null)
//            {
//
//            }else{
                evaluteInfo.setEvalute(str);
                evaluteInfoList.add(evaluteInfo);
//            }
//            two = getDesci(list,choose_two,0,);
//            three = getDesci(list,choose_three,1);
//            four = getDesci(list,choose_four,2);
//            five = getDesci(list,choose_five,3);
//            six = getDesci(list,choose_six,4);
//            seven = getDesci(list,choose_seven,5);
//            eight = getDesci(list,choose_eight,6);
//            nine = getDesci(list,choose_nine,7);

            // /choose把每一道题的选择和评价
            for(int i=0;i<8;i++)
              {
                  String choose="";
                  if(i==0)
                  {
                      choose = choose_two;
                  }else if(i==1)
                  {
                      choose = choose_three;
                  }else if(i==2)
                  {
                      choose = choose_four;
                  }else if(i==3)
                  {
                      choose = choose_five;
                  }else if(i==4)
                  {
                      choose = choose_six;
                  }else if(i==5)
                  {
                      choose = choose_seven;
                  }else if(i==6)
                  {
                      choose = choose_eight;
                  }else if(i==7)
                  {
                      choose = choose_nine;
                  }
                  getDesci(list,choose,i,evaluteInfoList);
              }

            return evaluteInfoList;
        }catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    //list是问题的题目，根据choose的选项选择，选出答案，根据choose截取评价
    public List<EvaluteInfo> getDesci(List<ChoiceQuestionInfo> list,String choose,int n,List<EvaluteInfo> evaluteInfoList){
        EvaluteInfo evaluteInfo = new EvaluteInfo();
        String num="";
        num +="问题:"+ list.get(n).getTitle()+"\n";
        evaluteInfo.setTitle(list.get(n).getTitle());
        if(choose.trim().substring(0,1).equals("1"))
        {
            num+="答案:"+list.get(n).getChoice_first()+"\n";
            evaluteInfo.setAnswer(list.get(n).getChoice_first());
        }else{
            num+="答案:"+list.get(n).getChoice_second()+"\n";
            evaluteInfo.setAnswer(list.get(n).getChoice_second());
        }
        num+="评价:"+choose.substring(choose.indexOf("|")+1)+"\n";
        evaluteInfo.setEvalute(choose.substring(choose.indexOf("|")+1));
        String str = choose.trim().substring(choose.indexOf("|")+1);
        if(str.equals("")||str==null||str.equals("null"))
        {
            num = "";
        }else{
            evaluteInfoList.add(evaluteInfo);
        }

        return evaluteInfoList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isHasExecuteCreate = true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        EntranceActivity.isNeedToPay = true;
        if(isHasExecuteCreate)
        {
           isHasExecuteCreate = false;
        }else {
            initView();
            setUpView();
            execute();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.total_evaluate_btn:

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("homeOwnerId", homeOwnerId);
                    BaseGetData bgd = new BaseGetData(new DataGetFinish() {
                        @Override
                        public void dataGetFinish(JSONObject jsonObject) {
                            if(jsonObject == null)
                            {
                                return;
                            }
                            Intent intent = new Intent();
                            intent.putExtra("json",jsonObject.toString());
                            startActivity("CameraContentActivity", intent);
                        }

                        @Override
                        public void IOException() {
                            isLianjieWangluo();
                        }
                    }, HttpPostUri.eight_towards, jsonObject.toString());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_total_evaluate, menu);
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

    public List<ChoiceQuestionInfo> getTtoN(){
        List<ChoiceQuestionInfo> list = new ArrayList<ChoiceQuestionInfo>();
        String[] choiceQuestionTitles = getResources().getStringArray(R.array.choice_question);
        for (int i = 0; i < choiceQuestionTitles.length; i++) {
            ChoiceQuestionInfo choiceQuestionInfo = new ChoiceQuestionInfo();
            choiceQuestionInfo.setTitle(choiceQuestionTitles[i].substring(choiceQuestionTitles[i].indexOf(".")+1));
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

    return list;
    }
    public void setHaveNotHave(ChoiceQuestionInfo choiceQuestionInfo) {
        choiceQuestionInfo.setChoice_first("有");
        choiceQuestionInfo.setChoice_second("无");
    }

    public void setYesNot(ChoiceQuestionInfo choiceQuestionInfo) {
        choiceQuestionInfo.setChoice_first("是");
        choiceQuestionInfo.setChoice_second("否");
    }

}
