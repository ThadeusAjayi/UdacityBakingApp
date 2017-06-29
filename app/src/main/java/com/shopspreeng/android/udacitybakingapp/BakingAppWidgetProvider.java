package com.shopspreeng.android.udacitybakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.ui.DetailActivity;
import com.shopspreeng.android.udacitybakingapp.ui.MainActivity;
import com.shopspreeng.android.udacitybakingapp.ui.PhoneMediaPlayerActivity;

import static java.security.AccessController.getContext;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews views = getListViewRv(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    public static RemoteViews getListViewRv(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.baking_app_widget_provider);


        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.baking_list, intent);

        Intent appIntent;

        if(tabletSize){
            appIntent = new Intent(context,DetailActivity.class);
        }else {
            appIntent = new Intent(context,DetailActivity.class);
        }

        PendingIntent appPendingIntent = PendingIntent.getActivity(context,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.baking_list,appPendingIntent);

        return remoteViews;
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

