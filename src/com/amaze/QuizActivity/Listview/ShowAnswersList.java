package com.amaze.QuizActivity.Listview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amaze.QuizActivity.StartPage;
import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.amaze.QuizActivity.R;

public class ShowAnswersList extends Activity {

	private static final String TAG = "ShowCorrectAnswersList";
	ArrayList<Integer> questionlist, answerlist, useranswerlist;
	String type;
	MyDatabase db;
	TextView TestHeading;
	Button FinishButton;
	boolean isAntonymTest = false;
	boolean isAnalogiesTest = false;
	Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.correct_answer_list);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		db = MyDatabase.getInstance(getApplicationContext());
		Intent intent = this.getIntent();
		questionlist = intent.getIntegerArrayListExtra("questionsidlist");
		Log.d(TAG, "Ashish: The size of the questionsidlist is :"+questionlist.size());
		answerlist = intent.getIntegerArrayListExtra("correctanswerslist");
		useranswerlist = intent.getIntegerArrayListExtra("useranswerlist");
		type = intent.getStringExtra(Constants.type);
		TestHeading = (TextView) findViewById(R.id.Test_Heading_show_list);
		TestHeading.setTypeface(typeface);
		if (type.equals(Constants.antonym)) {
			TestHeading.setText(R.string.Anotnym_test);
			isAntonymTest = true;
		}else if(type.equals(Constants.analogies))
		{
			TestHeading.setText(R.string.Analogies_test);
			isAnalogiesTest = true;
		}
		ListView list = (ListView) findViewById(R.id.correct_answers_list);
		// ListAdapter adapter = new List_Adapter(getApplicationContext(),
		// questionlist, answerlist, useranswerlist, db, isAntonymTest);
		ListAdapter adapter = new List_Adapter(this, questionlist, answerlist,
				useranswerlist, db, isAntonymTest,isAnalogiesTest,typeface);
		list.setAdapter(adapter);

		FinishButton = (Button) findViewById(R.id.finish_button_list);
		FinishButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShowAnswersList.this,
						StartPage.class);
				startActivity(intent);
				finish();
			}
		});

	}

}
