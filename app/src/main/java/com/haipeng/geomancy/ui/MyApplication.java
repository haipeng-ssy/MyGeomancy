package com.haipeng.geomancy.ui;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/25.
 */
public class MyApplication extends Application {

    private static MyApplication ma;
    private List<ActionBarActivity> abas = new ArrayList<ActionBarActivity>();
    public MyApplication(){

    }
    public static MyApplication getInstance(){
      if(ma==null)
      {
          ma = new MyApplication();
      }
        return ma;
    }

    public void addActionBarActivity(ActionBarActivity actionBarActivity){
        abas.add(actionBarActivity);
    }
    public void removeActionBarActivity(ActionBarActivity actionBarActivity){
        abas.remove(actionBarActivity);
    }
    public void killActivitys(){
        for(ActionBarActivity aba:abas)
        {
//            if(aba.getClass().equals(EntranceActivity.class))
//                continue;
            aba.finish();
        }
        System.exit(0);

    }
}
