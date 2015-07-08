package com.amaze.QuizActivity.Listview;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amaze.constants.Constants;
import com.amaze.database.MyDatabase;
import com.amaze.QuizActivity.R;

public class List_Adapter extends BaseAdapter {
	Context context;
	ArrayList<Integer> questionlist, answerlist, useranswerlist;
	MyDatabase db;
	boolean isAntonym;
	boolean isAnalogy;
	Typeface typeface;
	private static final String TAG = "List_Adapter";
	int button_list[] = { R.id.myRadioButton1_list, R.id.myRadioButton2_list,
			R.id.myRadioButton3_list, R.id.myRadioButton4_list,
			R.id.myRadioButton5_list };
	int check_list[] = { R.id.myCheck1_list, R.id.myCheck2_list, R.id.myCheck3_list,
			R.id.myCheck4_list, R.id.myCheck5_list };

	public List_Adapter(Context context, ArrayList<Integer> questionlist,
			ArrayList<Integer> answerlist, ArrayList<Integer> useranswerlist,
			MyDatabase db, boolean isAntonym,boolean isAnalogy,Typeface typeface) {
		super();
		this.context = context;
		this.questionlist = questionlist;
		this.answerlist = answerlist;
		this.useranswerlist = useranswerlist;
		this.db = db;
		this.isAntonym = isAntonym;
		this.isAnalogy = isAnalogy;
		this.typeface = typeface;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Log.d(TAG, "Ashish: The position is :"+position);
		int correctanswer = -1; 
		correctanswer = answerlist.get(position);
		Log.d(TAG, "Ashish: The correctanswer is :"+correctanswer);
		int useranswer = -1; 
		useranswer = useranswerlist.get(position);
		Log.d(TAG, "Ashish: The useranswer is :"+useranswer);
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.list_item, null);
			holder = new ViewHolder();
			holder.question_name = (TextView) convertView.findViewById(R.id.Question_list);
			holder.question_number = (TextView) convertView
					.findViewById(R.id.question_number_list);
			
			holder.option1 = (TextView) convertView.findViewById(R.id.myRadioButton1_list);
			holder.option1.setTextColor(Color.BLACK);
			
			holder.option2 = (TextView) convertView.findViewById(R.id.myRadioButton2_list);
			holder.option2.setTextColor(Color.BLACK);
			
			holder.option3 = (TextView) convertView.findViewById(R.id.myRadioButton3_list);
			holder.option3.setTextColor(Color.BLACK);
			
			holder.option4 = (TextView) convertView.findViewById(R.id.myRadioButton4_list);
			holder.option4.setTextColor(Color.BLACK);
			
			holder.option5 = (TextView) convertView.findViewById(R.id.myRadioButton5_list);
			holder.option5.setTextColor(Color.BLACK);
			holder.Description = (TextView)convertView.findViewById(R.id.description_list);
			if(isAnalogy)
			{
				holder.meaningLayout = (LinearLayout)convertView.findViewById(R.id.description_Layout);
				holder.meaningLayout.setVisibility(View.GONE);
			}
			holder.im1 = (ImageView) convertView.findViewById(R.id.myCheck1_list);
			holder.im1.setVisibility(View.INVISIBLE);
			holder.im2 = (ImageView) convertView.findViewById(R.id.myCheck2_list);
			holder.im2.setVisibility(View.INVISIBLE);
			holder.im3 = (ImageView) convertView.findViewById(R.id.myCheck3_list);
			holder.im3.setVisibility(View.INVISIBLE);
			holder.im4 = (ImageView) convertView.findViewById(R.id.myCheck4_list);
			holder.im4.setVisibility(View.INVISIBLE);
			holder.im5 = (ImageView) convertView.findViewById(R.id.myCheck5_list);
			holder.im5.setVisibility(View.INVISIBLE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/*holder.Description.setTypeface(typeface);*/
		/*holder.Sentence.setTypeface(typeface);*/
		holder.option1.setTextColor(Color.BLACK);
		/*holder.option1.setTypeface(typeface);*/
		holder.option2.setTextColor(Color.BLACK);
		/*holder.option2.setTypeface(typeface);*/
		holder.option3.setTextColor(Color.BLACK);
		/*holder.option3.setTypeface(typeface);*/
		holder.option4.setTextColor(Color.BLACK);
		/*holder.option4.setTypeface(typeface);*/
		holder.option5.setTextColor(Color.BLACK);
		/*holder.option5.setTypeface(typeface);*/
		holder.im1.setVisibility(View.INVISIBLE);
		holder.im2.setVisibility(View.INVISIBLE);
		holder.im3.setVisibility(View.INVISIBLE);
		holder.im4.setVisibility(View.INVISIBLE);
		holder.im5.setVisibility(View.INVISIBLE);
		convertView.setVisibility(View.VISIBLE);
		Cursor question;
		if (isAntonym) {
			question = db.getRowById(Constants.TABLE_ANTONYM,questionlist.get(position));
		} else if (isAnalogy) {
			question = db.getRowById(Constants.TABLE_ANALOGIES,questionlist.get(position));			
		}else {
			question = db.getRowById(Constants.TABLE_SYNONYM,questionlist.get(position));
		}
		if (question.moveToFirst()) {
			holder.question_name.setText(question.getString(question.getColumnIndex(Constants.QUESTION_COLUMN)));
			/*holder.question_name.setTypeface(typeface);*/
			holder.question_number.setText((position + 1) + " of 10");
			/*holder.question_number.setTypeface(typeface);*/
			holder.option1.setText("1. "
					+ question.getString(question
							.getColumnIndex(Constants.ANSWER_OPTION_1)).trim());
			holder.option2.setText("2. "
					+ question.getString(question
							.getColumnIndex(Constants.ANSWER_OPTION_2)).trim());
			holder.option3.setText("3. "
					+ question.getString(question
							.getColumnIndex(Constants.ANSWER_OPTION_3)).trim());
			holder.option4.setText("4. "
					+ question.getString(question
							.getColumnIndex(Constants.ANSWER_OPTION_4)).trim());
			holder.option5.setText("5. "
					+ question.getString(question
							.getColumnIndex(Constants.ANSWER_OPTION_5)).trim());
			if(!isAnalogy)
			holder.Description.setText(question.getString(question.getColumnIndex(Constants.ANSWER_DESCRIPTION)).trim());
			
		}
		if (correctanswer == useranswer) {
			Log.d(TAG, "Ashish: when the answer is true;");
			TextView btn = (TextView) convertView
					.findViewById(button_list[correctanswer - 1]);
			btn.setTextColor(Color.GREEN);
			ImageView img = (ImageView) convertView
					.findViewById(check_list[correctanswer - 1]);
			img.setBackgroundResource(R.drawable.button_ok);
			img.setVisibility(View.VISIBLE);
		} else if (useranswer == -1) {
			Log.d(TAG, "Ashish: when user not give  answer");
			TextView btn = (TextView) convertView
					.findViewById(button_list[correctanswer - 1]);
			btn.setTextColor(Color.GREEN);

		} else {
			Log.d(TAG, "Ashish: when user give wrong answer");
			TextView btn = (TextView) convertView
					.findViewById(button_list[correctanswer - 1]);
			btn.setTextColor(Color.GREEN);
			ImageView img = (ImageView) convertView
					.findViewById(check_list[correctanswer - 1]);
			img.setBackgroundResource(R.drawable.button_ok);
			img.setVisibility(View.VISIBLE);

			TextView btn2 = (TextView) convertView
					.findViewById(button_list[useranswer - 1]);
			btn2.setTextColor(Color.RED);
			ImageView img1 = (ImageView) convertView
					.findViewById(check_list[useranswer - 1]);
			img1.setBackgroundResource(R.drawable.fileclose);
			img1.setVisibility(View.VISIBLE);

		}

		return convertView;
	}

	/*
	 * @Override public void bindView(View convertView, Context context, Cursor
	 * cursor) { // TODO Auto-generated method stub ViewHolder holder = new
	 * ViewHolder(); holder.question_name =
	 * (TextView)convertView.findViewById(R.id.Question_list);
	 * holder.question_number =
	 * (TextView)convertView.findViewById(R.id.question_number_list);
	 * holder.option1 =
	 * (TextView)convertView.findViewById(R.id.myRadioButton1_list);
	 * holder.option2 =
	 * (TextView)convertView.findViewById(R.id.myRadioButton2_list);
	 * holder.option3 =
	 * (TextView)convertView.findViewById(R.id.myRadioButton3_list);
	 * holder.option4 =
	 * (TextView)convertView.findViewById(R.id.myRadioButton4_list);
	 * holder.option5 =
	 * (TextView)convertView.findViewById(R.id.myRadioButton5_list);
	 * holder.Description =
	 * (TextView)convertView.findViewById(R.id.description_list);
	 * holder.Sentence =
	 * (TextView)convertView.findViewById(R.id.Senetence_list); holder.im1 =
	 * (ImageView)convertView.findViewById(R.id.myCheck1_list); holder.im2 =
	 * (ImageView)convertView.findViewById(R.id.myCheck2_list); holder.im3 =
	 * (ImageView)convertView.findViewById(R.id.myCheck3_list); holder.im4 =
	 * (ImageView)convertView.findViewById(R.id.myCheck4_list); holder.im5 =
	 * (ImageView)convertView.findViewById(R.id.myCheck5_list);
	 * 
	 * super.bindView(convertView, context, cursor); }
	 */

	public void updateButton(TextView b, String s, int questionnumber) {
		b.setText(questionnumber + ". " + s);
		b.setTextColor(Color.BLACK);
	}

	private static class ViewHolder {
		TextView question_name;
		TextView question_number;
		TextView option1;
		TextView option2;
		TextView option3;
		TextView option4;
		TextView option5;
		TextView Description;
		LinearLayout meaningLayout;
		ImageView im1, im2, im3, im4, im5;

	}

}
