package com.amaze.filedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FileDatabase extends SQLiteOpenHelper{
	private static final String TAG = "FileDatabase";
	public static final String DATABASE_NAME = "Test";
	public static boolean isFromoncreate = false;
	public static final String TABLE_QUESTIONS = "Questions";
	public static final String QUESTIONS_COLUMN_ID = "_id";
	public static final String QUESTIONS_COLUMN_QUESTION ="Question";

	public static final String TABLE_ANSWERS = "Answers";
	public static final String ANSWERS_COLUMN_ID = "_id";
	public static final String ANSWERS_CORRECT_NUMBER = "CorrectAnswer";
	public static final String ANSWERS_COLUMN_ANSWER = "Answer";
	public static final String ANSWERS_OPTION = "Options";
	
	
	public FileDatabase(Context context) {
		super(context, DATABASE_NAME, null, 1);
		Log.d(TAG,"Ashish: in the constructor.");
		SQLiteDatabase db;
		db = this.getWritableDatabase();
		SharedPreferences prefrences = context.getSharedPreferences("firsttime",Context.MODE_WORLD_READABLE);
		isFromoncreate = prefrences.getBoolean("isFirstTime",true);
		Log.d(TAG, "Ashish: The registry value is :"+prefrences.getBoolean("isFirstTime",true));
		if(isFromoncreate)
		{
			dropTables();
			createQuestionTable(db);
			createAnswerTable(db);			
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Ashish: In the oncreate of file database");
		 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS); 
        // Create tables again
        onCreate(db);
	}

	public void setQuestion(ContentValues values){
		SQLiteDatabase db = this.getWritableDatabase();
		long d = db.insert(TABLE_QUESTIONS, null, values);
		Log.d(TAG,"Ashish: The set question is :"+d);
		db.close();
		Log.d(TAG,"Ashish: exit setQuestion.");
       
   }
	
	public void setAnswer(ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		long d = db.insert(TABLE_ANSWERS, null, values);
		Log.d(TAG,"Ashish: The set answer is :"+d);
		db.close();
		Log.d(TAG,"Ashish: exit setAnswer.");
       
   }
	
	public Cursor getQuestionById(long rowId) throws SQLException {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_QUESTIONS,
				new String[] { QUESTIONS_COLUMN_QUESTION }, QUESTIONS_COLUMN_ID + "= ?",
				new String [] {Long.toString(rowId)}, null, null, null, null);
		Log.d(TAG,"Ashish: The get question is :"+cursor.getCount());
		db.close();
		return cursor;
    }
 
    public Cursor getAnswerById(long rowId) throws SQLException {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(true, TABLE_ANSWERS,
    			new String []{ANSWERS_COLUMN_ANSWER},
    			ANSWERS_COLUMN_ID + "= "+rowId ,
    			null, null, null, null, null);
    	Log.d(TAG,"Ashish: The get answer is :"+cursor.getCount());
    	db.close();
    	return cursor;
    }
    public Cursor getCorrectAnswerById(long rowId) throws SQLException {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(true, TABLE_ANSWERS,
    			new String []{ANSWERS_CORRECT_NUMBER},
    			ANSWERS_COLUMN_ID + "= "+rowId ,
    			null, null, null, null, null);
    	Log.d(TAG,"Ashish: The get answer is :"+cursor.getCount());
    	db.close();
    	return cursor;
    }
    
    public void dropTables()
    {
    	Log.d(TAG,"Ashish: In the drop table function");
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
    	
    }
    public void createQuestionTable(SQLiteDatabase db)
    {
    	String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
		+ QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY," + QUESTIONS_COLUMN_QUESTION + " TEXT);";
		db.execSQL(CREATE_QUESTION_TABLE);
		Log.d(TAG,"Ashish: After making table question");
    }
    
    public void createAnswerTable(SQLiteDatabase db)
    {    	
		String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "("
		+ ANSWERS_COLUMN_ID + " Integer PRIMARY KEY," + ANSWERS_COLUMN_ANSWER + " TEXT NOT NULL," + ANSWERS_CORRECT_NUMBER +
		" Integer);";
		db.execSQL(CREATE_ANSWER_TABLE);
    }
    
    boolean isTableExists(String tableName) {
       SQLiteDatabase mDatabase = this.getReadableDatabase();

       Cursor c = null;
       boolean tableExists = false;
       //get cursor on it
       try
       {
           c = mDatabase.query("tbl_example", null,
               null, null, null, null, null);

           tableExists = true;
       }
       catch (Exception e) {
           //fail
           Log.d(TAG, tableName+" doesn't exist :(((");
       }

       return tableExists;
    }

}
