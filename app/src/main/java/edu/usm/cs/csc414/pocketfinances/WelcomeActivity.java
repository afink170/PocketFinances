package edu.usm.cs.csc414.pocketfinances;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    FrameLayout fragmentHolderLayout;
    TextView nextButton;
    FragmentTransaction fragmentTransaction;

    private int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fragmentHolderLayout = findViewById(R.id.activity_welcome_framelayout);
        nextButton = findViewById(R.id.activity_welcome_next_textview);

        // Set bottom padding to the layout so that any present soft keys don't overlap the nav bar
        setBottomPadding();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(fragmentHolderLayout.getId(), new WelcomeFragment1Intro())
                .commit();


        setListeners();
    }


    private void setListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;

                switch (counter) {
                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                        nextButton.setText(getString(R.string.continue_text));
                        fragmentTransaction.replace(fragmentHolderLayout.getId(), new WelcomeFragment2Accounts()).commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                        nextButton.setText(getString(R.string.continue_text));
                        fragmentTransaction.replace(fragmentHolderLayout.getId(), new WelcomeFragment3Budget()).commit();
                        break;
                    case 3:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                        nextButton.setText(R.string.skip_text);
                        fragmentTransaction.replace(fragmentHolderLayout.getId(), new WelcomeFragment4Security()).commit();
                        break;
                    case 4:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                        nextButton.setText(getString(R.string.finish_text));
                        Bundle bundle = new Bundle();
                        bundle.putString("prevPage", "skipped");
                        WelcomeFragment5Done doneFragment = new WelcomeFragment5Done();
                        doneFragment.setArguments(bundle);
                        fragmentTransaction.replace(fragmentHolderLayout.getId(), doneFragment).commit();
                        break;
                    case 5:
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void setNextButtonToFinish() {
        nextButton.setText(getString(R.string.finish_text));
        counter++;
    }


    private void setBottomPadding() {
        Log.v(TAG, "Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Log.d(TAG, "Soft key bar height: " + softKeyBarHeight);

            nextButton.setPadding(0,0,0, softKeyBarHeight);

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
