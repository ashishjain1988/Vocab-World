package com.amaze.QuizActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.amaze.QuizActivity.R;

public class ShowCorrectAnswers extends Activity implements OnClickListener{
	
	private static final String TAG = "ShowCorrectAnswers";
	ArrayList<Integer> questionlist,answerlist,useranswerlist;
	String type;
	MyDatabase db;
	Button button1,button2,button3,button4,button5;
	ImageView view1,view2,view3,view4,view5;
	TextView tv,tv1,TestHeading,textquestion,textUsage,textMeaning;
	Button PreviousButton,NextButton;
	public int question_counter = 0;
	int button_list[] = {R.id.myRadioButton1_show,R.id.myRadioButton2_show,R.id.myRadioButton3_show,R.id.myRadioButton4_show,R.id.myRadioButton5_show};
	int check_list [] = {R.id.myCheck1,R.id.myCheck2,R.id.myCheck3,R.id.myCheck4,R.id.myCheck5};
	boolean isAntonymTest = false;
	boolean isAnalogiesTest = false;
	Typeface typeface;
	LinearLayout meaningLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showcorrectanswers);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		Intent intent = this.getIntent();
		questionlist = intent.getIntegerArrayListExtra("questionsidlist");
		answerlist = intent.getIntegerArrayListExtra("correctanswerslist");
		useranswerlist = intent.getIntegerArrayListExtra("useranswerlist");
		type = intent.getStringExtra(Constants.type);
		TestHeading = (TextView)findViewById(R.id.Test_Heading_show);TestHeading.setTypeface(typeface);
		if(type.equals(Constants.antonym))
		{
			TestHeading.setText(R.string.Anotnym_test);
			isAntonymTest = true;
		}else if(type.equals(Constants.analogies))
		{
			TestHeading.setText(R.string.Analogies_test);
			isAnalogiesTest = true;
			meaningLayout = (LinearLayout)findViewById(R.id.show_descriptionLayout);
			meaningLayout.setVisibility(View.GONE);
			
		}
		tv = (TextView)findViewById(R.id.Question_show);/*tv.setTypeface(typeface);*/
		tv1 = (TextView)findViewById(R.id.Answer_show);/*tv1.setTypeface(typeface);*/
		textquestion = (TextView)findViewById(R.id.question_number_show);/*textquestion.setTypeface(typeface);*/
		/*textUsage = (TextView)findViewById(R.id.sentence);*/
		textMeaning = (TextView)findViewById(R.id.description);
		button1 = (Button)findViewById(R.id.myRadioButton1_show);view1 = (ImageView)findViewById(R.id.myCheck1);/*button1.setTypeface(typeface);*/
		button2 = (Button)findViewById(R.id.myRadioButton2_show);view2 = (ImageView)findViewById(R.id.myCheck2);/*button2.setTypeface(typeface);*/
		button3 = (Button)findViewById(R.id.myRadioButton3_show);view3 = (ImageView)findViewById(R.id.myCheck3);/*button3.setTypeface(typeface);*/
		button4 = (Button)findViewById(R.id.myRadioButton4_show);view4 = (ImageView)findViewById(R.id.myCheck4);/*button4.setTypeface(typeface);*/
		button5 = (Button)findViewById(R.id.myRadioButton5_show);view5 = (ImageView)findViewById(R.id.myCheck5);/*button5.setTypeface(typeface);*/
		PreviousButton = (Button)findViewById(R.id.Prev_button_show);PreviousButton.setTypeface(typeface);
		NextButton = (Button)findViewById(R.id.Next_Button_show);NextButton.setTypeface(typeface);
		PreviousButton.setOnClickListener(this);
		NextButton.setOnClickListener(this);
		PreviousButton.setVisibility(View.GONE);
		
		db = MyDatabase.getInstance(getApplicationContext());
		updateQuestion(questionlist.get(0),answerlist.get(0),useranswerlist.get(0));
		
		
	}
	
	public void updateQuestion(int questionnumber, int correctanwer, int useranswer)
	{
		Log.d(TAG, "questionnumber is :"+questionnumber+" correctanwer is :"+correctanwer+" useranswer is :"+useranswer);
		Cursor question;
		if(isAntonymTest)
		{
			question = db.getRowById(Constants.TABLE_ANTONYM,questionnumber);
		}
		else if(isAnalogiesTest)
		{
			question = db.getRowById(Constants.TABLE_ANALOGIES,questionnumber);
		}else
		{
			question = db.getRowById(Constants.TABLE_SYNONYM,questionnumber);
		}
				
		if(question!=null)
		{
			Log.d(TAG, "Ashish: In the if of cursor");
			if(question.moveToFirst())
			{
				String ques = question.getString(question.getColumnIndex(Constants.QUESTION_COLUMN));
				tv.setText(ques);	
				updateButton(button1,question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_1)),1);
				view1.setVisibility(View.GONE);
				updateButton(button2,question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_2)),2);
				view2.setVisibility(View.GONE);
				updateButton(button3,question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_3)),3);
				view3.setVisibility(View.GONE);
				updateButton(button4,question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_4)),4);
				view4.setVisibility(View.GONE);
				updateButton(button5,question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_5)),5);
				view5.setVisibility(View.GONE);
				if(!isAnalogiesTest)
				textMeaning.setText(question.getString(question.getColumnIndex(Constants.ANSWER_DESCRIPTION)).trim());
			}
			if(correctanwer == useranswer)
			{
				Button btn = (Button)findViewById(button_list[correctanwer-1]);
				btn.setTextColor(Color.GREEN);
				ImageView img = (ImageView)findViewById(check_list[correctanwer-1]);
				img.setBackgroundResource(R.drawable.button_ok);
				img.setVisibility(View.VISIBLE);
			}
			else if(useranswer == -1)
			{
				Button btn = (Button)findViewById(button_list[correctanwer-1]);
				btn.setTextColor(Color.GREEN);
				
			}else
			{
				Button btn = (Button)findViewById(button_list[correctanwer-1]);
				btn.setTextColor(Color.GREEN);
				ImageView img = (ImageView)findViewById(check_list[correctanwer-1]);
				img.setBackgroundResource(R.drawable.button_ok);
				img.setVisibility(View.VISIBLE);
								
				Button btn2 = (Button)findViewById(button_list[useranswer-1]);
				btn2.setTextColor(Color.RED);
				ImageView img1 = (ImageView)findViewById(check_list[useranswer-1]);
				img1.setBackgroundResource(R.drawable.fileclose);
				img1.setVisibility(View.VISIBLE);
				
			}
		}

	}
	
	public void updateButton(Button b,String s,int questionnumber)
	{
		b.setText(questionnumber+". "+s);
		b.setTextColor(Color.BLACK);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Prev_button_show:
			question_counter--;	
			textquestion.setText((question_counter+1)+" of 10");
			updateQuestion(questionlist.get(question_counter),answerlist.get(question_counter),useranswerlist.get(question_counter));			
			if(question_counter == 8)
			{
				NextButton.setText(R.string.next);
			}
			if(question_counter == 0)
			{
				PreviousButton.setVisibility(View.GONE);				
			}
			
			break;

		
		case R.id.Next_Button_show:
			if(question_counter<9)
			{
				question_counter++;
				textquestion.setText((question_counter+1)+" of 10");
				Log.d(TAG, "Ankush:Next_Button The value of the question_counter is :"+question_counter);
				updateQuestion(questionlist.get(question_counter),answerlist.get(question_counter),useranswerlist.get(question_counter));
				
				if(question_counter == 9)
				{
					NextButton.setText("Finish");
				}
				if(question_counter>0)
				{
					PreviousButton.setVisibility(View.VISIBLE);
				}
			}
			else
			{
				/////Finish and calculate the score				
				Intent intent = new Intent(ShowCorrectAnswers.this,StartPage.class);
				ResultActivity.h.sendEmptyMessage(0);
				startActivity(intent);
				this.finish();
				
			}


			break;
		default:
			break;
		}
	}

}
