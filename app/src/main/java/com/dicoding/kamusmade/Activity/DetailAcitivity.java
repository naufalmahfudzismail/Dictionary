package com.dicoding.kamusmade.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dicoding.kamusmade.Adapter.RecycleSearchAdapter;
import com.dicoding.kamusmade.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailAcitivity extends AppCompatActivity
{

	@BindView(R.id.detail_word)
	TextView detail_word;

	@BindView(R.id.detail_translate)
	TextView detail_translate;

	public static String newline = System.getProperty("line.separator");

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ButterKnife.bind(this);

		String word = RecycleSearchAdapter.getKamus().getWord();
		String translate = RecycleSearchAdapter.getKamus().getTranslate();

		translate = splitTranslate(translate);

		detail_word.setText(word);
		detail_translate.setText(translate);
	}

	private String splitTranslate(String translate)
	{
		String[] splitStr = translate.split("\\.");
		StringBuilder transform = null;

		int count = splitStr.length;

		for (int i = 0; i < count; i++)
		{
			if( i < 1)
			{
				transform = new StringBuilder(splitStr[i]);
			}
			else
			{
				transform.append(newline).append(splitStr[i]);
			}
		}

		return String.valueOf(transform);
	}
}
