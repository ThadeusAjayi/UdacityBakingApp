package com.shopspreeng.android.udacitybakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shopspreeng.android.udacitybakingapp.data.BakingContract;
import com.shopspreeng.android.udacitybakingapp.data.DataUtils;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static android.R.attr.visibility;

/**
 * Created by jayson surface on 27/06/2017.
 */

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }

    class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

        Context context;
        ArrayList<Step> steps;
        String recipe;
        Recipe recipes;

        public ListRemoteViewFactory(Context applicationContext){
            context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            recipe = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(getString(R.string.pref_default_key),getString(R.string.value));
            String [] selectionArgs = {recipe};
            Cursor mCursor = getContentResolver().query(BakingContract.BakingEntry.CONTENT_URI,null,
                                BakingContract.BakingEntry.RECIPE +"=?",selectionArgs,null);

            if(mCursor == null || mCursor.getCount() == 0){
                recipes = null;
                steps = new ArrayList<>();
            }else {

                mCursor.moveToFirst();

                recipes = DataUtils.cursorToArrayListRecipe(mCursor).get(0);

                String step = recipes.getmSteps();

                steps = DataUtils.extractStepsFromJson(step);
            }

        }

        @Override
        public void onDestroy() {}

        @Override
        public int getCount() {
            return steps.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.list_view_widget_item);
            if(steps == null || steps.size() == 0){
                itemView.setViewVisibility(R.id.empty_widget, View.VISIBLE);
                itemView.setTextViewText(R.id.empty_widget, getString(R.string.select_favorite));
                return itemView;
            }


            String step = steps.get(i).getShortDesc();

            itemView.setTextViewText(R.id.item_text, step);

            int position = i + 1;
            itemView.setTextViewText(R.id.item_serial, position + " >");

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(getString(R.string.name),recipe);
                fillInIntent.putExtra(getString(R.string.recipe_list),recipes);
                fillInIntent.putParcelableArrayListExtra(getString(R.string.steps), steps);
                fillInIntent.putExtra(getString(R.string.position), i);

            itemView.setOnClickFillInIntent(R.id.item_text, fillInIntent);

            return itemView;
        }


        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
