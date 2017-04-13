package com.sonal4999.inventory2app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    static final String NAME = "inventory.db";

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + Database_Column.Inventory_table.TABLE_NAME + " (" +
                Database_Column.Inventory_table.DB_ID + " INTEGER PRIMARY KEY," +
                Database_Column.Inventory_table.DB_PRODUCT_NAME + " TEXT NOT NULL, " +
                Database_Column.Inventory_table.DB_QUANTITY + " INTEGER NOT NULL, " +
                Database_Column.Inventory_table.DB_PRICE + " INTEGER NOT NULL," +
                Database_Column.Inventory_table.DB_IMAGE + " BLOB NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Database_Column.Inventory_table.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Insert data in the table
    public boolean insertQuery(String productName, int quantity, int price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database_Column.Inventory_table.DB_PRODUCT_NAME, productName);
        contentValues.put(Database_Column.Inventory_table.DB_QUANTITY, quantity);
        contentValues.put(Database_Column.Inventory_table.DB_PRICE, price);
        contentValues.put(Database_Column.Inventory_table.DB_IMAGE, image);
        db.insert(Database_Column.Inventory_table.TABLE_NAME, null, contentValues);
        return true;
    }

    // Get data from the table
    public Cursor getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + Database_Column.Inventory_table.TABLE_NAME +
                " WHERE name=\"" + name + "\"", null);
        return res;
    }
    // Delete all entries
    public int delete_all() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Database_Column.Inventory_table.TABLE_NAME, null, null);
    }

    // Delete one  entry
    public boolean deleteQuery(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Database_Column.Inventory_table.TABLE_NAME, "name=?", new String[]{name}) > 0;
    }

    // Update data
    public void updateQuery(String name, int quantity, int change) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " + Database_Column.Inventory_table.TABLE_NAME + " SET quantity = "
                + (quantity + change) + " WHERE name = \"" + name + "\"";
        db.execSQL(strSQL);
    }

    public ArrayList<String> getDataList() {
        ArrayList<String> productList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur_List = db.rawQuery("SELECT * FROM " + Database_Column.Inventory_table.TABLE_NAME, null);
        cur_List.moveToFirst();
        while (cur_List.isAfterLast() == false) {
            String productName = cur_List.getString(cur_List.getColumnIndex(Database_Column.Inventory_table.DB_PRODUCT_NAME));
            int quantity = cur_List.getInt(cur_List.getColumnIndex(Database_Column.Inventory_table.DB_QUANTITY));
            int price = cur_List.getInt(cur_List.getColumnIndex(Database_Column.Inventory_table.DB_PRICE));
            productList.add(productName + "\n" + "Quantity: " + quantity + "\n" + "Price: $" + price);
            cur_List.moveToNext();
        }
        return productList;
    }
}


