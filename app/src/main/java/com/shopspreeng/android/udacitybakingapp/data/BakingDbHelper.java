package com.shopspreeng.android.udacitybakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by jayson surface on 26/06/2017.
 */

public class BakingDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "baking_db";

    public static final int VERSION = 1;

    final String CREATE_BAKING_TABLE = "CREATE TABLE " +
            BakingContract.BakingEntry.TABLE_NAME + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            BakingContract.BakingEntry.RECIPE + " TEXT NOT NULL,"+
            BakingContract.BakingEntry.INGREDIENTS + " TEXT,"+
            BakingContract.BakingEntry.STEPS + " TEXT" +
            ");";

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BAKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
