package com.dicoding.kamusmade.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Kamus implements Parcelable
{
	private int id;
	private String word;
	private String translate;

	public Kamus()
	{
	}

	public Kamus(String word, String translate)
	{
		this.word = word;
		this.translate = translate;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getWord()
	{
		return word;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

	public String getTranslate()
	{
		return translate;
	}

	public void setTranslate(String translate)
	{
		this.translate = translate;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(this.id);
		dest.writeString(this.word);
		dest.writeString(this.translate);
	}

	protected Kamus(Parcel in)
	{
		this.id = in.readInt();
		this.word = in.readString();
		this.translate = in.readString();
	}

	public static final Parcelable.Creator<Kamus> CREATOR = new Parcelable.Creator<Kamus>()
	{
		@Override
		public Kamus createFromParcel(Parcel source)
		{
			return new Kamus(source);
		}

		@Override
		public Kamus[] newArray(int size)
		{
			return new Kamus[size];
		}
	};
}
