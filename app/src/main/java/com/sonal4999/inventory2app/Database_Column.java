package com.sonal4999.inventory2app;


import android.provider.BaseColumns;
import android.provider.SyncStateContract;

public class Database_Column {

    public static final class Inventory_table implements BaseColumns {

        public static final String DB_ID = "id";
        public static final String TABLE_NAME = "inventory";
        public static final String DB_PRODUCT_NAME = "name";
        public static final String DB_QUANTITY = "quantity";
        public static final String DB_IMAGE ="image";
        public static final String DB_PRICE = "price";

    }
}
