package com.amaze.QuizActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amaze.QuizActivity.Listview.ShowAnswersList;
import com.amaze.constants.Constants;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.HtmlAd;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class ResultActivity extends Activity implements OnClickListener, AdEventListener{
	
	TextView correctAnswerText,wrongAnswerText,unattemptedText,resulttext,tv,tv1,tv2,tv3,resultHeading;
	Button menuButton,reviewButton,reviewListbutton;
	int correctAnswers,wrongAnswers,unattempted;
	ArrayList<Integer> questionlist,answerlist,useranswerlist;
	String type;
	Typeface typeface;
	private Dialog STdialog =null;
	public static Handler h;
	int result;
	Spinner spinner;
	int type_of_view=0;
	String nameofplayer = "Player";
	//private AdView adview;
	/*private HtmlAd htmlAd = null;*/
	private StartAppAd startAppAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "12134512", "212026226", true);
		setContentView(R.layout.result);
			//////For Google Ad//////
			//adview = (AdView)this.findViewById(R.id.adView_result);
		    //adview.loadAd(new AdRequest());
			////////////////////////
		startAppAd = new StartAppAd(this);
		   //new TapContextSDK(getApplicationContext()).initialize();
		   
		    //AdPreferences adPreferences = new AdPreferences("112134512","212026226", AdPreferences.TYPE_INAPP_EXIT);
		    //htmlAd = new HtmlAd(this); 
		    //htmlAd.load(adPreferences, this);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/bankgthd.ttf");
		resultHeading = (TextView)findViewById(R.id.result_heading);
		resultHeading.setTypeface(typeface);
		correctAnswerText =(TextView)findViewById(R.id.correctanswerText);/*correctAnswerText.setTypeface(typeface);*/
		wrongAnswerText =(TextView)findViewById(R.id.wronganswerText);/*wrongAnswerText.setTypeface(typeface);*/
		unattemptedText =(TextView)findViewById(R.id.unattemptedanswerText);/*unattemptedText.setTypeface(typeface);*/
		resulttext =(TextView)findViewById(R.id.resulttext);/*resulttext.setTypeface(typeface);*/
		spinner = (Spinner)findViewById(R.id.review_option_spinner);
		menuButton =(Button)findViewById(R.id.startpageButton);menuButton.setTypeface(typeface);
		reviewButton = (Button)findViewById(R.id.reviewbutton);reviewButton.setTypeface(typeface);
		reviewListbutton = (Button)findViewById(R.id.reviewlistbutton);reviewListbutton.setTypeface(typeface);
		menuButton.setOnClickListener(this);
		reviewButton.setOnClickListener(this);
		reviewListbutton.setOnClickListener(this);
		Intent intent = this.getIntent();
		correctAnswers = intent.getIntExtra("correctAnswers", 0);
		wrongAnswers = intent.getIntExtra("wrongAnswers", 0);
		unattempted = intent.getIntExtra("unattempted", 0);
		questionlist = intent.getIntegerArrayListExtra("questionsidlist");
		answerlist = intent.getIntegerArrayListExtra("correctanswerslist");
		useranswerlist = intent.getIntegerArrayListExtra("useranswerlist");
		type = intent.getStringExtra(Constants.type);
		correctAnswerText.setText(correctAnswers+"");
		wrongAnswerText.setText(wrongAnswers+"");
		unattemptedText.setText(unattempted+"");
		result = ((correctAnswers * 3) - wrongAnswers);
		resulttext.setText(result+"");
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.view_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        //showToast("Spinner1: position=" + position + " id=" + id);
                    	Log.e("", "Ashish: The position selected is :"+position);
                    	type_of_view = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //showToast("Spinner1: unselected");
                    }
                });
		
        boolean fromMain;
        SharedPreferences prefrences = this.getSharedPreferences("firsttime",Context.MODE_WORLD_READABLE);
        fromMain = prefrences.getBoolean("firstInresult",true);
        if(fromMain)
        {
        	int position = -1;
        	if(type.equals(Constants.antonym))
        	{
        		position = linearsearch(StartPage.score,result);
        	}else if(type.equals(Constants.analogies))
        	{
        		position = linearsearch(StartPage.score_analogies,result);	
        	}else
        	{
        		position = linearsearch(StartPage.score_synonym,result);
        	}		
        	if(position!=-1)
        	{
        		showDialog(2);
        		//Toast.makeText(getApplicationContext(),"In show dialog: "+fromMain, Toast.LENGTH_LONG).show();
        		 SharedPreferences prefences = this.getSharedPreferences("firsttime", Context.MODE_WORLD_WRITEABLE);
        			Editor editor = prefences.edit();
        			editor.putBoolean("firstInresult", false);
        			editor.commit();
        	}
        }
		
		h = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					finish();
					break;

				default:
					break;
				}
			}
		};
		
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		startAppAd.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startAppAd.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void calculatepercentage()
	{
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.startpageButton:
			intent = new Intent(ResultActivity.this, StartPage.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.reviewbutton:
			intent = new Intent(ResultActivity.this,CountDown.class);
			Bundle bundle = new Bundle();
			bundle.putString("type",type);
			intent.putExtra("type",bundle);
			intent.putIntegerArrayListExtra("QuestionList",questionlist);
			intent.putExtra("isFromResult", true);
			startActivity(intent);
			ResultActivity.this.finish();
			break;
		case R.id.reviewlistbutton:
			if(type_of_view ==0)
			{
				intent = new Intent(ResultActivity.this, ShowAnswersList.class);
			}
			else
			{
				intent = new Intent(ResultActivity.this, ShowCorrectAnswers.class);
			}
			intent.putIntegerArrayListExtra("correctanswerslist", answerlist);
			intent.putIntegerArrayListExtra("questionsidlist", questionlist);
			intent.putIntegerArrayListExtra("useranswerlist", useranswerlist);
			intent.putExtra("type", type);
			startActivity(intent);
			break;
		case R.id.button_yes:
			startAppAd.onBackPressed();
			finish();
			break;
		case R.id.button_no:
			STdialog.dismiss();
			break;
		case R.id.button_ok:
			Log.d("", "Ashish: In the Ok click");
			EditText editbox = (EditText)STdialog.findViewById(R.id.editbox);
			nameofplayer = editbox.getText().toString();
			Log.d("", "Ashish: the result is :"+result);
			if(type.equals(Constants.antonym))
			{
				updateHistory(nameofplayer,result,StartPage.score,StartPage.name);
				STdialog.dismiss();
				writeInhistoryFile(StartPage.score,StartPage.name,Constants.antonym+".txt");
			}else if(type.equals(Constants.analogies))
			{
				updateHistory(nameofplayer,result,StartPage.score_analogies,StartPage.name_analogies);
				STdialog.dismiss();
				writeInhistoryFile(StartPage.score_analogies,StartPage.name_analogies,Constants.analogies+".txt");
			}else
			{
				updateHistory(nameofplayer,result,StartPage.score_synonym,StartPage.name_synonym);
				STdialog.dismiss();
				writeInhistoryFile(StartPage.score_synonym,StartPage.name_synonym,Constants.synonym+".txt");
			}		 
			break;
		case R.id.button_cancel:
			Log.d("", "Ashish: In the cancel click");
			STdialog.dismiss();
			break;
			
		default:
			break;
		}

	}
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		Dialog tmpdialog = null;
		switch (id) {
		case 1:
			STdialog = new Dialog(ResultActivity.this,android.R.style.Theme_Translucent);
			STdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			STdialog.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
			STdialog.setContentView(R.layout.popupexit);
			TextView text = (TextView)STdialog.findViewById(R.id.Dialog_heading);
			text.setText(R.string.quittest);
			STdialog.setOnDismissListener(new OnDismissListener()
            {
                public void onDismiss(DialogInterface dialog)
                {
					ResultActivity.this.removeDialog(1);
					STdialog.dismiss();
					 /*ExitAd exit = new ExitAd(getParent());
					 exit.show();*/
                }
            });
			Button button_yes = (Button)STdialog.findViewById(R.id.button_yes);
			button_yes.setOnClickListener(this);
			Button button_no = (Button)STdialog.findViewById(R.id.button_no);
			button_no.setOnClickListener(this);
			tmpdialog = STdialog;
			break;
		case 2:
			STdialog = new Dialog(ResultActivity.this,android.R.style.Theme_Translucent);
			STdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			STdialog.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
			STdialog.setContentView(R.layout.history_dialog);
			STdialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		        @Override
		        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
		            if (keyCode == KeyEvent.KEYCODE_BACK && 
		                event.getAction() == KeyEvent.ACTION_UP && 
		                !event.isCanceled()) {
		            	EditText editbox = (EditText)STdialog.findViewById(R.id.editbox);
		    			nameofplayer = editbox.getText().toString();
		    			Log.d("", "Ashish: the result is :"+result);
		    			if(type.equals(Constants.antonym))
		    			{
		    				updateHistory(nameofplayer,result,StartPage.score,StartPage.name);
		    				STdialog.dismiss();
		    				writeInhistoryFile(StartPage.score,StartPage.name,Constants.antonym+".txt");
		    			}else if(type.equals(Constants.analogies))
		    			{
		    				updateHistory(nameofplayer,result,StartPage.score_analogies,StartPage.name_analogies);
		    				STdialog.dismiss();
		    				writeInhistoryFile(StartPage.score_analogies,StartPage.name_analogies,Constants.analogies+".txt");
		    			}else
		    			{
		    				updateHistory(nameofplayer,result,StartPage.score_synonym,StartPage.name_synonym);
		    				STdialog.dismiss();
		    				writeInhistoryFile(StartPage.score_synonym,StartPage.name_synonym,Constants.synonym+".txt");
		    			}		            	
		                return true;
		            }
		            return false;
		        }
		    });
			STdialog.setOnDismissListener(new OnDismissListener()
            {
                public void onDismiss(DialogInterface dialog)
                {
					ResultActivity.this.removeDialog(2);
					STdialog.dismiss();
                }
            });
			Button button_ok = (Button)STdialog.findViewById(R.id.button_ok);
			button_ok.setOnClickListener(this);
			Button button_cancel = (Button)STdialog.findViewById(R.id.button_cancel);
			button_cancel.setOnClickListener(this);
			tmpdialog = STdialog;
			break;
		default:
			break;
		}
		return tmpdialog;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showDialog(1);
		
		
	}
	int linearsearch(List<Integer> list,int item)
	{
		int position = -1;
		
			for(int i=0;i<list.size();i++)
			{
				if(item>list.get(i))
				{
					position = i;
					break;
				}
			}
			
		return position;
	}
	public void writeInhistoryFile(ArrayList<Integer> score,ArrayList<String> name,String filename)
	{
		String temp = "";
		try {
	    	   FileOutputStream fw1=openFileOutput(filename,0);
	    	   for(int i=0;i<score.size();i++)
	    	   {
	    		   temp += name.get(i) + ";," + score.get(i) + "\r\n";
	    		   Log.e("", "Ashish: The temp is :"+temp);
	    	   }
	           byte [] bt1 = temp.getBytes();
	           fw1.write(bt1);
	           fw1.close();
		} catch (IOException e) {
			
			e.printStackTrace();
	       }
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
		
	}
	
	public void updateHistory(String player,int result, List<Integer> score, List<String> name)
	{
		Log.d("","Ashish: The result is :"+result);
		if(player.equals("")||player == null)
		{
			player = "Player";
		}
		if(score!=null && name!=null)
		{
			 if(score.size() == 10)
			 {
				 int position = linearsearch(score,result);
				 if(position!=-1)
				 {
					 score.add(position, result);
					 name.add(position, player);
					 score.remove(10);
					 name.remove(10);
				 }
			 }
			 else
			 {
				 int position = linearsearch(score,result);
				 if(position!=-1)
				 {
					 score.add(position, result);
					 name.add(position, player);
				 }
				 else
				 {
					 score.add(result);
					 name.add(player);
				 }
			 }
		}
		else
		{
			 score.add(result);
			 name.add(player);
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
