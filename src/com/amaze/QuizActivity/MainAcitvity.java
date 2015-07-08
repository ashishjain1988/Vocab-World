package com.amaze.QuizActivity;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.amaze.QuizActivity.R;

public class MainAcitvity extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener, OnClickListener{
	private static final String TAG = "MainActivity";
	public long MAX_QUIZ_TIME = 1000*2*60;
	public boolean isAnyButtonChecked = false;
	public int answer = -1;
	public ArrayList<Integer> questionlist;///From Startpage the list if random numbers
	public int question_counter = 0;
	public ArrayList<Integer> answerlist;
	public ArrayList<Integer> useranswerlist;///The list of answers given by participant
	public HashMap<Integer, Integer> answer_map;
	MyDatabase db;
	RadioButton button1,button2,button3,button4,button5;
	TextView tv,tv1,TestHeading,textquestion,timerText;
	Button PreviousButton,NextButton;
	RadioGroup mRadiogroup;
	Chronometer mChrnometer;
	CalculateScore calculate;
	Typeface typeface;
	CountDownTimer timer;
	boolean isAntonymTest = false;
	boolean isAnalogiesTest = false;
	String type;
	private Dialog STdialog =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testactivity);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		db = MyDatabase.getInstance(getApplicationContext());
		TestHeading = (TextView)findViewById(R.id.Test_Heading);
		TestHeading.setTypeface(typeface);
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(Constants.type);
		type = bundle.getString(Constants.type);
		if(type.equals(Constants.antonym))
		{
			TestHeading.setText(R.string.Anotnym_test);
			isAntonymTest = true;
		}else if(type.equals(Constants.analogies))
		{
			TestHeading.setText(R.string.Analogies_test);
			isAnalogiesTest = true;
		}
			
		questionlist = intent.getIntegerArrayListExtra("QuestionList");
		tv = (TextView)findViewById(R.id.Question);/*tv.setTypeface(typeface);*/
		tv1 = (TextView)findViewById(R.id.Answer);/*tv1.setTypeface(typeface);*/
		textquestion = (TextView)findViewById(R.id.question_number);/*textquestion.setTypeface(typeface);*/
		timerText = (TextView)findViewById(R.id.count_down);/*timerText.setTypeface(typeface);*/
		mRadiogroup = (RadioGroup)findViewById(R.id.myRadioGroup1);
		button1 = (RadioButton)findViewById(R.id.myRadioButton1);/*button1.setTypeface(typeface);*/
		button2 = (RadioButton)findViewById(R.id.myRadioButton2);/*button2.setTypeface(typeface);*/
		button3 = (RadioButton)findViewById(R.id.myRadioButton3);/*button3.setTypeface(typeface);*/
		button4 = (RadioButton)findViewById(R.id.myRadioButton4);/*button4.setTypeface(typeface);*/
		button5 = (RadioButton)findViewById(R.id.myRadioButton5);/*button5.setTypeface(typeface);*/
		button1.setOnCheckedChangeListener(this);
		button2.setOnCheckedChangeListener(this);
		button3.setOnCheckedChangeListener(this);
		button4.setOnCheckedChangeListener(this);
		button5.setOnCheckedChangeListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		
		/*h = new Handler(){
			public void handleMessage(Message msg)
			{
				int key = msg.what;
				switch (key) {
				case 1:					
					long elapsedMillis = SystemClock.elapsedRealtime() - mChrnometer.getBase(); 
					if(elapsedMillis >=MAX_QUIZ_TIME)
					{
						calculateScore();
					}
					break;

				default:
					break;
				}
			}
		};*/
		
		PreviousButton = (Button)findViewById(R.id.Prev_button);PreviousButton.setTypeface(typeface);
		NextButton = (Button)findViewById(R.id.Next_Button);NextButton.setTypeface(typeface);
		PreviousButton.setOnClickListener(this);
		NextButton.setOnClickListener(this);
		PreviousButton.setVisibility(View.GONE);
		answer_map = new HashMap<Integer, Integer>();
		Initalizemap(answer_map);
		updateQuestion(questionlist.get(0));
		//mChrnometer.start();
		//long elapsedMillis = SystemClock.elapsedRealtime() - mChrnometer.getBase(); 
		/*t = new Timer();
		t.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();       
				message.what = 1; 
				h.sendMessage(message); 
			}
		},500);*/
		timer = new CountDownTimer(MAX_QUIZ_TIME,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				long minutes = (millisUntilFinished/1000)/60;
				long seconds = (millisUntilFinished/1000)%60;
				timerText.setText(minutes+":"+seconds+" Minutes Remaining");
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				calculateScore();
			}
		};
		timer.start();
		
	}

	public void updateQuestion(int questionnumber)
	{
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
			if(question.moveToFirst())
			{
				String ques = question.getString(question.getColumnIndex(Constants.QUESTION_COLUMN));
				tv.setText(ques);
				button1.setText(question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_1)));
				button2.setText(question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_2)));
				button3.setText(question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_3)));
				button4.setText(question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_4)));
				button5.setText(question.getString(question.getColumnIndex(Constants.ANSWER_OPTION_5)));
			}
		}

	}
	
	public void Initalizemap(HashMap<Integer, Integer> map)
	{
		answer_map.put(1, -1);
		answer_map.put(2, -1);
		answer_map.put(3, -1);
		answer_map.put(4, -1);
		answer_map.put(5, -1);
		answer_map.put(6, -1);
		answer_map.put(7, -1);
		answer_map.put(8, -1);
		answer_map.put(9, -1);
		answer_map.put(10, -1);
	}
	
	public void updateRadiobutton(int answer)
	{
		switch (answer) {
		case 1:
			button1.setChecked(true);
			break;
		case 2:
			button2.setChecked(true);
			break;
		case 3:
			button3.setChecked(true);
			break;
		case 4:
			button4.setChecked(true);
			break;
		case 5:
			button5.setChecked(true);
			break;
		case -1:
			mRadiogroup.clearCheck();
			break;
		default:
			mRadiogroup.clearCheck();
			break;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub		
			isAnyButtonChecked = false;
			switch (arg0.getId()) {
			case R.id.myRadioButton1:
				if(button1.isChecked())
				{
					answer = 1;
				}
				break;
			case R.id.myRadioButton2:
				if(button2.isChecked())
				{
					answer = 2;
				}
				break;
			case R.id.myRadioButton3:
				if(button3.isChecked())
				{
					answer = 3;
				}
				break;
			case R.id.myRadioButton4:
				if(button4.isChecked())
				{
					answer = 4;
				}
				break;
			case R.id.myRadioButton5:
				if(button5.isChecked())
				{
					answer = 5;
				}
				break;
			default:
				break;
			}		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId() == mRadiogroup.getCheckedRadioButtonId() && isAnyButtonChecked)
		{
			mRadiogroup.clearCheck();
			answer = -1;
		}
		else
		{
			isAnyButtonChecked =true;
		}
		switch (v.getId()) {
		case R.id.Next_Button:
			answer_map.put(question_counter+1, answer);/////To store Answers
			//Log.d(TAG, "Ankush:Next_Button The value of the answer before counter is :"+answer);
			if(question_counter<9)
			{
				question_counter++;
				textquestion.setText((question_counter+1)+" of 10");
				answer = answer_map.get(question_counter+1);
				updateQuestion(questionlist.get(question_counter));
				updateRadiobutton(answer);
				
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
				calculateScore();
							
			}


			break;
		case R.id.Prev_button:
			answer_map.put(question_counter+1, answer);
			question_counter--;	
			textquestion.setText((question_counter+1)+" of 10");
			answer = answer_map.get(question_counter+1);
			updateQuestion(questionlist.get(question_counter));
			updateRadiobutton(answer);
			
			if(question_counter == 8)
			{
				NextButton.setText(R.string.next);
			}
			if(question_counter == 0)
			{
				PreviousButton.setVisibility(View.GONE);				
			}
			break;
		case R.id.button_yes:
			answer_map.put(question_counter+1, answer);/////To store Answers
			calculateScore();
			break;
		case R.id.button_no:
			STdialog.dismiss();
			break;
		default:
			break;
		}
		
	}

	public void calculateScore()
	{
		Cursor correctanswer;
		int answer;
		answerlist = new ArrayList<Integer>();
		useranswerlist = new ArrayList<Integer>();
		calculate = new CalculateScore();
		for(int i=0;i<questionlist.size();i++)
		{
			if(isAntonymTest)
			{
				correctanswer = db.getAnswerById(Constants.TABLE_ANTONYM,questionlist.get(i));
			}
			else if(isAnalogiesTest)
			{
				correctanswer = db.getAnswerById(Constants.TABLE_ANALOGIES,questionlist.get(i));
			}else
			{
				correctanswer = db.getAnswerById(Constants.TABLE_SYNONYM,questionlist.get(i));
			}
			
			correctanswer.moveToFirst();
			Log.e("","Ashish: the cursor is "+correctanswer);
			answer = correctanswer.getInt(correctanswer.getColumnIndex(Constants.ANSWER_COLUMN));
			Log.e("","Ashish: the cursor is "+answer);
			answerlist.add(answer);
			useranswerlist.add(answer_map.get(i+1));/////To get user answers
		}
		calculate.Calculate(answer_map, answerlist);
		Intent intent = new Intent(MainAcitvity.this,ResultActivity.class);
		intent.putIntegerArrayListExtra("correctanswerslist", answerlist);
		intent.putIntegerArrayListExtra("questionsidlist", questionlist);
		intent.putIntegerArrayListExtra("useranswerlist", useranswerlist);
		intent.putExtra("correctAnswers", calculate.correctAnswers);
		intent.putExtra("wrongAnswers", calculate.wrongAnswers);
		intent.putExtra("unattempted", calculate.unattempted);
		SharedPreferences prefences = this.getSharedPreferences("firsttime", Context.MODE_WORLD_WRITEABLE);
		Editor editor = prefences.edit();
		editor.putBoolean("firstInresult", true);
		editor.commit();
		intent.putExtra(Constants.type,type);
		startActivity(intent);
		this.finish();	
	}
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		Dialog tmpdialog = null;
		switch (id) {
		case 1:
			STdialog = new Dialog(MainAcitvity.this,android.R.style.Theme_Translucent);
			STdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			STdialog.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
			STdialog.setContentView(R.layout.popupexit);
			TextView text = (TextView)STdialog.findViewById(R.id.Dialog_heading);
			text.setText(R.string.quitthetest);
			STdialog.setOnDismissListener(new OnDismissListener()
            {
                public void onDismiss(DialogInterface dialog)
                {
					MainAcitvity.this.removeDialog(1);
					STdialog.dismiss();
                }
            });
			Button button_yes = (Button)STdialog.findViewById(R.id.button_yes);
			button_yes.setOnClickListener(this);
			Button button_no = (Button)STdialog.findViewById(R.id.button_no);
			button_no.setOnClickListener(this);
			tmpdialog = STdialog;
			break;

		default:
			break;
		}
		return tmpdialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
		switch (id) {
		case 1:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showDialog(1);
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mChrnometer!=null)
		{
			mChrnometer.stop();
		}
		if(timer!=null)
		{
			timer.cancel();
		}
	}
	
	
}
