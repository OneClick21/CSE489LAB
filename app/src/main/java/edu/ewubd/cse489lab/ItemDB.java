package edu.ewubd.cse489lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDB extends SQLiteOpenHelper {

	public ItemDB(Context context) {
		super(context, "ItemDB.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("DB@OnCreate");
		String sql = "CREATE TABLE items  ("
								+ "ID TEXT PRIMARY KEY,"
								+ "itemName TEXT,"
								+ "Cost REAL,"
								+ "Date INTEGER"
				                + ")";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
		// db.execSQL("ALTER table my_table  ......");
	}
	public void insertItem(String ID, String itemName, double Cost, long Date) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("ID", ID);
		cols.put("itemName", itemName);
		cols.put("Cost", Cost);
		cols.put("Date", Date);

		db.insert("items", null ,  cols);
		db.close();
	}
	public void updateItem(String ID, String itemName, double Cost, long Date) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("ID", ID);
		cols.put("itemName", itemName);
		cols.put("Cost", Cost);
		cols.put("Date", Date);
  		db.update("items", cols, "ID=?", new String[ ] {ID} );
		db.close();
	}
	public void deleteItem(String ID) {
		SQLiteDatabase db = this.getWritableDatabase();
  		db.delete("items", "ID=?", new String[ ] {ID} );
		db.close();
	}
	public Cursor selectItems(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res=null;
		try {
			res = db.rawQuery(query, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}