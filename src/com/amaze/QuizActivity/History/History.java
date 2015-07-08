package com.amaze.QuizActivity.History;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.amaze.QuizActivity.StartPage;

public class History extends TabActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final TabHost tabhost = getTabHost();		
		tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("Antonym Test").setContent(new Intent(this,AntonymHistory.class)));
		tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("Synonym Test").setContent(new Intent(this,SynonymHistory.class)));
		tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("Analogies Test").setContent(new Intent(this,AnalogiesHistory.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
	}
	 public View createTabContent(String tag) {
	        final TextView tv = new TextView(this);
	        tv.setText("Content for tab with tag " + tag);
	        return tv;
	    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		Intent intent = new Intent(History.this,StartPage.class);
		startActivity(intent);
		History.this.finish();
		
	}
	 
}
