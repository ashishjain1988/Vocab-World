package com.amaze.QuizActivity.History;

import com.amaze.QuizActivity.StartPage;
import com.amaze.QuizActivity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AnalogiesHistory extends Activity{
	private ListView list;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.analogieshistory);
	        list = (ListView)findViewById(R.id.list_analogies_history);
	        Log.d("", "Ashish: The score list  are a of size: "+StartPage.score_analogies.size());
	        Log.d("", "Ashish: The name list are a of size: "+StartPage.name_analogies.size());
	        ListAdapter adapter = new History_Adapter(this,StartPage.score_analogies,StartPage.name_analogies);
	        list.setAdapter(adapter);
	        
	 }
	 public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			Intent intent = new Intent(AnalogiesHistory.this,StartPage.class);
			startActivity(intent);
			AnalogiesHistory.this.finish();
	 }

}
