package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import timber.log.Timber;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    CustomSharedPreferences sharedPrefs;

    ImageView background;
    FrameLayout fragmentHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("Attempting to create SettingsActivity.");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Allow the app window to fill the entire screen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        background = findViewById(R.id.activity_settings_background);
        fragmentHolder = findViewById(R.id.activity_settings_fragmentholder);

        sharedPrefs = new CustomSharedPreferences(getApplicationContext());

        try {
            // Set chosen background from sharedPrefs
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }
        catch (Exception e) {
            sharedPrefs.setActivityBackground(Background.DARK_GREY);
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }

        setFragmentHolderPadding();


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(fragmentHolder.getId(), new SettingsMainFragment())
                .commit();
        }




    /**
     * Method for handling the window size for the activity.
     * The activity should span the entire screen, including the space behind any potential soft keys.
     * Therefore, we need to add bottom padding to the FragmentHolder so that it floats above the soft keys, not behind it.
     */
    private void setFragmentHolderPadding() {
        Timber.d("Checking for soft keys.");
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

    public void reloadActivityBackground() {
        try {
            // Set chosen background from sharedPrefs
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }
        catch (Exception e) {
            sharedPrefs.setActivityBackground(Background.DARK_GREY);
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }
    }
}
