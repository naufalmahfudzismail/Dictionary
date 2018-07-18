package com.dicoding.kamusmade.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dicoding.kamusmade.Activity.DetailAcitivity;
import com.dicoding.kamusmade.Class.Kamus;
import com.dicoding.kamusmade.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleSearchAdapter extends RecyclerView.Adapter<RecycleSearchAdapter.SearchViewHolder>
{

	private ArrayList<Kamus> kamus_list = new ArrayList<>();
	private static Kamus kamus;

	public static Kamus getKamus()
	{
		return kamus;
	}

	public static void setKamus(Kamus kamus)
	{
		RecycleSearchAdapter.kamus = kamus;
	}

	public ArrayList<Kamus> getKamus_list()
	{
		return kamus_list;
	}

	public void setKamus_list(ArrayList<Kamus> kamus_list)
	{
		this.kamus_list = kamus_list;
	}

	public void replaceAll(ArrayList<Kamus> kamuses)
	{
		kamus_list = kamuses;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public RecycleSearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcy_item, parent, false);
		return new SearchViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull SearchViewHolder holder, int position)
	{
		holder.bind_kamus(kamus_list.get(position));
	}

	@Override
	public int getItemCount()
	{
		return kamus_list.size();
	}

	static class SearchViewHolder extends RecyclerView.ViewHolder
	{
		@BindView(R.id.dict_word)
		TextView detail_word;

		@BindView(R.id.dict_translate)
		TextView detail_translate;

		SearchViewHolder(View itemView)
		{
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		void bind_kamus(final Kamus kamus)
		{
			detail_word.setText(kamus.getWord());
			detail_translate.setText(kamus.getTranslate());

			itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					setKamus(kamus);
					Intent intent = new Intent(itemView.getContext(), DetailAcitivity.class);
					itemView.getContext().startActivity(intent);
				}
			});
		}

	}
}
