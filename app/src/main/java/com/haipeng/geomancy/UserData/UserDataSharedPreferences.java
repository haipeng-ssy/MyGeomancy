package com.haipeng.geomancy.UserData;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/7/23.
 */
public class UserDataSharedPreferences {

    public static final String SP_USERNAME = "username";
    public static final String SP_USERSEX = "usersex";
    public static final String SP_USERPROVICE = "userprovince";
    public static final String SP_USERCITY = "usercity";
    public static final String SP_USERCOUNTRY = "usercountry";
    public static final String SP_USERBIR = "userbir";
    public static final String SP_USERBIRLUAR = "userbirluar";
    public static final String SP_USERXIA = "userxia";
    public static final String SP_USERKONWDETAILTIME = "userkonwdetailtime";
    public static final String SP_USERSTARTTIME = "userstarttime";
    public static final String SP_USERENDTIME = "userendtime";
    public static final String SP_HADPAID = "userhadpaid";
    public static final String SP_HADSERVICED = "userhadserviced";
    public static final String SP_HOMEOWNERID = "userhomeownerid";
    public static final String SP_USERBIRDETAILTIME   = "userdetailtime";

    public static void setChoiceMap(Context context,Map<String,String> map){
        SharedPreferences sp = context.getSharedPreferences("UserQuestions", Application.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("0",map.get("0").toString());
        editor.putString("1",map.get("1").toString());
        editor.putString("2",map.get("2").toString());
        editor.putString("3",map.get("3").toString());
        editor.putString("4",map.get("4").toString());
        editor.putString("5",map.get("5").toString());
        editor.putString("6",map.get("6").toString());
        editor.putString("7",map.get("7").toString());
        editor.putString("8",map.get("8").toString());
        editor.commit();
    }

    public static Map<String,String> getChoiceMap(Context context){
        SharedPreferences sp = context.getSharedPreferences("UserQuestions", Application.MODE_APPEND);
        Map<String,String> map = new HashMap<String,String>();
        map.put("0",sp.getString("0","0"));
        map.put("1",sp.getString("1","0"));
        map.put("2",sp.getString("2","0"));
        map.put("3",sp.getString("3","0"));
        map.put("4",sp.getString("4","0"));
        map.put("5",sp.getString("5","0"));
        map.put("6",sp.getString("6","0"));
        map.put("7",sp.getString("7","0"));
        map.put("8",sp.getString("8","0"));
        return map;
    }

    public static void clearSPUserInfoByStr(Context context,String userColumn) {
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Application.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(userColumn);
        editor.commit();
    }

    public static void insertSPUserInfoByStr(Context context,String userColumn,String userValue) {
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Application.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userColumn,userValue);
        editor.commit();
    }

    public static String querySPUserInfoByStr(Context context,String userColumn) {
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Application.MODE_APPEND);
        String value = sp.getString(userColumn,"");
        return value;
    }
    public static void clearAllSPUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("UserInfo", Application.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }



}
