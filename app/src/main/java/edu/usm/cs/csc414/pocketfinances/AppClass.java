package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.content.Intent;
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

    // Constants for testing of password activity and welcome activity
    private static final boolean TESTING_WELCOME_ACTIVITY = false;
    private static final boolean TESTING_PASSWORD_ACTIVITY = true;
    private static final boolean TESTING_FINGERPRINT_ACTIVITY = true;
    private static final char[] DEFAULT_TEST_PASSWORD = {'0', '0', '0', '0'};


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


        // TODO : Remove this once testing of PasswordActivity and WelcomeActivity is complete
        // If testing password or welcome activity, or if actual first run, set shared preferences first run to true
        //sharedPrefs.setIsFirstRun(TESTING_PASSWORD_ACTIVITY || TESTING_WELCOME_ACTIVITY || sharedPrefs.getIsFirstRun());
        sharedPrefs.setFingerprintEnabled(TESTING_FINGERPRINT_ACTIVITY);
        sharedPrefs.setPasswordEnabled(TESTING_PASSWORD_ACTIVITY);





        // ---------------------- PERFORM FIRST LAUNCH TASKS --------------------------
        // If this is the first time running
        if (sharedPrefs.getIsFirstRun()) {

            // Generate or set encryption key, then store it in sharedPrefs as an encrypted string.
            // That way, the encryption key is never stored as plain text
            sharedPrefs.generateEncryptKeyAndStore();


            // Number of bytes of salt
            final int bytes = 20;

            byte[] salt;

            if (!sharedPrefs.getIsSaltGenerated()) {
                // Generate random salt
                SecureRandom rng = new SecureRandom();
                salt = rng.generateSeed(bytes);

                // Store salt in shared prefs
                sharedPrefs.setSalt(salt);
            } else {
                salt = sharedPrefs.getSalt();
            }

            if (!TESTING_PASSWORD_ACTIVITY) {

                // Launch WelcomeActivity, which walks the user through the app and allows them to set up basic settings/features
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);

            }
            else {
                // TODO : remove this once password feature has been tested and fixed.
                // For password activity testing
                try {

                    // Number of iterations
                    final int iterations = 100;


                    byte[] pinBytes = new String(DEFAULT_TEST_PASSWORD).getBytes();

                    // hash password
                    PKCS5S2ParametersGenerator kdf1 = new PKCS5S2ParametersGenerator();
                    kdf1.init(pinBytes, salt, iterations);
                    byte[] savedHash = ((KeyParameter) kdf1.generateDerivedMacParameters(8 * bytes)).getKey();

                    // store password hash in shared prefs
                    sharedPrefs.setPinHash(savedHash);

                    // Launch PasswordActivity
                    Intent intent = new Intent(this, PasswordActivity.class);
                    startActivity(intent);


                } catch(Exception e) {
                    Log.e(TAG, "Failed to initialize default password.");
                }
            }



        }
        else if (sharedPrefs.getPasswordEnabled() || TESTING_PASSWORD_ACTIVITY) {

            // Launch PasswordActivity
            Intent intent = new Intent(this, PasswordActivity.class);
            startActivity(intent);

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
