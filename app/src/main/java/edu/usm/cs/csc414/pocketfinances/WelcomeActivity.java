package edu.usm.cs.csc414.pocketfinances;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
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
import android.widget.TextView;

import timber.log.Timber;

@SuppressWarnings("deprecation")
public class WelcomeActivity extends AppCompatActivity {

    FrameLayout fragmentHolder;
    TextView nextButton;
    FragmentTransaction fragmentTransaction;
    ImageView background;

    private int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fragmentHolder = findViewById(R.id.activity_welcome_framelayout);
        nextButton = findViewById(R.id.activity_welcome_next_textview);
        background = findViewById(R.id.activity_welcome_background);

        new CustomSharedPreferences(getApplicationContext()).setActivityBackground(CustomSharedPreferences.BACKGROUND_DARKGREY);
        background.setImageResource(new CustomSharedPreferences(getApplicationContext()).getActivityBackground());

        // Set bottom padding to the layout so that any present soft keys don't overlap the nav bar
        setBottomPadding();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(fragmentHolder.getId(), new WelcomeFragment1Intro())
                .commit();


        setListeners();
    }


    private void setListeners() {
        nextButton.setOnClickListener(view -> {
            counter++;

            switch (counter) {
                case 1:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(getString(R.string.continue_text));
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment2Accounts()).commit();
                    break;
                case 2:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(getString(R.string.continue_text));
                    //fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment3Budget()).commit();
                    // Skip BudgetFragment for now.  Need to implement later.
                    counter++;
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment4Security()).commit();
                    break;
                case 3:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(R.string.continue_text);
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment4Security()).commit();
                    break;
                case 4:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(R.string.skip_text);
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment4SecurityEnter()).commit();
                    break;
                case 5:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(R.string.continue_text);
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment4SecurityFingerprint()).commit();
                    break;
                case 6:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    nextButton.setText(getString(R.string.finish_text));
                    fragmentTransaction.replace(fragmentHolder.getId(), new WelcomeFragment5Done()).commit();
                    break;
                case 7:
                    Intent intent = new Intent(getBaseContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        });
    }

    public void jumpToDone() {
        counter = 5;
        nextButton.performClick();
    }

    public void jumpToFingerprint() {

        if (canUseFingerprint()) {
            counter = 4;
            nextButton.performClick();
            return;
        }

        Timber.i("Fingerprint scanner not available for use.  Skipping to DoneFragment");
        jumpToDone();
    }


    private void setBottomPadding() {
        Timber.v("Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Timber.d("Soft key bar height: ", softKeyBarHeight);

            nextButton.setPadding(0,0,0, softKeyBarHeight);

        } catch(Exception e) {
            Timber.e(e, "Error in checking presence of soft keys and adapting UI accordingly.");
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

    private boolean canUseFingerprint() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return false;

        FingerprintManager fingerprintManager = (FingerprintManager) getApplicationContext().getSystemService(FINGERPRINT_SERVICE);

        if (fingerprintManager == null)
            return false;

        if (!fingerprintManager.isHardwareDetected())
            return false;

        if (!fingerprintManager.hasEnrolledFingerprints())
            return false;

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(KEYGUARD_SERVICE);

        if (keyguardManager == null)
            return false;

        if (!keyguardManager.isKeyguardSecure())
            return false;

        return true;
    }
}
