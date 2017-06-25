package com.shopspreeng.android.udacitybakingapp.data;

import android.provider.BaseColumns;

/**
 * Created by jayson surface on 25/06/2017.
 */

public class BakingContract {

    public static final class BakingEntry implements BaseColumns{
        public static final String TABLE_NAME = "baking";
        public static final String COLUMN_NAME = "recipeName";
        public static final String COLUMN_SERVING = "serving";
    }
}
