package com.amaze.QuizActivity;




import com.amaze.QuizActivity.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Instructions extends Activity
{
	Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		TextView text = (TextView)findViewById(R.id.instruct);
		text.setTypeface(typeface);
		
	}
}
