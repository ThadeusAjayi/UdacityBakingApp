package com.shopspreeng.android.udacitybakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.AsyncTaskLoader;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.shopspreeng.android.udacitybakingapp.data.BakingContract;
import com.shopspreeng.android.udacitybakingapp.data.DataUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * helper methods.
 */
public class BakingDatabaseUpdateService extends IntentService {

    public static final String QUERY_ALL = "query_all";
    public static final String INSERT_RECIPE = "insert_recipe";

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

                ContentValues [] cv = DataUtils.getRecipeCvArray(recipes);

                int rows = getContentResolver().bulkInsert(BakingContract.BakingEntry.CONTENT_URI,cv);

                if(rows > 0){
                    Log.v("Bulk Insert successful", rows +" rows inserted");
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                    int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(),BakingAppWidgetProvider.class));

                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.list_view_widget_item);
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
