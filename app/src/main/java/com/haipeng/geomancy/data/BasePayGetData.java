package com.haipeng.geomancy.data;

import android.os.AsyncTask;
import com.haipeng.geomancy.MyInterface.DataGetFinish;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Sunyiyan on 2015/1/31.
 * 付款
 */
public class BasePayGetData {
    DataGetFinish mdgf;
    String mUri,mJsonParams,mAttationStr;
    JSONObject getJsonData = null;
    public BasePayGetData(DataGetFinish dgf, String uri){
        mdgf = dgf;
        String[] params={uri};
        new GetData().execute(params);
    }
    public class GetData extends AsyncTask<String,Integer,JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet    httpGet   = new HttpGet(params[0]);
            HttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    String getDataStr = EntityUtils.toString(response.getEntity());
                    getJsonData =  new JSONObject(getDataStr);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                getJsonData = null;
//                mdgf.IOException();
            }
            return getJsonData;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            mdgf.dataGetFinish(getJsonData);
        }
    }

}
