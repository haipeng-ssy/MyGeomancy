package com.haipeng.geomancy.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haipeng.geomancy.MyInterface.EnsureCancel;
import com.haipeng.geomancy.MyInterface.HasDgressChange;
import com.haipeng.geomancy.R;
import com.haipeng.geomancy.UserData.UserDataSharedPreferences;
import com.haipeng.geomancy.data.MyConstants;
import com.haipeng.geomancy.data.MyStaticData;
import com.haipeng.geomancy.entity.EightTowards;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sunyiyan on 2015/1/25.
 */
public class CameraContentActivity extends BaseActivity implements SurfaceHolder.Callback,View.OnClickListener,HasDgressChange{

    SurfaceView mSurfaceView;
    Camera mCamera;
    SurfaceHolder mSurfaceHolder;
    Camera.Parameters parameters;
    int carmeraId;

    TextView mTextView;
    TextView gpsTV;

    private static final String TAG="sensor";
    ImageView camera_iv;
    Resources res ;
    Bitmap img;
    Matrix matrix ;
    Bitmap img_roate;
    int width;
    int height;

    static float a = -1f;
    //GPS

    LinearLayout ll_operate;
    TextView tv_sv_operate_one,tv_sv_operate_two,tv_sv_operate_three;
    ImageView iv_sv_operate_one,iv_sv_operate_two,iv_sv_operate_three;
    LinearLayout ll_analyse,ll_sui,ll_nosuit,ll_avoid,ll_yearmonth;
    TextView  tv_analyse,tv_sui,tv_nosuit,tv_avoid,tv_yearmonth;

    TextView tv_null;
    Button btn_operate;

    TextView tv_profile_direction;
    TextView tv_profile_profile;
    TextView tv_nature;
    ImageView iv_profile_image;
    ScrollView sv_intro;

    LocationManager locationManager;
    Location mLocation;
    double longitude,latitude,altitude;
    String jsonString;
    Map<String,EightTowards> map_et;
    Typeface ttf_lishu;
    LinearLayout scroll_one_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttf_lishu = Typeface.createFromAsset(getAssets(),"fonts/lishu.ttf");
        scroll_one_ll = (LinearLayout) findViewById(R.id.camera_content_scroll_one_ll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        execute();
        scroll_one_ll.setOnClickListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_camera_content);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_content_surfaceView);
        res = this.getApplicationContext().getResources();
        img= BitmapFactory.decodeResource(res, R.drawable.icon_compass);
        matrix = new Matrix();
        width = img.getWidth();
        height = img.getHeight();
        ll_operate = (LinearLayout) findViewById(R.id.camera_content_operate_ll);
        tv_sv_operate_one = (TextView) findViewById(R.id.camera_content_operate_tv_one);
        iv_sv_operate_one = (ImageView) findViewById(R.id.camera_content_operate_iv_one);
        tv_sv_operate_two = (TextView) findViewById(R.id.camera_content_operate_tv_two);
        iv_sv_operate_two = (ImageView) findViewById(R.id.camera_content_operate_iv_two);
        tv_sv_operate_three = (TextView) findViewById(R.id.camera_content_operate_tv_three);
        iv_sv_operate_three = (ImageView) findViewById(R.id.camera_content_operate_iv_three);
        btn_operate = (Button) findViewById(R.id.camera_content_operate_btn);
        //介绍
        sv_intro  = (ScrollView) findViewById(R.id.camera_content_sv_intro);
//        sv_intro  = (SurfaceView) findViewById(R.id.camera_content_sv_intro);
        //方位
        tv_profile_direction  =   (TextView)findViewById(R.id.camera_content_tv_profile_direction_value);
        tv_profile_profile    =   (TextView)findViewById(R.id.camera_content_tv_profile_profile_value);
        tv_nature       =   (TextView) findViewById(R.id.tv_nature);
        iv_profile_image      =   (ImageView)findViewById(R.id.camera_content_profile_image);
        ll_sui = (LinearLayout) findViewById(R.id.ll_sui);
        ll_nosuit = (LinearLayout) findViewById(R.id.ll_nosuit);
        ll_avoid = (LinearLayout) findViewById(R.id.ll_avoid);
        ll_yearmonth = (LinearLayout) findViewById(R.id.ll_yearmonth);
        tv_analyse = (TextView) findViewById(R.id.tv_analyse);
        tv_sui = (TextView) findViewById(R.id.tv_sui);
        tv_nosuit = (TextView) findViewById(R.id.tv_nosuit);
        tv_avoid = (TextView) findViewById(R.id.tv_avoid);
        tv_yearmonth = (TextView) findViewById(R.id.tv_yearmonth);
        tv_null  = (TextView) findViewById(R.id.tv_null);

        MyStaticData.isGHJTCY = false;
    }
    @Override
    public void setUpView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        btn_operate.setOnClickListener(this);
        tv_sv_operate_one.setText(getResources().getString(R.string.camera_operate_one));
        tv_sv_operate_two.setText(getResources().getString(R.string.camera_operate_two));
        tv_sv_operate_three.setText(getResources().getString(R.string.camera_operate_three));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap_one  = BitmapFactory.decodeResource(getResources(),R.drawable.orientation_one,options);
        Bitmap bitmap_two  = BitmapFactory.decodeResource(getResources(),R.drawable.camera_operate_two);
        Bitmap bitmap_three  = BitmapFactory.decodeResource(getResources(),R.drawable.camera_operate_three);

        iv_sv_operate_one.setImageBitmap(bitmap_one);
        iv_sv_operate_two.setImageBitmap(bitmap_two);
        iv_sv_operate_three.setImageBitmap(bitmap_three);

        jsonString = getIntent().getStringExtra("json");

    }

    @Override
    public void execute() {

        try {
             JSONObject jsonObject= new JSONObject(jsonString);
             String homeOwnerId =   jsonObject.getString("homeOwnerId");
             String result =   jsonObject.getString("result");
             JSONArray jsonArray  = jsonObject.getJSONArray("et");
             map_et = new HashMap<String,EightTowards>();
             for(int i=0;i<jsonArray.length();i++)
             {
                 JSONObject ja_jo = jsonArray.getJSONObject(i);
                 EightTowards et = new EightTowards();
                 et.setNature(ja_jo.getString("nature"));
                 et.setStar(ja_jo.getString("star"));
                 et.setAnalyse(ja_jo.getString("analyse"));
                 et.setSuit(ja_jo.getString("suit"));
                 et.setNosuit(ja_jo.getString("nosuit"));
                 et.setAvoid(ja_jo.getString("avoid"));
                 et.setYearmonth(ja_jo.getString("yearmonth"));

                 map_et.put(ja_jo.getString("palace"),et);
             }

             //记录用户已经获取服务
             UserDataSharedPreferences.insertSPUserInfoByStr(CameraContentActivity.this,
                     UserDataSharedPreferences.SP_HADSERVICED, "true");
         }catch (Exception e)
         {
             e.printStackTrace();
         }
        GetCompass(null, this);
    }

    @Override
    public void hadgressChange(float currentDgress) {
//         Log.i("tag",currentDgress+"");
        float dgress = 0;
        String drection ="坎";
        if(currentDgress<0)
         {
             dgress = Math.abs(currentDgress);
         }
        if(currentDgress>0)
        {
            dgress = 360f - currentDgress;
        }
        if((337.5f<=dgress&&dgress<360.0f)||(0.0f<=dgress&&dgress<22.5f))
        {
           drection  ="坎";
        }else if(22.5f<=dgress&&dgress<67.5f){
            drection  ="艮";
        }else if(67.5f<=dgress&&dgress<112.5f){
            drection  ="震";
        }else if(112.5f<=dgress&&dgress<157.5f){
            drection  ="巽";
        }else if(157.5<=dgress&&dgress<202.5f){
            drection  ="离";
        }else if(202.5f<=dgress&&dgress<247.5f){
            drection  ="坤";
        }else if(247.5f<=dgress&&dgress<292.5f){
            drection  ="兑";
        }else if(292.5f<=dgress&&dgress<337.5f){
            drection  ="乾";
        }
        updateScreen(drection);
    }
    public void updateScreen(String str){
        Set<String> key =map_et.keySet();
        EightTowards et = new EightTowards();

        for (Iterator<String> it = key.iterator();it.hasNext();)
        {
            String s = it.next();
            if(s.equals(str)) {
             et =   map_et.get(s);
            }
        }

        tv_profile_direction.setTypeface(ttf_lishu);
        tv_profile_profile.setTypeface(ttf_lishu);
        tv_nature.setTypeface(ttf_lishu);
        tv_analyse.setTypeface(ttf_lishu);
        tv_sui.setTypeface(ttf_lishu);
        tv_nosuit.setTypeface(ttf_lishu);
        tv_avoid.setTypeface(ttf_lishu);
        tv_yearmonth.setTypeface(ttf_lishu);


        tv_profile_direction.setText(str);
        tv_profile_profile.setText(et.getStar());
        tv_nature.setText(et.getNature()+"\n");
        tv_analyse.setText(et.getAnalyse()+"\n");
        tv_sui.setText(et.getSuit()+"\n");
        tv_nosuit.setText(et.getNosuit()+"\n");
        tv_avoid.setText(et.getAvoid()+"\n");
        tv_yearmonth.setText(et.getYearmonth());

        if(et.getSuit().equals(""))
        {
            ll_sui.setVisibility(View.GONE);
        }else{
            ll_sui.setVisibility(View.VISIBLE);
        }
        if(et.getNosuit().equals(""))
        {
            ll_nosuit.setVisibility(View.GONE);
        }else{
            ll_nosuit.setVisibility(View.VISIBLE);
        }
        if(et.getAvoid().equals(""))
        {
            ll_avoid.setVisibility(View.GONE);
        }else{
            ll_avoid.setVisibility(View.VISIBLE);
        }

        if(et.getYearmonth().equals(""))
        {
            ll_yearmonth.setVisibility(View.GONE);
        }else{
            ll_yearmonth.setVisibility(View.VISIBLE);
        }

        if(et.getSuit().equals("")&&et.getNosuit().equals("")&&et.getAvoid().equals("")&&et.getYearmonth().equals(""))
        {
             tv_null.setVisibility(View.VISIBLE);
             tv_null.setTypeface(ttf_lishu);
             tv_null.setText("无");
        }else{
            tv_null.setVisibility(View.GONE);
        }

    }

    //自定义摄像头
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //取出后摄像头
        int carmeras = mCamera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i=0;i<carmeras;i++)
        {
            mCamera.getCameraInfo(i,cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                carmeraId = i;
            }
        }
        //打开摄像头
        try{
            mCamera = Camera.open(carmeraId);
            parameters = mCamera.getParameters();
            mCamera.setDisplayOrientation(90);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(width,height);

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        for (int i =0;i<sizes.size();i++)
        {
            Camera.Size vSize = sizes.get(i);
        }
        try{
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null) {
            mCamera.release();
            mCamera = null;
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.camera_content_operate_btn:
                    ll_operate.setVisibility(View.INVISIBLE);
                    sv_intro.setVisibility(View.GONE);
                break;
            case R.id.camera_content_scroll_one_ll:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMyAlterDialog(new EnsureCancel() {
                            @Override
                            public void ensure() {
//                                MyConstants.ToRegisterFlag = MyConstants.HomeOwnerToRegister;
                                Intent intent = new Intent();
                                startActivity("EntranceActivity",intent);
                                clearSPHomeOwnerId();
                            }
                            @Override
                            public void cancel() {
                                MyConstants.ToRegisterFlag = MyConstants.HomeOtherToRegister;
                                Intent intent2 = new Intent();
                                startActivity("RegisterActivity", intent2);
                                MyStaticData.isGHJTCY = true;
                            }
                        },"只要不退出、您可以反复环视堪测风水\n(或点击弹出框以外区域可继续)","" +
                                "堪测\n" +
                                "另一套房屋",
                                "更换\n" +
                                "家庭成员");
                    }
                });

                break;
            default:
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera_content, menu);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent();
            startActivity("AppUserEvaluateActivity",intent);
        }
        return true;
    }

}
