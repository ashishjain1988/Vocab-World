package com.amaze.database;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amaze.QuizActivity.R;
import com.amaze.constants.Constants;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper{
	
	private static final String TAG = "MyDatabase";
	private String DATABASE_PATH;
	public static MyDatabase _dbHelper;
    private SQLiteDatabase mDb;
    private final Context mContext;
    public static boolean isFromoncreate = false;
    private boolean mCreateDatabase = true;
    private boolean mUpgradeDatabase = false;
	
	//////////////Constructor Of the My Database adapter/////////////////////
	private MyDatabase(Context context) 
	{
		super(context, Constants.DATABASE_NAME, null, /*context.getResources().getInteger(R.integer.questionDbVersion)*/1);
		DATABASE_PATH = "/data/data/"+context.getPackageName()+"/databases/";		
		mContext = context;
		initializeDatabase();
	}
	
	public static MyDatabase getInstance(Context context)
	{
		if( _dbHelper== null)
		{
			_dbHelper = new MyDatabase(context);
		}
		return _dbHelper;
	}
	/////////Initalize and copy the user Database////////////////
	public void initializeDatabase(/*String path*/) {
        getWritableDatabase();
        Log.d(TAG, "Ashish: In the initalize database");
        if(mUpgradeDatabase) {
            mContext.deleteDatabase(Constants.DATABASE_NAME);
            SharedPreferences prefences = mContext.getSharedPreferences("firsttime", Context.MODE_WORLD_WRITEABLE);
			Editor editor = prefences.edit();
			editor.putBoolean("isFirstTime", true);
			editor.commit();
        }
 
        SharedPreferences prefrences = mContext.getSharedPreferences("firsttime",Context.MODE_WORLD_READABLE);
		isFromoncreate = prefrences.getBoolean("isFirstTime",true);
		Log.d(TAG, "Ashish: The registry value is :"+isFromoncreate);
		if(isFromoncreate)
		 {
            try {
                copyDatabase();
            } catch (IOException e) {
                Log.d(TAG,"In the exception: "+e.getMessage());
                
            }
            
        }
    }
	///////////////Copies the User Database into the Phone's Database/////////////
	 private void copyDatabase() throws IOException {
	        //close();
	        Log.d(TAG, "Ashish: In the copydatabase after close();");
	        InputStream input = mContext.getResources().openRawResource(R.raw.synonym);
	        Log.d(TAG, "Ashish: In the after getting inputstream");
	        String outFileName = DATABASE_PATH + Constants.DATABASE_NAME;
	        OutputStream output = new FileOutputStream(outFileName);
	        Log.d(TAG, "Ashish: In the after getting outputstream");
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = input.read(buffer)) > 0) {
	            output.write(buffer, 0, length);
	        }
	        output.flush();
	        output.close();
	        input.close();
	        
	        SharedPreferences prefences = mContext.getSharedPreferences("firsttime", Context.MODE_WORLD_WRITEABLE);
			Editor editor = prefences.edit();
			editor.putBoolean("isFirstTime", false);
			editor.commit();
	    }
	
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "Ashish: In the oncreate of MyDatabase");
		mCreateDatabase = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		mUpgradeDatabase = true;
	}
	/////////To Open The Database/////////////////////
	public MyDatabase open() throws SQLException 
	{
		mDb = getReadableDatabase();
		return this;
	}
	/////////To Close The Database/////////////////////
	public void CleanUp() 
	{
		mDb.close();
	}
	
	/**
     * Public helper methods
     */
 
	public Cursor getAll(String tableName) throws SQLException {
    	SQLiteDatabase mDb =getReadableDatabase();
        Cursor cursor = mDb.query(true, tableName,
                null,null,
                null, null, null, null,"50");        
        return cursor;
    }
    public Cursor getRowById(String tableName,long rowId) throws SQLException {
    	SQLiteDatabase mDb =getReadableDatabase();
        Cursor cursor = mDb.query(true,tableName,
                null, Constants.COLUMN_ID + "=" + rowId,
                null, null, null, null, null);        
        return cursor;
    }
    
    public Cursor getAnswerById(String tableName,long rowId) throws SQLException {
    	
    	SQLiteDatabase mDb =getReadableDatabase();
        Cursor cursor = mDb.query(true, tableName,
        		new String [] {Constants.ANSWER_COLUMN}, Constants.COLUMN_ID + "=" + rowId,
                null, null, null, null, null);        
        return cursor;
    } 
}
