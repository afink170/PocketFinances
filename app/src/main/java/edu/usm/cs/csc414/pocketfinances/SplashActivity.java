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

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    ProgressBar progressBar;
    ImageView background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize progress bar and animate it
        progressBar = findViewById(R.id.activity_splash_progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.animate();

        background = findViewById(R.id.activity_splash_background);
        background.setImageResource(new CustomSharedPreferences(getApplicationContext()).getActivityBackground());

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

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Failed to wait before launching MainActivity.", e);
            }

            // Launch MainActivity
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity.finish();
        }
    }

}
