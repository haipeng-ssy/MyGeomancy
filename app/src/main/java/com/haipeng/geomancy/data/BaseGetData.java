package com.haipeng.geomancy.data;

import android.os.AsyncTask;
import android.util.Log;

import com.haipeng.geomancy.MyInterface.DataGetFinish;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * Created by Sunyiyan on 2015/1/31.
 * 和两岸易理后台的交互
 */
public class BaseGetData {
    DataGetFinish mdgf;
    String mUri,mJsonParams,mAttationStr;
    JSONObject getJsonData = null;
    public BaseGetData(DataGetFinish dgf,String uri,String jsonParams){
        mdgf = dgf;
        String[] params={uri,jsonParams};
        new GetData().execute(params);
    }
    public class GetData extends AsyncTask<String,Integer,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost   httpPost   = new HttpPost(params[0]);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("charset", HTTP.UTF_8);
            HttpResponse response;
            try {
//                StringEntity se = new StringEntity( params[1].toString());
                if(!"".equals(params[1].toString())) {
                    StringEntity se = new StringEntity(params[1].toString(), HTTP.UTF_8);
                    httpPost.setEntity(se);
                }
                response = httpClient.execute(httpPost);
                Log.d("tag",httpPost.getURI().toString());
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    String getDataStr = EntityUtils.toString(response.getEntity());
                    getJsonData =  new JSONObject(getDataStr);
                }
            } catch (Exception e)
            {
                Log.i("BaseGetData","BaseGetDataException..");
                getJsonData = null;
                e.printStackTrace();
                mdgf.IOException();
            }
            return getJsonData;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mdgf.dataGetFinish(getJsonData);
            if(jsonObject!=null)
            Log.i("onPostExcute",jsonObject.toString());
            super.onPostExecute(jsonObject);

        }
    }

}
