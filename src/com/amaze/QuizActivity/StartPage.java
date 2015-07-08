package com.amaze.QuizActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.amaze.QuizActivity.History.History;
import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class StartPage extends Activity implements OnClickListener, AdEventListener{
	public static ArrayList<String> name = new ArrayList<String>();
	public static ArrayList<Integer> score = new ArrayList<Integer>();
	
	public static ArrayList<String> name_synonym = new ArrayList<String>();
	public static ArrayList<Integer> score_synonym = new ArrayList<Integer>();
	
	public static ArrayList<String> name_analogies = new ArrayList<String>();
	public static ArrayList<Integer> score_analogies = new ArrayList<Integer>();
	Dialog dialog;
	public final int EXIT_DIALOG = 11;	
	private static final String TAG = "StartPage";
	Typeface typeface;
	private Dialog STdialog =null;
	MyDatabase mDb;
	private StartAppAd startAppAd;
	//private AdView adview;
	//private HtmlAd htmlAd = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "12134512", "212026226", true);
		Intent intent = getIntent();
		boolean isfromFlash = intent.getBooleanExtra("isfromFlash", false);
		if(isfromFlash)
		{
			StartAppAd.showSplash(this, savedInstanceState);
		}
		setContentView(R.layout.startpage);
		//////For Google Ad//////
		/*adview = (AdView)this.findViewById(R.id.adView);
	    adview.loadAd(new AdRequest());*/
		////////////////////////
	    //////For start App/////
	    //AndroidSDKProvider.initSDK(this);
	    /*AdPreferences adPreferences = new AdPreferences("112134512","212026226", AdPreferences.TYPE_INAPP_EXIT);
	    htmlAd = new HtmlAd(this); 
	    htmlAd.load(adPreferences, this);*/
	    ////////////////////////
	    //new TapContextSDK(getApplicationContext()).initialize();
		StartAppAd.showSlider(this);
		startAppAd = new StartAppAd(this);
		////////Get and Initalize database/////////////
		mDb = MyDatabase.getInstance(getApplicationContext());
		//////////////////////////////////////////////
		
		if(isfromFlash)
		{
			readFromHistoryFile(Constants.antonym+".txt",score,name);
			readFromHistoryFile(Constants.synonym+".txt",score_synonym,name_synonym);
			readFromHistoryFile(Constants.analogies+".txt",score_analogies,name_analogies);
		}
		////////////////////////////////////////////////////////////
		typeface = Typeface.createFromAsset(getAssets(),"fonts/UnrealT.ttf");
		Button AntonymTest = (Button)findViewById(R.id.start_antonym_button);
		Button SynonymTest = (Button)findViewById(R.id.start_synoynm_button);
		Button AnalogiesTest = (Button)findViewById(R.id.start_analogies_button);
		Button FlashCard = (Button)findViewById(R.id.flash_card);
		Button HelpButton = (Button)findViewById(R.id.Help);
		Button AboutButton = (Button)findViewById(R.id.about);
		Button MoreButton = (Button)findViewById(R.id.more);
		Button FBButton = (Button)findViewById(R.id.fbButton);
		Button Exit = (Button)findViewById(R.id.exit);
		buttonMakeUp(AntonymTest, typeface);
		buttonMakeUp(SynonymTest, typeface);
		buttonMakeUp(FlashCard, typeface);
		buttonMakeUp(HelpButton, typeface);
		buttonMakeUp(AboutButton, typeface);
		buttonMakeUp(Exit, typeface);
		buttonMakeUp(AnalogiesTest, typeface);
		buttonMakeUp(MoreButton, typeface);
		buttonMakeUp(FBButton, typeface);
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle bundle;
		switch (arg0.getId()) {
		case R.id.start_antonym_button:
			intent = new Intent(StartPage.this,CountDown.class);
			bundle = new Bundle();
			bundle.putString(Constants.type,Constants.antonym);
			intent.putExtra(Constants.type,bundle);
			startActivity(intent);
			StartPage.this.finish();
			break;
			
		case R.id.start_synoynm_button:
			intent = new Intent(StartPage.this,CountDown.class);
			bundle = new Bundle();
			bundle.putString(Constants.type,Constants.synonym);
			intent.putExtra(Constants.type,bundle);
			startActivity(intent);
			StartPage.this.finish();		
			break;
			
		case R.id.start_analogies_button:
			intent = new Intent(StartPage.this,CountDown.class);
			bundle = new Bundle();
			bundle.putString(Constants.type,Constants.analogies);
			intent.putExtra(Constants.type,bundle);
			startActivity(intent);
			StartPage.this.finish();		
			break;
			
		case R.id.flash_card:
			intent = new Intent(StartPage.this,FlashCard.class);
			startActivity(intent);
			StartPage.this.finish();
			break;
			
		case R.id.Help:
			intent = new Intent(StartPage.this,Instructions.class);
			startActivity(intent);
			break;
			
		case R.id.about:
			intent = new Intent(StartPage.this,History.class);
			startActivity(intent);
			StartPage.this.finish();
			break;
			
		case R.id.more:
			Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=A-MAZE+Studios");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
			break;
			
		case R.id.fbButton:
			Uri uri1 = Uri.parse("http://www.facebook.com/pages/A-Maze-Studios/536581979714975?ref=hl");
            startActivity(new Intent(Intent.ACTION_VIEW, uri1));
			break;
			
		case R.id.exit:
			/*if(htmlAd != null) {
				htmlAd.show();
				}*/
			showDialog(EXIT_DIALOG);
			break;
			
		case R.id.button_yes:
			/*if(htmlAd != null) {
				htmlAd.show();
				}*/
			startAppAd.onBackPressed();
			finish();
			break;
			
		case R.id.button_no:
			STdialog.dismiss();
			break;

		default:
			break;
		}
		
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		Dialog tmpdialog = null;
		switch (id) {
		case EXIT_DIALOG:
			STdialog = new Dialog(StartPage.this,android.R.style.Theme_Translucent);
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
					StartPage.this.removeDialog(EXIT_DIALOG);
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

		default:
			break;
		}
		return tmpdialog;
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showDialog(EXIT_DIALOG);
	}

	
	public void readFromHistoryFile(String fileName,List<Integer> score, List<String> name)
	{
		String lineconf;
		name.clear();
		score.clear();
		try {

			FileInputStream inputStream = openFileInput(fileName);
			InputStreamReader rafconf = new InputStreamReader(inputStream);			
			BufferedReader buffreaderconf = new BufferedReader(rafconf);
			Log.e(TAG, "Ashish: The history file exists for "+fileName);
			while (null != ( lineconf = buffreaderconf.readLine()))
			{
				Log.e(TAG, "Ashish: Lineconfig is "+lineconf);
				String [] strarray = lineconf.split(";,");
				if(strarray.length>2) continue;
				name.add(strarray[0]);
				Log.e("","Ashish: the strarray 1 is :"+Integer.parseInt(strarray[1]));
				score.add(Integer.parseInt(strarray[1]));	
				Log.e(TAG, "Ashish: name :"+name+" score : "+score);
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "Ashish: In the catch of readhistory");
			for(int i=0;i<10;i++)
			{
				score.add(0);
				name.add("Player"+(i+1));
			}
		}
	}
	public void buttonMakeUp(Button button, Typeface typeface)
	{
		button.setTypeface(typeface);
		button.setTextColor(getResources().getColor(R.color.Blue));
		button.setOnClickListener(this);
	}
	
	public void buttonMakeUpDisable(Button button, Typeface typeface)
	{
		button.setTypeface(typeface);
		button.setTextColor(getResources().getColor(R.color.Gray));
		//button.setOnClickListener(this);
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		// TODO Auto-generated method stub
		/*if(htmlAd != null) {
			htmlAd.show();
			}*/
	}
}
