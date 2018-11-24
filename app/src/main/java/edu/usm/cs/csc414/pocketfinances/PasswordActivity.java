package edu.usm.cs.csc414.pocketfinances;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;


public class PasswordActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;

    // Declare Ui elements
        FrameLayout fragmentHolder;
        ImageView background;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirect user to different starting activity if necessary
        handleStartingActivity();

        setContentView(R.layout.activity_password);

        // Initialize UI elements
        fragmentHolder = findViewById(R.id.activity_password_framelayout);
        background = findViewById(R.id.activity_password_background);

        try {
            // Set chosen background from sharedPrefs
            background.setImageResource(new CustomSharedPreferences(getApplicationContext()).getActivityBackground());
        }
        catch (Exception e) {
            new CustomSharedPreferences(getApplicationContext()).setActivityBackground(CustomSharedPreferences.BACKGROUND_LIGHTBLUE);
            background.setImageResource(new CustomSharedPreferences(getApplicationContext()).getActivityBackground());
        }

        // Allow the app window to fill the entire screen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Set bottom padding to the layout so that any present soft keys don't overlap the fragment holder
        setFragmentHolderPadding();

        // Load the home fragment into the frame layout in the UI
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // Load the home fragment into the frame layout in the UI
        if (new CustomSharedPreferences(getApplicationContext()).getFingerprintEnabled()) {
            fragmentTransaction
                    .replace(fragmentHolder.getId(), new FingerprintFragment())
                    .commit();
        }
        else {
            fragmentTransaction
                    .replace(fragmentHolder.getId(), new PasswordFragment())
                    .commit();
        }
    }


    /**
     * Method for handling the window size for the activity.
     * The activity should span the entire screen, including the space behind any potential soft keys.
     * Therefore, we need to add bottom padding to the fragmentHolder so that it floats above the soft keys, not behind it.
     */
    private void setFragmentHolderPadding() {
        Timber.v("Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Timber.d("Soft key bar height: %d", softKeyBarHeight);

            fragmentHolder.setPadding(0,0,0, softKeyBarHeight);

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

    private void handleStartingActivity() {


        //------------------------ INIT SHARED PREFERENCES -----------------------------
        // declare and initialize shared preferences
        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(this);

        if (sharedPrefs.getIsFirstRun()) {
            // Launch WelcomeActivity, which walks the user through the app and allows them to set up basic settings/features
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            this.finish();
        }
        else if (!sharedPrefs.getPasswordEnabled()) {
            // Launch MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
