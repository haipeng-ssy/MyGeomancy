package com.haipeng.geomancy.ui;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haipeng.geomancy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sunyiyan on 2015/1/25.
 */
public class GuideActivity extends BaseActivity {

    Timer timer;
    TimerTask timerTask;
    ViewPager viewPager;
    int currentItem = 0;
    int guide_images[] = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };
    List<ImageView> imageViewsList;

    Button guide_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//父类里的这个方法里的内容执行完了，才执行下面的

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        guide_btn = (Button)findViewById(R.id.guide_btn);
    }

    @Override
    public void setUpView() {

        //imageViewsList
        imageViewsList = new ArrayList<ImageView>();
        for (int i = 0; i < guide_images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(guide_images[i]);

            imageViewsList.add(imageView);
        }

        timer = new Timer();
        timerTask = new MyTimeTask();
        timer.schedule(timerTask,5000,2000);



    }

    @Override
    public void execute() {
     /**
         * ViewPager的PagerAdapter
         */
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViewsList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViewsList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(imageViewsList.get(position), 0);
                return imageViewsList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    /**
     *
     * */

    public class MyTimeTask extends TimerTask {
        @Override
        public void run() {

            currentItem = currentItem + 1;
            mHandler.obtainMessage().sendToTarget();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                    viewPager.setCurrentItem(currentItem);
                   if(currentItem==3)
                   timer.cancel();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide, menu);
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
