package com.amaze.QuizActivity;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class CalculateScore 
{
	public int correctAnswers;
	public int wrongAnswers;
	public int unattempted;
	public int score;
	
	public CalculateScore() {
		this.correctAnswers = 0;
		this.wrongAnswers = 0;
		this.unattempted = 0;
		score = 0;
	}

	public void Calculate(HashMap<Integer,Integer> map,ArrayList<Integer> list)
	{
		for(int i=1;i<=map.size();i++)
		{
			if(map.get(i) == list.get(i-1))
			{
				
				correctAnswers++;
				Log.d("CalculateScore","The value of correct anser is:"+correctAnswers);
			}
			else if(map.get(i) == -1)
			{
				unattempted++;
				Log.d("CalculateScore","The value of ubattempted anser is:"+unattempted);
			}else
			{
				wrongAnswers++;
				Log.d("CalculateScore","The value of wrong anser is:"+wrongAnswers);
			}
		}
		score = ((correctAnswers * 2) - wrongAnswers);
		
	}
}
