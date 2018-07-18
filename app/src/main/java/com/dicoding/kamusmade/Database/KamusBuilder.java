package com.dicoding.kamusmade.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.dicoding.kamusmade.Class.Kamus;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.ArrayList;


public class KamusBuilder
{
	private static String dict_English = DatabaseBuilder.ENGLISH_TABLE;
	private static String dict_Indonesia = DatabaseBuilder.INDO_TABLE;

	private Context context;
	private DatabaseBuilder db_builder;
	private SQLiteDatabase database;


	public KamusBuilder(Context context)
	{
		this.context = context;
	}

	public KamusBuilder open() throws SQLException
	{
		db_builder = new DatabaseBuilder(context);
		database = db_builder.getWritableDatabase();
		return this;
	}

	public void close()
	{
		db_builder.close();
	}

	private Cursor SearchQueryByTextInput(String Query, boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		return database.rawQuery("SELECT * FROM " + DB_TABLE +
				" WHERE " + DatabaseBuilder.WORD_COLUMN + " LIKE '%" + Query.trim() + "%'", null);
	}

	public ArrayList<Kamus> getDataByText(String SearchText, boolean isIndo)
	{
		Kamus kamus;
		 ArrayList<Kamus>dict_list = new ArrayList<>();
		 Cursor cursor = SearchQueryByTextInput(SearchText, isIndo);

		 cursor.moveToFirst();

		 if(cursor.getCount() > 0)
		 {
		 	do
		    {
				kamus = new Kamus();
				kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseBuilder.ID_COLUMN)));
				kamus.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseBuilder.TRANSLATE_COLUMN)));
				kamus.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseBuilder.WORD_COLUMN)));
				dict_list.add(kamus);
				cursor.moveToNext();

		    }
		    while(!cursor.isAfterLast());
		 }
		 cursor.close();
		 return dict_list;
	}

	public ArrayList<Kamus> getAllData(boolean isIndo)
	{
		Kamus kamus;

		ArrayList<Kamus> arrayList = new ArrayList<>();
		Cursor cursor = queryAllData(isIndo);

		cursor.moveToFirst();
		if (cursor.getCount() > 0)
		{
			do
			{
				kamus = new Kamus();
				kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseBuilder.ID_COLUMN)));
				kamus.setWord(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseBuilder.WORD_COLUMN)));
				kamus.setTranslate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseBuilder.TRANSLATE_COLUMN)));
				arrayList.add(kamus);

				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		cursor.close();
		return arrayList;
	}

	private Cursor queryAllData(boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		return database.rawQuery("SELECT * FROM " + DB_TABLE + " ORDER BY " +
				DatabaseBuilder.ID_COLUMN + " ASC", null);
	}

	public String getData(String search, boolean isIndo)
	{
		String result = "";
		Cursor cursor = SearchQueryByTextInput(search, isIndo);
		cursor.moveToFirst();
		if (cursor.getCount() > 0)
		{
			result = cursor.getString(2);
			for (; !cursor.isAfterLast(); cursor.moveToNext())
			{
				result = cursor.getString(2);
			}
		}
		cursor.close();
		return result;
	}

	public long insert(Kamus kamus, boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		ContentValues contentValues = new ContentValues();
		contentValues.put(DatabaseBuilder.WORD_COLUMN, kamus.getWord());
		contentValues.put(DatabaseBuilder.TRANSLATE_COLUMN,  kamus.getTranslate());
		return database.insert(DB_TABLE, null, contentValues);
	}

	public void InsertTransaction(ArrayList<Kamus> kamusArrayList, boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		String sql = "INSERT INTO " +  DB_TABLE + " (" +
				DatabaseBuilder.WORD_COLUMN + ", " +
				DatabaseBuilder.TRANSLATE_COLUMN + ") VALUES (?, ?)";
		database.beginTransaction();

		SQLiteStatement  sqLiteStatement = database.compileStatement(sql);

		for ( int i = 0 ; i <kamusArrayList.size(); i++)
		{
			sqLiteStatement.bindString(1, kamusArrayList.get(i).getWord());
			sqLiteStatement.bindString(2, kamusArrayList.get(i).getTranslate());
			sqLiteStatement.execute();
			sqLiteStatement.clearBindings();
		}

		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void Update(Kamus kamus, boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		ContentValues contentValues = new ContentValues();
		contentValues.put(DatabaseBuilder.WORD_COLUMN, kamus.getWord());
		contentValues.put(DatabaseBuilder.TRANSLATE_COLUMN,  kamus.getTranslate());
		database.update(DB_TABLE, contentValues,
				DatabaseBuilder.ID_COLUMN + "= '" + kamus.getId() +"'",
				null);

	}

	public void Delete(int id, boolean isIndo)
	{
		String DB_TABLE;
		if(isIndo)DB_TABLE = dict_Indonesia;
		else DB_TABLE = dict_English;

		database.delete(DB_TABLE,
				DatabaseBuilder.ID_COLUMN+ " = '" + id + "'", null );
	}

}
