package edu.usm.cs.csc414.pocketfinances;


import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    private static final String AD_UNIT_ID = "ca-app-pub-4305516840079041/3998872493";

    private CustomSharedPreferences sharedPrefs;

    // Declare UI elements
    FragmentTransaction fragmentTransaction;
    FrameLayout fragmentHolder;
    BottomNavigationView bottomNavView;
    AdView adView;
    ImageView background;

    // Declare flag for tracking the active fragment
    private int activeFragmentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = new CustomSharedPreferences(getApplicationContext());

        MobileAds.initialize(this, AD_UNIT_ID);

        // Add any new expenses in the background
        new AddRecurringExpensesTask(this).execute();

        // Allow the app window to fill the entire screen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize UI elements
        fragmentHolder = findViewById(R.id.activity_main_framelayout);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        adView = findViewById(R.id.adView);
        background = findViewById(R.id.activity_main_background);

        try {
            // Set chosen background from sharedPrefs
            background.setImageResource(sharedPrefs.getActivityBackground());
        }
        catch (Exception e) {
            sharedPrefs.setActivityBackground(CustomSharedPreferences.BACKGROUND_DARKGREY);
            background.setImageResource(sharedPrefs.getActivityBackground());
        }

        // Set bottom padding to the layout so that any present soft keys don't overlap the nav bar
        setNavMenuPadding();

        // Build the ad request
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Load ads into the adView
        adView.loadAd(adRequest);

        // Load the home fragment into the frame layout in the UI
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(fragmentHolder.getId(), new HomeFragment())
                .commit();
        bottomNavView.setSelectedItemId(R.id.nav_home);
        activeFragmentId = R.id.nav_home;

        // Set listener for handling clicks on the bottomNavView
        bottomNavView.setOnNavigationItemSelectedListener(item -> {

            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.nav_home:
                    if (R.id.nav_home != activeFragmentId) {
                        fragmentTransaction
                                .setCustomAnimations(R.animator.slide_right_left_in, R.animator.slide_right_left_out)
                                .replace(fragmentHolder.getId(), new HomeFragment(), "HomeFragment")
                                .commit();
                        activeFragmentId = R.id.nav_home;
                    }
                    return true;
                case R.id.nav_accounts:
                    if (R.id.nav_accounts != activeFragmentId) {

                        if (activeFragmentId == R.id.nav_home)
                            fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                        else
                            fragmentTransaction.setCustomAnimations(R.animator.slide_right_left_in, R.animator.slide_right_left_out);

                        fragmentTransaction
                                .replace(fragmentHolder.getId(), new AccountsFragment(), "AccountsFragment")
                                .addToBackStack(null)
                                .commit();
                        activeFragmentId = R.id.nav_accounts;
                    }
                    return true;
                case R.id.nav_recurringexpenses:
                    if (R.id.nav_recurringexpenses != activeFragmentId) {

                        if (activeFragmentId == R.id.nav_budget)
                            fragmentTransaction.setCustomAnimations(R.animator.slide_right_left_in, R.animator.slide_right_left_out);
                        else
                            fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);

                        fragmentTransaction
                                .replace(fragmentHolder.getId(), new RecurringExpensesFragment(), "RecurringExpensesFragment")
                                .addToBackStack(null)
                                .commit();
                        activeFragmentId = R.id.nav_recurringexpenses;
                    }
                    return true;
                case R.id.nav_budget:
                    if (R.id.nav_budget != activeFragmentId) {

                        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);

                        /*
                        fragmentTransaction
                                .replace(fragmentHolder.getId(), new BudgetFragment(), "BudgetFragment")
                                .addToBackStack(null)
                                .commit();

                        */

                        fragmentTransaction
                                .replace(fragmentHolder.getId(), new ViewPagerTestFragment(), "ViewPagerTestFragment")
                                .addToBackStack(null)
                                .commit();

                        activeFragmentId = R.id.nav_budget;

                    }
                    return true;
            }
            return false;
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }


    /**
     * Method for handling the window size for the activity.
     * The activity should span the entire screen, including the space behind any potential soft keys.
     * Therefore, we need to add bottom padding to the navBar so that it floats above the soft keys, not behind it.
     */
    private void setNavMenuPadding() {
        Timber.d("Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Timber.d("Soft key bar height: %d", softKeyBarHeight);

            bottomNavView.setPadding(0,0,0, softKeyBarHeight);

        } catch(Exception e) {
            Timber.e(e, "Error in checking presence of soft keys and adapting UI accordingly.");
        }
    }


    /**
     * Method for getting the height of the soft key bar, if it exists on the device.
     *
     * @return The height in pixels of soft key bar, if present.  Otherwise, returns 0.
     */
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        return realHeight - usableHeight;
    }

}
