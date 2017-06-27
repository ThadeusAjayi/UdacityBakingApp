package com.shopspreeng.android.udacitybakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.shopspreeng.android.udacitybakingapp.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jayson surface on 27/06/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


}
