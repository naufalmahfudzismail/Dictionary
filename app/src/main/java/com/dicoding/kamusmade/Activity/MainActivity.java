package com.dicoding.kamusmade.Activity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dicoding.kamusmade.Adapter.RecycleSearchAdapter;
import com.dicoding.kamusmade.Class.Kamus;
import com.dicoding.kamusmade.Database.KamusBuilder;
import com.dicoding.kamusmade.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener
{
	@BindView(R.id.rcy_search)
	RecyclerView rcy_search;

	@BindView(R.id.search_bar)
	MaterialSearchBar search_bar;

	@BindView(R.id.drawer_layout)
	DrawerLayout drawer;

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.nav_view)
	NavigationView navigationView;

	private static final String TAG = "MainActivity";

	private KamusBuilder kamusBuilder;
	private RecycleSearchAdapter adapter;

	private ArrayList<Kamus> kamus_list = new ArrayList<>();
	private boolean isIndo = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener(this);
		search_bar.setOnSearchActionListener(this);
		kamusBuilder = new KamusBuilder(this);

		create_list();
		load_data();


		Log.d(TAG, "number of dictinoary "+ kamus_list.size());

		navigationView.getMenu().getItem(1).setChecked(true);

	}

	private void create_list()
	{
		adapter = new RecycleSearchAdapter();
		rcy_search.setLayoutManager(new LinearLayoutManager(this));
		rcy_search.setAdapter(adapter);
	}

	private void load_data(String search)
	{
		try
		{
			kamusBuilder.open();
			if(search.isEmpty()) kamus_list = kamusBuilder.getAllData(isIndo);
			else kamus_list = kamusBuilder.getDataByText(search, isIndo);

			if(isIndo)
				getSupportActionBar().setSubtitle(getResources().getString(R.string.indonesia_english));
			else getSupportActionBar().setSubtitle(getResources().getString(R.string.english_indonesia));

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			kamusBuilder.close();
		}

		adapter.replaceAll(kamus_list);
	}

	private void load_data()
	{
		load_data("");
	}

	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		} else
		{
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		return id == R.id.action_settings || super.onOptionsItemSelected(item);

	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_e_i)
		{
			isIndo = false;
			load_data();
		}
		if (id == R.id.nav_i_e)
		{
			isIndo = true;
			load_data();
			//handle it
		}

		if(id == R.id.nav_coming_jawa || id == R.id.nav_coming_belanda)
		{
			Toast.makeText(getApplicationContext(), "Coming Soon !",Toast.LENGTH_SHORT).show();
		}

		if (id == R.id.nav_share) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\n\n" + getString(R.string.share_description));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
		}
			drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	@Override
	public void onSearchStateChanged(boolean enabled)
	{

	}

	@Override
	public void onSearchConfirmed(CharSequence text)
	{
		load_data(String.valueOf(text));
	}

	@Override
	public void onButtonClicked(int buttonCode)
	{

	}
}
