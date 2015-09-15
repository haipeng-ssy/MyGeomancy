package com.haipeng.geomancy.adapter;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haipeng.geomancy.R;
import com.haipeng.geomancy.entity.EvaluteInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/5/24.
 */
public class EvaluteAdapter extends BaseAdapter {

    List<EvaluteInfo> mList;
    ActionBarActivity mContext;
    Typeface lishu;
    public EvaluteAdapter(ActionBarActivity context,List<EvaluteInfo> list){
        mList = list;
        mContext = context;
        lishu = Typeface.createFromAsset(context.getAssets(),"fonts/lishu.ttf");
    }
    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = mContext.getLayoutInflater();
        convertView = mInflater.inflate(R.layout.item_totalevalute,null);

        TextView tv_title = (TextView) convertView.findViewById(R.id.item_evalute_title);
        TextView tv_answer= (TextView) convertView.findViewById(R.id.item_evalute_answer);
        TextView tv_evalute= (TextView) convertView.findViewById(R.id.item_evalute_evalute);

        tv_title.setText(mList.get(position).getTitle());
        tv_title.setTypeface(lishu);
        tv_answer.setText(mList.get(position).getAnswer());
        tv_answer.setTypeface(lishu);
        tv_evalute.setText(mList.get(position).getEvalute()+"\n");
        tv_evalute.setTypeface(lishu);

        return convertView;
    }

}
