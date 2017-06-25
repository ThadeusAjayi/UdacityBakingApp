package com.shopspreeng.android.udacitybakingapp;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shopspreeng.android.udacitybakingapp.data.BakingContract;
import com.shopspreeng.android.udacitybakingapp.data.BakingDbHelper;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BakingDatabaseUpdateService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.shopspreeng.android.udacitybakingapp.action.FOO";
    private static final String ACTION_BAZ = "com.shopspreeng.android.udacitybakingapp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.shopspreeng.android.udacitybakingapp.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.shopspreeng.android.udacitybakingapp.extra.PARAM2";
    public static final String UPDATE_DB = "update-db";

    SQLiteDatabase mDb;

    public BakingDatabaseUpdateService() {
        super("BakingDatabaseUpdateService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BakingDatabaseUpdateService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BakingDatabaseUpdateService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_DB.equals(action)) {
                final ArrayList<Recipe> param1 = intent.getParcelableArrayListExtra(getString(R.string.recipe_list));
                insertRecipeToDb(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void insertRecipeToDb(ArrayList<Recipe> param1) {
        // TODO: Handle action Foo

        ContentValues cv = new ContentValues();
        List<ContentValues> list = new ArrayList<>();

        BakingDbHelper dbHelper = new BakingDbHelper(getApplicationContext());
        mDb = dbHelper.getWritableDatabase();

        for(Recipe recipe : param1){
            cv.put(BakingContract.BakingEntry.COLUMN_NAME,recipe.getmName());
            cv.put(BakingContract.BakingEntry.COLUMN_SERVING,recipe.getmServings());
            list.add(cv);
        }

        Log.v("list size", String.valueOf(list.size()));

        Cursor result = mDb.query(BakingContract.BakingEntry.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            BakingContract.BakingEntry._ID);

        if(result.getCount() == 0) {

            for (ContentValues c : list) {
                mDb.insert(BakingContract.BakingEntry.TABLE_NAME,
                        null,
                        c);
            }
        }else {
            Log.v("query size", String.valueOf(result.getCount()));
        }


        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
