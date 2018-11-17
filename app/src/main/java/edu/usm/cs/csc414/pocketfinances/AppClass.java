package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.commonsware.cwac.saferoom.SQLCipherUtils;

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.io.IOException;
import java.security.SecureRandom;

/**
 * Class for the application
 * Specifies any necessary setup for the app to run, and specifies which activity to start.
 */
public class AppClass extends Application {

    private static final String DB_NAME = "finances_db";
    private static final String TAG = "AppClass";


    /**
     * Method that is called when the application launches and before any activities are started.
     * Responsible for performing actions like setting up the custom font, encrypting the database if needed.
     * Also, upon first launch, generates unique password salt and database encryption key and stores them in SharedPreferences.
     */
    @Override
    public void onCreate() {
        super.onCreate();


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
        if(sharedPrefs.getVersionCode() <= 1) {
            // There are some breaking changes before this,
            // so it is best to just clear the database and shared prefs
            Log.i(TAG, "Database-breaking changes have been introduced since last update, " +
                    "so database and shared preferences will be cleared.");

            // Reset all stored preferences
            sharedPrefs.clearSharedPreferences();
        }


        try {
            @SuppressWarnings("deprecation")
            int version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

            Log.i(TAG, "Current app version code:  " + version);

            sharedPrefs.setVersionCode(version);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to get app version!", e);
        }



        // ---------------------- PERFORM FIRST LAUNCH TASKS --------------------------
        // If this is the first time running
        if (sharedPrefs.getIsFirstRun()) {

            sharedPrefs.setIsFirstRun(true);

            // TODO : Need to perhaps find a safer way to do this?
            FinancesDatabase.deleteDatabase(getApplicationContext());

            // Generate or set encryption key, then store it in sharedPrefs as an encrypted string.
            // That way, the encryption key is never stored as plain text
            sharedPrefs.generateEncryptKeyAndStore();



            // Number of bytes of salt
            final int bytes = 20;

            byte[] salt;

            // Generate random salt
            SecureRandom rng = new SecureRandom();
            salt = rng.generateSeed(bytes);

            // Store salt in shared prefs
            sharedPrefs.setSalt(salt);
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
                    Log.i(TAG, "Successfully encrypted database!");

                } catch (IOException e) {
                    Log.e(TAG, "Failed to encrypt previously unencrypted database!", e);
                }
            } else if (state == SQLCipherUtils.State.ENCRYPTED)

                // Otherwise, good to go
                Log.i(TAG, "Database is encrypted.  No action necessary.");

            else if (state == SQLCipherUtils.State.DOES_NOT_EXIST)

                // No database found.  Normal if first launch
                Log.e(TAG, "No database found.");

        } catch(Exception e) {
            Log.e(TAG, "Failed to get database encryption state!", e);
        }
    }
}
