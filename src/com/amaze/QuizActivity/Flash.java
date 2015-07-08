package com.amaze.QuizActivity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.HtmlAd;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.model.AdPreferences;

public class Flash extends Activity implements AdEventListener{

	Timer t;
	private  boolean bfinished = false;
	//private HtmlAd htmlAd = null;
	private StartAppAd startAppAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Typeface typeface;
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "12134512", "212026226", true);
		setContentView(R.layout.flash);
		/*AdPreferences adPreferences = new AdPreferences("112134512","212026226", AdPreferences.TYPE_INAPP_EXIT);
	    htmlAd = new HtmlAd(this); 
	    htmlAd.load(adPreferences, this);*/
	    startAppAd = new StartAppAd(this);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/Jokerman-Regular.ttf");
		TextView button = (TextView)findViewById(R.id.welcome);
		button.setTypeface(typeface);
		button.setTextColor(Color.GREEN);
		TextView to = (TextView)findViewById(R.id.to);
		to.setTypeface(typeface);
		to.setTextColor(Color.GREEN);
		TextView vocab = (TextView)findViewById(R.id.Vocab);
		vocab.setTypeface(typeface);
		vocab.setTextColor(Color.GREEN);
		t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!bfinished)
				{
					bfinished = true;
					Intent intent = new Intent(Flash.this,StartPage.class);
					intent.putExtra("isfromFlash",true);
					startActivity(intent);
					Flash.this.finish();
				}

			}
		}, 2000);
		
		Button btn = (Button)findViewById(R.id.button_flash);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!bfinished)
				{
					bfinished = true;
					Intent intent = new Intent(Flash.this,StartPage.class);
					intent.putExtra("isfromFlash",true);
					startActivity(intent);
					Flash.this.finish();
				}				
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*if(htmlAd != null) {
			htmlAd.show();
			}*/
		if(t!=null)
		{
			t.cancel();
		}
	}
	@Override
	public void onFailedToReceiveAd(Ad arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReceiveAd(Ad arg0) {
		// TODO Auto-generated method stub
		
	}
}
