package com.amaze.QuizActivity.History;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.amaze.QuizActivity.StartPage;
import com.amaze.QuizActivity.R;

public class SynonymHistory extends Activity{

	
	private ListView list;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.synonymhistory);
	        list = (ListView)findViewById(R.id.list_synonym_history);
	        Log.d("", "Ashish: The score list  are s of size: "+StartPage.score_synonym.size());
	        Log.d("", "Ashish: The name list are s of size: "+StartPage.name_synonym.size());
	        ListAdapter adapter = new History_Adapter(this,StartPage.score_synonym,StartPage.name_synonym);
	        list.setAdapter(adapter);
	        
	 }
	 public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			Intent intent = new Intent(SynonymHistory.this,StartPage.class);
			startActivity(intent);
			SynonymHistory.this.finish();
	 }
}
