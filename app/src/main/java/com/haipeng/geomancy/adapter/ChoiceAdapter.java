package com.haipeng.geomancy.adapter;

import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haipeng.geomancy.R;
import com.haipeng.geomancy.entity.ChoiceQuestionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/1.
 */
public class ChoiceAdapter extends BaseAdapter {

    ActionBarActivity mActivity;
    List<ChoiceQuestionInfo> mList;
    Map<String,String> map = new HashMap<String,String>();
    public ChoiceAdapter(ActionBarActivity actionBarActivity,List<ChoiceQuestionInfo> list){
         mActivity = actionBarActivity;
         mList = list;

    }

    @Override
    public int getCount() {
        return mList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = mActivity.getLayoutInflater();
        if(position ==0)
        {
            final View view = inflater.from(mActivity).inflate(R.layout.item_choicequestionfirst,null);
            RadioGroup rg= (RadioGroup) view.findViewById(R.id.choice_quesiont_first_rg);
            RadioButton rba = (RadioButton) view.findViewById(R.id.choice_quesiont_first_rba);
            RadioButton rbb = (RadioButton) view.findViewById(R.id.choice_quesiont_first_rbb);
            RadioButton rbc = (RadioButton) view.findViewById(R.id.choice_quesiont_first_rbc);
            String str = map.get(position+"");
            if(str == null)
            {
                rba.setChecked(false);
                rbb.setChecked(false);
                rbc.setChecked(false);
            }else {
                if (str.equals("a")) {
                    rba.setChecked(true);
                    rbb.setChecked(false);
                    rbc.setChecked(false);
                } else if (str.equals("b")) {
                    rba.setChecked(false);
                    rbb.setChecked(true);
                    rbc.setChecked(false);
                } else if (str.equals("c")) {
                    rba.setChecked(false);
                    rbb.setChecked(false);
                    rbc.setChecked(true);
                } else if (str.equals("")) {
                    rba.setChecked(false);
                    rbb.setChecked(false);
                    rbc.setChecked(false);
                }
            }
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                   RadioButton rb = (RadioButton) view.findViewById(checkedId);
                   String strChoice = rb.getText().toString().substring(0,1);
                   map.put(position+"",strChoice);
                }
            });
            return  view;
        }else {
            TextView tv;
            RadioGroup radioGroup;
            RadioButton radioButton_first;
            RadioButton radioButton_second;



            convertView = inflater.from(mActivity).inflate(R.layout.item_choice_adapter, null);
            final View mConvertView = convertView;
            tv = (TextView) convertView.findViewById(R.id.item_choice_question_tv);
            radioGroup = (RadioGroup) convertView.findViewById(R.id.item_choice_question_radiogroup);
            radioButton_first = (RadioButton) convertView.findViewById(R.id.item_choice_question_radiobutton_first);
            radioButton_second = (RadioButton) convertView.findViewById(R.id.item_choice_question_radiobutton_second);
            tv.setText(mList.get(position-1).getTitle());
            radioButton_first.setText(mList.get(position-1).getChoice_first());
            radioButton_second.setText(mList.get(position-1).getChoice_second());

            try {
                String str = map.get(position + "");
                if(str == null)
                {
                    radioButton_first.setChecked(false);
                    radioButton_second.setChecked(false);
                }else {
                    if (str.equals("是") || str.equals("有")) {
                        radioButton_first.setChecked(true);
                        radioButton_second.setChecked(false);

                    }
                    if (str.equals("否") || str.equals("无")) {
                        radioButton_first.setChecked(false);
                        radioButton_second.setChecked(true);

                    }
                    if (str.equals("")) {
                        radioButton_first.setChecked(false);
                        radioButton_second.setChecked(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                radioButton_first.setChecked(false);
                radioButton_second.setChecked(false);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int radioButtonId = group.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) mConvertView.findViewById(radioButtonId);
                    String p = position+"";
                    String str = rb.getText().toString();
                    System.out.println("fds");
                    map.put(p, str);
                    System.out.println("fds");
                    String strfds;
                    System.out.println("s");
                }
            });

            return convertView;
        }
    }
    public Map<String,String> getMap(){
        return  map;
    }

}
