package edu.usm.cs.csc414.pocketfinances;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(getApplicationContext());

        // Initialize progress_light bar and animate it
        progressBar = findViewById(R.id.activity_splash_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setIndeterminateDrawable(
                getDrawable(
                        sharedPrefs.getActivityTheme() == CustomSharedPreferences.THEME_DARK
                        ? R.drawable.progress_light : R.drawable.progress_dark
                )
        );
        progressBar.animate();

        background = findViewById(R.id.activity_splash_background);

        try {
            // Set chosen background from sharedPrefs
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }
        catch (Exception e) {
            sharedPrefs.setActivityBackground(Background.DARK_GREY);
            background.setImageResource(sharedPrefs.getActivityBackground().getResourceId());
        }

        new LaunchMainActivity(this).execute();
    }


    private static class LaunchMainActivity extends AsyncTask<Void, Void, Void> {

        @SuppressLint("StaticFieldLeak")
        private Activity activity;

        LaunchMainActivity(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void[] voids) {

            //Timber.v("LaunchMainActivity.doInBackground called.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Timber.e(e, "Failed to wait before launching MainActivity.");
            }

            // Launch MainActivity
            //Timber.v("Launching MainActivity.");
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //Timber.v("LaunchMainActivity.onPostExecute called.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Timber.e(e, "Failed to wait before finishing SplashActivity.");
            }

            //Timber.v("Finishing SplashActivity.");
            activity.finish();
        }
    }

}
