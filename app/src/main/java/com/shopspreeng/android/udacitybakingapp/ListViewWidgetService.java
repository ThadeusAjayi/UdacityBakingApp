package com.shopspreeng.android.udacitybakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shopspreeng.android.udacitybakingapp.data.BakingContract;

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
        Cursor mCursor;

        public ListRemoteViewFactory(Context applicationContext){
            context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mCursor = getContentResolver().query(BakingContract.BakingEntry.CONTENT_URI,null,null,null,null);
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            mCursor.moveToPosition(i);
            String recipeName = mCursor.getString(mCursor.getColumnIndex(BakingContract.BakingEntry.RECIPE));
            String id = mCursor.getString(mCursor.getColumnIndex(BakingContract.BakingEntry._ID));

            RemoteViews itemView = new RemoteViews(context.getPackageName(), R.layout.list_view_widget_item);

            itemView.setTextViewText(R.id.item_text,recipeName);
            itemView.setImageViewResource(R.id.item_image,R.drawable.brownies);

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
