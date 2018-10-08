package edu.usm.cs.csc414.pocketfinances;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


// Entry point activity for application.
// Will load most fragments from here.
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Declare UI elements
    FragmentTransaction fragmentTransaction;
    FrameLayout fragmentHolder;
    BottomNavigationView bottomNavView;

    // Declare flag for tracking the active fragment
    private int activeFragmentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fragmentHolder = findViewById(R.id.activity_main_framelayout);
        bottomNavView = findViewById(R.id.bottom_nav_view);

        // Set bottom padding to the layout so that any present soft keys don't overlap the nav bar
        setNavMenuPadding();

        // Load the home fragment into the frame layout in the UI
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(fragmentHolder.getId(), new HomeFragment())
                .commit();
        bottomNavView.setSelectedItemId(R.id.nav_home);
        activeFragmentId = R.id.nav_home;

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                    case R.id.nav_subscriptions:
                        if (R.id.nav_subscriptions != activeFragmentId) {
                            // Launch new fragment
                        }
                        return true;
                    case R.id.nav_budget:
                        if (R.id.nav_budget != activeFragmentId) {
                            // Launch new fragment
                        }
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    private void setNavMenuPadding() {
        Log.v(TAG, "Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Log.d(TAG, "Soft key bar height: " + softKeyBarHeight);

            bottomNavView.setPadding(0,0,0, softKeyBarHeight);

        } catch(Exception e) {
            Log.e(TAG, "Error in checking presence of soft keys and adapting UI accordingly.", e);
        }
    }


    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        return realHeight - usableHeight;
    }

}
