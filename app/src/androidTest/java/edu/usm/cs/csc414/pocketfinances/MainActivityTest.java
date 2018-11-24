package edu.usm.cs.csc414.pocketfinances;

import android.support.design.widget.BottomNavigationView;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.rule.ActivityTestRule;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MainActivity activity;
    private FrameLayout fragmentHolder;
    private BottomNavigationView bottomNavView;
    private AdView adView;
    private ImageView background;


    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activity = rule.getActivity();

        fragmentHolder = activity.findViewById(R.id.activity_main_framelayout);
        bottomNavView = activity.findViewById(R.id.bottom_nav_view);
        adView = activity.findViewById(R.id.adView);
        background = activity.findViewById(R.id.activity_main_background);
    }

    @Test
    public void testPreConditions() {
        assertNotNull(fragmentHolder);
        assertNotNull(bottomNavView);
        assertNotNull(adView);
        assertNotNull(background);
    }
}