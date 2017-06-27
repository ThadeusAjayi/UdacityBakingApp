package com.shopspreeng.android.udacitybakingapp;

import android.app.IntentService;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shopspreeng.android.udacitybakingapp.data.BakingContract;
import com.shopspreeng.android.udacitybakingapp.data.DatabaseUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BakingDatabaseUpdateService extends IntentService {

    public static final String TAG = BakingDatabaseUpdateService.class.getSimpleName();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.shopspreeng.android.udacitybakingapp.action.FOO";
    private static final String ACTION_BAZ = "com.shopspreeng.android.udacitybakingapp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.shopspreeng.android.udacitybakingapp.extra.PARAM1";
    public static final String QUERY_ALL = "query_all";
    public static final String INSERT_RECIPE = "insert_recipe";

    DatabaseUtils dbUtils = new DatabaseUtils();


    public BakingDatabaseUpdateService() {
        super("BakingDatabaseUpdateService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (INSERT_RECIPE.equals(action)) {
                ArrayList<Recipe> recipes = intent.getParcelableArrayListExtra(getString(R.string.recipe_list));
                insertAllrecipe(recipes);
            }else if(QUERY_ALL.equals(action)){
                queryAllRecipe();
            }
        }
    }

    private void insertAllrecipe(final ArrayList<Recipe> recipes) {

        new AsyncTaskLoader<Void>(getApplicationContext()) {
            @Override
            public Void loadInBackground() {
//not working yet
               /* getContentResolver().delete(BakingContract.BakingEntry.CONTENT_URI,null,null);*/

                ContentValues [] cv = dbUtils.getRecipeCvArray(recipes);

                int rows = getContentResolver().bulkInsert(BakingContract.BakingEntry.CONTENT_URI,cv);

                if(rows > 0){
                    Log.v("Bulk Insert successful", rows +" rows inserted");
                }else {
                    throw new UnsupportedOperationException("Not yet implemented");
                }
                return null;
            }
        }.forceLoad();

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private Cursor queryAllRecipe() {

        return  getContentResolver().query(BakingContract.BakingEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

    }
}
