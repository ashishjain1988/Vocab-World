package com.amaze.QuizActivity.History;

import java.util.ArrayList;

import com.amaze.QuizActivity.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class History_Adapter extends BaseAdapter{

	ArrayList<Integer> score_history;
	ArrayList<String> name_history;
	Context context;
	public History_Adapter(Context context,ArrayList<Integer> score, ArrayList<String> name)
	{
		score_history = score;
		name_history = name;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return score_history.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = View.inflate(context, R.layout.history_item, null);
			holder = new ViewHolder();
			holder.serialno = (TextView)convertView.findViewById(R.id.serialnumber);
			holder.name = (TextView)convertView.findViewById(R.id.name_history);
			holder.score = (TextView)convertView.findViewById(R.id.score_show_history);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		Log.e("","Ashish: The value of buttons are holder.name: "+holder.name);
		if(name_history!=null || score_history!=null)
		{
			if(name_history.get(position)!=null && score_history.get(position)!=null)
			{
				holder.serialno.setText((position+1)+". ");
				holder.name.setText(name_history.get(position));
				holder.score.setText(score_history.get(position)+" Point");
			}

		}else
		{
			Log.d("","Ashish: In the else condition when no history");
			holder.serialno.setText("No History");
			holder.score.setText("No History");
		}
		return convertView;
	}
	private static class ViewHolder {
        TextView serialno;
        TextView name;
        TextView score;
        
    }

}
