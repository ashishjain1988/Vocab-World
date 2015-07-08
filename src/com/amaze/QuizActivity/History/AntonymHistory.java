package com.amaze.QuizActivity.History;


import java.util.ArrayList;


import com.amaze.QuizActivity.StartPage;
import com.amaze.QuizActivity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AntonymHistory extends Activity{

	private ListView list;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.antonymhistory);
	        list = (ListView)findViewById(R.id.list_antonym_history);
	        Log.d("", "Ashish: The score list  are a of size: "+StartPage.score.size());
	        Log.d("", "Ashish: The name list are a of size: "+StartPage.name.size());
	        ListAdapter adapter = new History_Adapter(this,StartPage.score,StartPage.name);
	        list.setAdapter(adapter);
	        
	 }
	 public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			Intent intent = new Intent(AntonymHistory.this,StartPage.class);
			startActivity(intent);
			AntonymHistory.this.finish();
	 }
}
