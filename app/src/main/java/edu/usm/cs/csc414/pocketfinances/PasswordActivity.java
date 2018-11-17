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

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;


public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivity";

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

        fragmentHolder = findViewById(R.id.activity_password_framelayout);
        background = findViewById(R.id.activity_password_background);

        background.setImageResource(new CustomSharedPreferences(getApplicationContext()).getActivityBackground());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setNavMenuPadding();

        // Load the home fragment into the frame layout in the UI
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

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


    private void setNavMenuPadding() {
        Log.v(TAG, "Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Log.d(TAG, "Soft key bar height: " + softKeyBarHeight);

            fragmentHolder.setPadding(0,0,0, softKeyBarHeight);

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
