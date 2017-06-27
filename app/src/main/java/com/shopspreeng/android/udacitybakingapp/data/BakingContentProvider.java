package com.shopspreeng.android.udacitybakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.R.attr.defaultHeight;
import static android.R.attr.id;
import static com.shopspreeng.android.udacitybakingapp.data.BakingContract.BakingEntry.TABLE_NAME;

/**
 * Created by jayson surface on 26/06/2017.
 */

public class BakingContentProvider extends ContentProvider {

    private BakingDbHelper mBakingDbHelper;

    public static final int BAKING = 100;
    public static final int BAKING_WITH_ID = 101;
    public static final int BAKING_WITH_RECIPE = 102;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //whole directory
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.BAKING_PATH,BAKING);

        //single item with id
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.BAKING_PATH + "/#",BAKING_WITH_ID);

        //single item with recipename
        uriMatcher.addURI(BakingContract.AUTHORITY,BakingContract.BAKING_PATH + "/*", BAKING_WITH_RECIPE);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mBakingDbHelper = new BakingDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mBakingDbHelper.getReadableDatabase();

        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case BAKING:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    //content values here should contain string of recipe
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case BAKING:
                long id = db.insert(TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {


        throw new UnsupportedOperationException("Unknown uri: " + uri);

    }
    //content values here should contain string of steps and ingredients
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        int id;

        switch (sUriMatcher.match(uri)) {

            case BAKING_WITH_ID:
                id = db.update(TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return id;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case BAKING:
                db.beginTransaction();
                int rowInserted = 0;

                try{
                    for(ContentValues value : values){
                        long _id = db.insert(BakingContract.BakingEntry.TABLE_NAME,null,value);
                        if(_id > -1){
                            rowInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if(rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowInserted;
            default:
                return super.bulkInsert(uri,values);
        }

    }
}
