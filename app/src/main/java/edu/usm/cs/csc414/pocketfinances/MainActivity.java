package edu.usm.cs.csc414.pocketfinances;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;



// Entry point activity for application.
// Will load most fragments from here.
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int BOTTOM_NAV_HEIGHT_DP = 60;

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

        fragmentHolder = findViewById(R.id.activity_main_framelayout);
        bottomNavView = findViewById(R.id.bottom_nav_view);

        // Make phone status bar transparent if API level >= KitKat (19)
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            // but this also messes with the soft key bar
            // So we need to check for this and adjust the UI accordingly
            setNavMenuPadding();
        }
        */

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
                                    .replace(fragmentHolder.getId(), new HomeFragment())
                                    .commit();
                            activeFragmentId = R.id.nav_home;
                        }
                        return true;
                    case R.id.nav_accounts:
                        if (R.id.nav_accounts != activeFragmentId) {
                            fragmentTransaction
                                    .replace(fragmentHolder.getId(), new AccountsFragment())
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

    // TODO:  Need to fix this so that it doesn't throw ClassCastException.
    private void setNavMenuPadding() {
        Log.v(TAG, "Checking for soft keys.");
        try {
            boolean hasMenuKey = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            int softKeyBarSize = getNavigationBarSize(getApplicationContext()).y;

            if ((!hasMenuKey && !hasBackKey) || softKeyBarSize > 0) {
                // Device has soft key navigation bar
                Log.i(TAG, "Device soft keys detected. Attempting to adapt UI to fit soft keys.");

                // We need to adjust the layout so that the soft key bar doesn't cover up part of the UI
                BottomNavigationView.LayoutParams layoutParams = new BottomNavigationView.LayoutParams(
                        BottomNavigationView.LayoutParams.MATCH_PARENT, dpToPx(BOTTOM_NAV_HEIGHT_DP));

                layoutParams.setMargins(0,0,0,softKeyBarSize);

                bottomNavView.setLayoutParams(layoutParams);

            }
        } catch(Exception e) {
            Log.e(TAG, "Error in checking presence of soft keys and adapting UI accordingly.", e);
        }
    }


    private int dpToPx(int dp) {
        // Converts 14 dip into its equivalent px
        Resources r = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }


    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the side
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
                Log.e(TAG, "Exception in getRealScreenSize method.", e);
            }
        }

        return size;
    }

}
