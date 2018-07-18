package com.dicoding.kamusmade.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dicoding.kamusmade.Class.AppPreference;
import com.dicoding.kamusmade.Class.Kamus;
import com.dicoding.kamusmade.Database.KamusBuilder;
import com.dicoding.kamusmade.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity
{
	@BindView(R.id.progress_load)
	ProgressBar load_proses;

	@BindView(R.id.start_text)
	TextView text;

	private final static String TAG = "StartActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ButterKnife.bind(this);

		new LoadData().execute();
	}

	@SuppressLint("StaticFieldLeak")
	private class LoadData extends AsyncTask<Void, Integer, Void>
	{
		KamusBuilder kamusBuilder;
		AppPreference appPreference;
		double progress;
		double max_progress = 1000;

		@SuppressLint("SetTextI18n")
		@Override
		protected Void doInBackground(Void... voids)
		{
			Boolean first_run = appPreference.getFirstRun();

			if(first_run)
			{
				//Ambil data dari raw text
				ArrayList<Kamus> KamusIndo = startLoad(R.raw.indonesia_english);
				ArrayList<Kamus> KamusEng = startLoad(R.raw.english_indonesia);

				publishProgress((int) progress);

				try
				{
					kamusBuilder.open();
				}

				catch (SQLException e)
				{
					e.printStackTrace();
				}

				Double ProgressMaxInsert = 100.0;
				Double progressDiff = (ProgressMaxInsert - progress) / (KamusIndo.size() + KamusEng.size());

				kamusBuilder.InsertTransaction(KamusIndo, true);
				progress += progressDiff;
				publishProgress((int) progress);

				kamusBuilder.InsertTransaction(KamusEng, false);
				progress += progressDiff;
				publishProgress((int) progress);

				Log.d(TAG, "number of data received "+ KamusIndo.size() + KamusEng.size());

				kamusBuilder.close();
				appPreference.setFirstRun(false);
				publishProgress((int) max_progress);
			}
			else
			{
				text.setText("Welcome to Flash Dictionary");
				try
				{
					synchronized (this)
					{
						this.wait(1000);
						publishProgress(50);

						this.wait(300);
						publishProgress((int) max_progress);
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPreExecute()
		{
			kamusBuilder = new KamusBuilder(StartActivity.this);
			appPreference = new AppPreference(StartActivity.this);
		}

		@Override
		protected void onPostExecute(Void aVoid)
		{
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
			startActivity(intent);

			finish();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			load_proses.setProgress(values[0]);
		}

		@Override
		protected void onCancelled(Void aVoid)
		{
			super.onCancelled(aVoid);
		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}
	}

	private void LoadDummy()
	{
		final int countdown = 1000;
		load_proses.setMax(countdown);

		CountDownTimer countDownTimer = new CountDownTimer(countdown, countdown / 100)
		{
			@Override
			public void onTick(long millisUntilFinished)
			{
				load_proses.setProgress((int)(countdown - millisUntilFinished));
			}

			@Override
			public void onFinish()
			{
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		}.start();
	}

	public ArrayList<Kamus> startLoad(int data)
	{
		ArrayList<Kamus> kamusList = new ArrayList<>();
		BufferedReader reader;

		try
		{
			Resources res = getResources();
			InputStream raw_dict = res.openRawResource(data);

			reader = new BufferedReader(new InputStreamReader(raw_dict));
			String line = null;

			do
			{
				line  = reader.readLine();
				String[] splitstr = line.split("\t");
				Kamus kamus = new Kamus(splitstr[0], splitstr[1]);
				kamusList.add(kamus);
			}
			while(line != null);


		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return kamusList;
	}
}
