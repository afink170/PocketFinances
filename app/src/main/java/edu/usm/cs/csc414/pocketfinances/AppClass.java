package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.commonsware.cwac.saferoom.SQLCipherUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

import java.io.IOException;

import timber.log.Timber;

/**
 * Class for the application
 * Specifies any necessary setup for the app to run, and specifies which activity to start.
 */
public class AppClass extends Application {

    private static final String DB_NAME = "finances_db";


    /**
     * Method that is called when the application launches and before any activities are started.
     * Responsible for performing actions like setting up the custom font, encrypting the database if needed.
     * Also, upon first launch, generates unique password salt and database encryption key and stores them in SharedPreferences.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());


        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
        else
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    if (priority == Log.ERROR)
                        Crashlytics.log(priority, tag, message + t.getMessage());
                }
            });


        //------------------------ SET CUSTOM FONT -------------------------------------
        // get custom font from resources
        String newFont = getString(R.string.font_walkway_black);

        // Replace any/all default fonts with the custom font.
        ReplaceFont.replaceDefaultFont(this, "DEFAULT", newFont);
        ReplaceFont.replaceDefaultFont(this, "MONOSPACE", newFont);
        ReplaceFont.replaceDefaultFont(this, "SERIF", newFont);
        ReplaceFont.replaceDefaultFont(this, "SANS_SERIF", newFont);



        //------------------------ INIT SHARED PREFERENCES -----------------------------
        // declare and initialize shared preferences
        CustomSharedPreferences sharedPrefs = new CustomSharedPreferences(this);




        //------------------------ HANDLE APP VERSION CHANGES -------------------------
        if(sharedPrefs.getVersionCode() < 181117) {
            // There are some breaking changes before this,
            // so it is best to just clear the database and shared prefs
            Timber.i("Database-breaking changes have been introduced since last update, " +
                    "so database and shared preferences will be cleared.");

            // Reset all stored preferences
            sharedPrefs.clearSharedPreferences();
        }


        try {
            @SuppressWarnings("deprecation")
            int version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            Timber.i("Current app version code:  %d", version);

            sharedPrefs.setVersionCode(version);

        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Failed to get app version!");
        }



        // ---------------------- PERFORM FIRST LAUNCH TASKS --------------------------
        // If this is the first time running
        if (sharedPrefs.getIsFirstRun()) {

            sharedPrefs.setIsFirstRun(true);

            // Delete any previous database files just in case
            FinancesDatabase.deleteDatabase(getApplicationContext());

            // Generate or set encryption key, then store it in sharedPrefs as an encrypted string.
            // That way, the encryption key is never stored as plain text
            sharedPrefs.generateEncryptKeyAndStore();

            // Generate unique salt and store it in shared prefs
            sharedPrefs.setSalt(HashingHandler.generateSalt());
        }


        //------------------------ HANDLE DATABASE ENCRYPTION STATE --------------------
        try {
            // Get database state
            SQLCipherUtils.State state = SQLCipherUtils.getDatabaseState(getApplicationContext(), DB_NAME);

            // Check whether database is encrypted
            if (state == SQLCipherUtils.State.UNENCRYPTED) {
                // If it's unencrypted, encrypt the database
                try {
                    // Get database instance
                    FinancesDatabase db = FinancesDatabase.getDatabase(getApplicationContext());

                    // Close database
                    db.close();

                    // Encrypt database with encryption key
                    SQLCipherUtils.encrypt(getApplicationContext(), DB_NAME, sharedPrefs.getEncryptKey());
                    Timber.i("Successfully encrypted database!");

                } catch (IOException e) {
                    Timber.e(e, "Failed to encrypt previously unencrypted database!");
                }
            } else if (state == SQLCipherUtils.State.ENCRYPTED)
                // Otherwise, good to go
                Timber.i("Database is encrypted.  No action necessary.");

            else if (state == SQLCipherUtils.State.DOES_NOT_EXIST)
                // No database found.  Normal if first launch
                Timber.w("No database found.");

        } catch(Exception e) {
            Timber.e(e, "Failed to get database encryption state!");
        }
    }
}
