package com.amaze.QuizActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.amaze.constants.Constants;
import com.amaze.QuizActivity.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

public class CountDown extends Activity{
	public ArrayList<Integer> questionlist;
	String type;
	int layout1 []={R.drawable.three,R.drawable.two,R.drawable.one}; 
	Timer t,t1;
	int i=0;
	Runnable runnable;
	final Handler handler = new Handler();
	public boolean isfromresult = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown);
		Intent intent = getIntent();
		isfromresult = intent.getBooleanExtra("isFromResult", false);
		Log.e("", "Ashish: The value of isfromresult is :"+isfromresult);
		Bundle bundle = intent.getBundleExtra(Constants.type);
		type = bundle.getString(Constants.type);
		Set<Integer> set;
		if(type.equals(Constants.analogies))
		{
			set = randomList(Constants.MAX_QUESTION_RANGE_ANALOGIES, Constants.MIN_QUESTION_RANGE);
		}else
		{
			set = randomList(Constants.MAX_QUESTION_RANGE, Constants.MIN_QUESTION_RANGE);
		}
		
		Iterator<Integer> it = set.iterator();
		questionlist = new ArrayList<Integer>();
		while(it.hasNext())
		{		
			questionlist.add(it.next());
		}
		Log.e("", "Ashish: the type is :"+type);
		final LinearLayout layout = (LinearLayout)findViewById(R.id.countdown);
		t = new Timer();
		t1 = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bundle bundle1 = new Bundle();
				bundle1.putString(Constants.type,type);
				Intent intent1 = new Intent(CountDown.this,MainAcitvity.class);
				intent1.putExtra(Constants.type,bundle1);
				intent1.putIntegerArrayListExtra(Constants.QuestionList,questionlist);
				startActivity(intent1);
				CountDown.this.finish();

			}
		},3000);
		/*t1.schedule(new TimerTask() {
			LinearLayout layout = (LinearLayout)findViewById(R.id.countdown);
			@Override
			public void run() {
				// TODO Auto-generated method stub
				layout.setBackgroundResource(layout1[i]);
				i++;

			}
		},1000);*/
		runnable = new Runnable() {
			int i=0;
			public void run() {
				
				if(i<=layout1.length-1)
				{
					layout.setBackgroundResource(layout1[i]);
					i++;
				}
				handler.postDelayed(this,1000);  //for interval...
			}
		};
		handler.postDelayed(runnable,0);


	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(t!=null)
		{
			t.cancel();
		}
		if(handler!=null)
        {
        	handler.removeCallbacks(runnable);
        }
	}
	public Set<Integer> randomList(int max , int min)
	{
		HashSet<Integer> set = new HashSet<Integer>();
		while(set.size()<10)
		{
			int random = (int)(Math.random()*(max - min));
			Log.e("", "Ashish: The random is :"+random +" and the item is :"+ (min + random));
			set.add(min + random);
		}
		return set;		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}

}
