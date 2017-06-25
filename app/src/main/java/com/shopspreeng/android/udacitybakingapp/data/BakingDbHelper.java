package com.shopspreeng.android.udacitybakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by jayson surface on 25/06/2017.
 */

public class BakingDbHelper extends SQLiteOpenHelper {

    private static int dbVersion = 1;

    private static final String DATABASE_NAME = "baking.db";

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " +
                BakingContract.BakingEntry.TABLE_NAME + "(" +
                BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.BakingEntry.COLUMN_NAME + " TEXT NOT NULL," +
                BakingContract.BakingEntry.COLUMN_SERVING + " INTEGER NOT NULL," +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
