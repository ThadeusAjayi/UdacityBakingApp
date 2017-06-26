package com.shopspreeng.android.udacitybakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jayson surface on 26/06/2017.
 */

public class BakingContract {

    public static final String AUTHORITY = "com.shopspreeng.android.udacitybakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String BAKING_PATH = "baking";

    private BakingContract(){}

    public static final class BakingEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(BAKING_PATH).build();

        public static final String TABLE_NAME = "baking";
        public static final String RECIPE = "recipe";
        public static final String INGREDIENTS = "ingredients";
        public static final String STEPS = "steps";

    }
}
