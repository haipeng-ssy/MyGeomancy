package com.haipeng.geomancy.lunarDatePicker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View.OnTouchListener;

import com.haipeng.geomancy.R;

public class LunarDatePicker extends LinearLayout {

	private float xDistance, yDistance, xLast, yLast;
	OnClickLunarDateListener mOldl;
	
	@SuppressLint("NewApi")
	public LunarDatePicker(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public LunarDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	    setMyView(context, attrs);
	}

	public LunarDatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setMyView(context, attrs);
	}

	public LunarDatePicker(Context context,OnClickLunarDateListener oldl) {
		super(context);
		// TODO Auto-generated constructor stub
		setMyView(context, null);
	    mOldl = oldl;
	}


	TextView tv_complete,tv_title;
	LinearLayout ll_1,ll_2,ll_3;
	TextView ll_1_tv_1,ll_1_tv_2,ll_1_tv_3,
	         ll_2_tv_1,ll_2_tv_2,ll_2_tv_3,
	         ll_3_tv_1,ll_3_tv_2,ll_3_tv_3;
	private void setMyView(Context context,AttributeSet attrs){
		LayoutInflater li = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view =li.inflate(R.layout.layout_lunar_date_picker, this);
		ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
		ll_2 = (LinearLayout) view.findViewById(R.id.ll_2);
		ll_3 = (LinearLayout) view.findViewById(R.id.ll_3);
		ll_1_tv_1 = (TextView) view.findViewById(R.id.ll_1_tv_1);
		ll_1_tv_2 = (TextView) view.findViewById(R.id.ll_1_tv_2);
		ll_1_tv_3 = (TextView) view.findViewById(R.id.ll_1_tv_3);
		ll_2_tv_1 = (TextView) view.findViewById(R.id.ll_2_tv_1);
		ll_2_tv_2 = (TextView) view.findViewById(R.id.ll_2_tv_2);
		ll_2_tv_3 = (TextView) view.findViewById(R.id.ll_2_tv_3);
		ll_3_tv_1 = (TextView) view.findViewById(R.id.ll_3_tv_1);
		ll_3_tv_2 = (TextView) view.findViewById(R.id.ll_3_tv_2);
		ll_3_tv_3 = (TextView) view.findViewById(R.id.ll_3_tv_3);
		
		ll_1.setOnTouchListener(new MyOnTouchListener(R.id.ll_1));
		ll_2.setOnTouchListener(new MyOnTouchListener(R.id.ll_2));
		ll_3.setOnTouchListener(new MyOnTouchListener(R.id.ll_3));
		
		tv_complete = (TextView) view.findViewById(R.id.tv_complete);
		tv_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str =""+ ll_1_tv_2.getText()
						     +ll_2_tv_2.getText()+ll_3_tv_2.getText();
				mOldl.onClickComplete(str);
			    
			}
		});
		tv_title = (TextView) view.findViewById(R.id.tv_title);
	}
	public TextView getTextViewComplete(){
		return tv_complete;
	}

	class MyOnTouchListener implements OnTouchListener{

		int id ;
		public MyOnTouchListener(int id) {
			// TODO Auto-generated constructor stub
		   this.id = id;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDistance = yDistance = 0;
				xLast = event.getX();
				yLast = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				xDistance = event.getX()-xLast;
				yDistance = event.getY()-yLast;
				if(id == R.id.ll_1)
				{
				setChangeTextView(ll_1_tv_1,ll_1_tv_2,ll_1_tv_3);
				}else if(id == R.id.ll_2)
				{
				setChangeTextView(ll_2_tv_1,ll_2_tv_2,ll_2_tv_3);
				}else if(id == R.id.ll_3)
				{
				setChangeTextView(ll_3_tv_1,ll_3_tv_2,ll_3_tv_3);
				}
				break;
			default:
				break;
			}
			return true;
		}
		
		public void setChangeTextView(TextView v1,TextView v2,TextView v3){
			
			
			if(yDistance>10)
			{
				setHuaDongNYR(v1,v2,v3,-1);
			}
			if(yDistance<-10)
			{
				setHuaDongNYR(v1,v2,v3,1);
			}
		}
		
		public int[] getMonths(int year){
			int[] n = null;
		    int leapMonth = CalendarUtil.leapMonth(year);
		    int hasLeapMoth;
		    if(leapMonth==0)
		    {
		    	hasLeapMoth = 0;
		    }else{
		    	hasLeapMoth = 1;
		    }
		    
		    n = new int[12+hasLeapMoth+1];
		    n[12+hasLeapMoth]= leapMonth;
		    int count = 0;
		    for(int i=0;i<12;i++)
		    {
		    	
		    	if(i!=0&&i==leapMonth)
		    	{
		    		n[i]=leapMonth;
		    		count = 1;
		    	}
		    	n[i+count] = i+1;
		    	
		    }
		    
		    return n;
		} 
		int count =0;
		int dayCount =0;
		int months[] = null;
		int monthDays[] =null;
		String daysStr[] = null;
	    String str1 ;
    	String str2 ;
    	String str3 ;
    	int days;
    	int one=0,two=1,three=2;
    	int one_day=0,two_day=1,three_day=2;
		public void setHuaDongNYR(TextView v1,TextView v2,TextView v3,int jx){
		
			String str = getNYR(v1);
			if(str.equals("月"))
			{
			
				  months = getMonths(getNeedInt(getNYR(ll_1_tv_2),ll_1_tv_2.getText().toString()));
				    
				    if(one<0)
				    {
				    	one = months.length -2;
				    }
				    if(two<0)
				    {
				    	two = months.length -2;
				    }
				    if(three<0)
				    {
				    	three = months.length -2;
				    }
				    if(one>months.length -2)
				    {
				    	one = 0;
				    }
				    if(two>months.length -2)
				    {
				    	two = 0;
				    }
				    if(three>months.length -2)
				    {
				    	three = 0;
				    }
				    if(months[months.length-1]==0)
				    {
				    	 str1 = months[one] + str;
				    	 str2 = months[two] + str;
				    	 str3 = months[three] + str;
				    	
				    }else{
				    	str1 = months[one] + str;
				    	str2 = months[two] + str;
				    	str3 = months[three] + str;
				    	if(one == months[months.length-1])
				    	{
				    		str1 = "闰"+str1;
				    	}else if(two == months[months.length-1])
				    	{
				    		str2 = "闰"+str2;
				    	}else if(three == months[months.length-1])
				    	{
				    		str3 = "闰"+str3;
				    	}
				    }
				    
					v1.setText(str1);
					v2.setText(str2);
					v3.setText(str3);
					one = one +jx;
					two = two +jx;
					three = three+jx;
			}else if(str.equals("日"))
			{
			
			   days = getMonthDays(ll_2_tv_2.getText().toString(), getNeedInt("年",ll_1_tv_2.getText().toString()));	
			   monthDays = new int[days];
			   daysStr = new String[days];
			   for(int i=0;i<days;i++)
			   {
				   monthDays[i] = i+1;
				   daysStr[i] = i+1+str;
			   }
			   
			   
			   if(one_day<0)
			    {
			    	one_day = days-1;
			    }
			    if(two_day<0)
			    {
			    	two_day = days-1;
			    }
			    if(three_day<0)
			    {
			    	three_day = days-1;
			    }
			    if(one_day>days-1)
			    {
			    	one_day = 0;
			    }
			    if(two_day>days-1)
			    {
			    	two_day = 0;
			    }
			    if(three_day>days-1)
			    {
			    	three_day = 0;
			    }
			    str1 = daysStr[one_day];
			    str2 = daysStr[two_day];
			    str3 = daysStr[three_day];
			   
			    v1.setText(str1);
				v2.setText(str2);
				v3.setText(str3);		
//				dayCount = dayCount+jx;
				one_day = one_day+jx;
				two_day = two_day+jx;
				three_day = three_day+jx;
				
			}else{
				int[] n = new int[3];
				n = getNumber(v1, v2, v3);		
			    
				n[0] = n[0]+jx;
			    n[1] = n[1]+jx;		    
			    n[2] = n[2]+jx;
			    if((n[0]+jx)<1901)
			    {
			    	n[0] = 1901;
			    	n[1] = 1902;
			    	n[2] = 1903;
			    }
			    v1.setText(n[0] + str);
			    v2.setText(n[1] + str);
			    v3.setText(n[2] + str);
			}
			tv_title.setText(ll_1_tv_2.getText()+ll_2_tv_2.getText().toString()+ll_3_tv_2.getText()+"");
		 
		}
		
		public String getNYR(TextView v1)
		{
			String str1 = v1.getText().toString();
			String NYR="";
			if(str1.contains("年"))
			{
			  	NYR = "年";
			}else if(str1.contains("月"))
			{
				NYR = "月";	
			}else if(str1.contains("日"))
			{
				NYR = "日";
			}
			return NYR;
		}
		public int[] getNumber(TextView v1,TextView v2,TextView v3)
		{
			String str1 = v1.getText().toString();
			String str2 = v2.getText().toString();
			String str3 = v3.getText().toString();
			int n[] = new int[3];
			
			n = (getNeedInt(getNYR(v1),str1,str2,str3));
			return n;
		}
		public int[] getNeedInt(String str,String str1,String str2,String str3){
			int index1,index2,index3;
			int one,two,three;
			index1 = str1.indexOf(str);
			index2 = str2.indexOf(str);
			index3 = str3.indexOf(str);
			int hasrun_one = 0,hasrun_two =0,hasrun_three=0;
			if(str1.contains("闰"))
			{
				hasrun_one =1;
			}
			if(str2.contains("闰"))
			{
				hasrun_two =1;
			}
			if(str3.contains("闰"))
			{
				hasrun_three =1;
			}
			one = Integer.parseInt(str1.subSequence(hasrun_one, index1).toString());
			two = Integer.parseInt(str2.subSequence(hasrun_two, index2).toString());
			three = Integer.parseInt(str3.subSequence(hasrun_three, index3).toString());
			int n[] = new int[3];
			n[0] = one;
			n[1] = two;
			n[2] = three;
			return n;
		}
		public int getNeedInt(String str,String str1){
			int index1;
			int n;
			index1 = str1.indexOf(str);
			n = Integer.parseInt(str1.subSequence(0, index1).toString());
			return n;
		}
		public int getMonthDays(String str,int year){
			
			int index;
			int days;
			int month;
			index = str.indexOf("月");
			CalendarUtil cu = new CalendarUtil();
			
			if(str.contains("闰"))
			{

				month = Integer.parseInt(str.subSequence(1, index).toString());
			    days  = cu.leapDays(year);
			}
			else{
			 month = Integer.parseInt(str.subSequence(0, index).toString());
			 days  = cu.monthDays(year, month);
			}
			return days;
		}
		
		
		
	}
		
}
