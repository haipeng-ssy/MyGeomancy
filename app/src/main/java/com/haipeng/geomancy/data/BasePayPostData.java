package com.haipeng.geomancy.data;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.util.Log;

import com.haipeng.geomancy.MyInterface.DataGetFinish;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunyiyan on 2015/1/31.
 */
public class BasePayPostData {
    DataGetFinish mdgf;
    String mUri, mJsonParams, mAttationStr;
    JSONObject getJsonData = null;
    //查询用户付款信息
    //post方式
    public BasePayPostData(DataGetFinish dgf, String uri, String userId) {
        mdgf = dgf;
        String[] params = {uri, userId};
        new GetData().execute(params);
    }

    public class GetData extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
//            httpPost.addHeader("charset", HTTP.UTF_8);
//            httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
//            httpPost.addHeader("Connection", " Keep-Alive");
//            httpPost.addHeader("Cache-Control", "max-age=0");
            //httpPost.addHeader("Referer", "http://layl.kaydong.com/default.aspx");
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            httpPost.addHeader("Referer", "http://sj.layl.cn/default.aspx");
            httpPost.addHeader("User-Agent", "Mozilla/5.0");
            HttpResponse response;
            try {

//                StringEntity se = new StringEntity(params[1].toString(), HTTP.UTF_8);
//                StringEntity se = new StringEntity("UserId=34", HTTP.UTF_8);

                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("UserId", params[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//                httpPost.setEntity(se);
                response = httpClient.execute(httpPost);
//                httpPost.setParams(HttpParams.);
                Log.d("tag", httpPost.getURI().toString());
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String getDataStr = EntityUtils.toString(response.getEntity());
                    getJsonData = new JSONObject(getDataStr);

                }
            } catch (HttpResponseException e) {
                getJsonData = null;
                e.printStackTrace();
//                mdgf.IOException();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            return getJsonData;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            mdgf.dataGetFinish(getJsonData);
            if(getJsonData!=null)
            Log.i("tag","payMoney = "+getJsonData.toString());
            if(getJsonData==null)
            Log.i("tag","getJsonDat is null");
        }
    }

}
