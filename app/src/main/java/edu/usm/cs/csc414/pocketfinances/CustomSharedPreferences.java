package edu.usm.cs.csc414.pocketfinances;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.thz.keystorehelper.KeyStoreManager;

import java.util.Arrays;

import timber.log.Timber;


/**
 * A custom class that inherits from the {@link SharedPreferences} class.
 * Provides access to the shared preferences instance containing user settings, keys, etc.
 * Also provides methods for mutating this data.
 */
public class CustomSharedPreferences {

    private static final String SHARED_PREFS = "shared_prefs";

    private static final String PREFS_VERSION_CODE = "version_code";
    private static final String PREFS_DEFAULT_ACCOUNT = "default_account";
    private static final String PREFS_DEFAULT_IS_ALL_ACCOUNTS = "default_is_all";
    private static final String PREFS_PIN_HASH = "pin_hash";
    private static final String PREFS_TEMP_PIN_HASH = "temp_pin_hash";
    private static final String PREFS_SALT = "salt";
    private static final String PREFS_FIRST_RUN = "is_first_run";
    private static final String PREFS_PASSWORD_ENABLED = "password_enabled";
    private static final String PREFS_ENCRYPT_KEY = "encrypt_key";
    private static final String PREFS_FINGERPRINT_ENABLED = "fingerprint_enabled";
    private static final String PREFS_ACTIVITY_BACKGROUND = "activity_background";
    private static final String PREFS_ACTIVITY_THEME = "activity_theme";

    public static final int THEME_DARK = 1;
    public static final int THEME_LIGHT = 0;

    private static final String KEYSTORE_ENCRYPT_ALIAS = "encrypt_alias";

    private SharedPreferences sharedPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;
    private Context context;

    public CustomSharedPreferences(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    // Method for setting a listener for changed preferences, such as a change of default account
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.onSharedPreferenceChangeListener = onSharedPreferenceChangeListener;
    }


    /**
     * Clears all data from shared preferences and resets to default
     */
    public void clearSharedPreferences() {
        sharedPrefs.edit().clear().apply();
    }


    /**
     * Gets the version code of the app.
     * Used for handling app updates.
     *
     * @return The integer version code of the app as of the last time it was opened.
     */
    public int getVersionCode() {
        return sharedPrefs.getInt(PREFS_VERSION_CODE, 1);
    }


    /**
     * Stores the version code of the app in shared preferences
     *
     * @param version The integer version code (not the version name) of the app
     */
    public void setVersionCode(int version) {
        sharedPrefs.edit().putInt(PREFS_VERSION_CODE, version).apply();
    }

    /**
     * Gets the ID of the current default bank account.
     * If ID is -1 (i.e. if there is not one set), then it will default to all account balances
     *
     * @return The ID of the current default bank account, or -1 if there is no default set
     */
    public int getDefaultAccountId() {
        int id = sharedPrefs.getInt(PREFS_DEFAULT_ACCOUNT, -1);

        if (id == -1) setDefaultIsAllAccounts(true);

        return id;
    }


    /**
     * Stores the new default bank account ID in shared preferences
     *
     * @param defaultAccountId Integer value of the ID of the new default bank account
     */
    public void setDefaultAccountId(int defaultAccountId) {

        if (defaultAccountId != -1) setDefaultIsAllAccounts(false);

        sharedPrefs.edit().putInt(PREFS_DEFAULT_ACCOUNT, defaultAccountId).apply();
    }


    /**
     * Gets whether the home fragment should display the sum of all account balances as the balance.
     *
     * @return True if total of account balances should be displayed, false otherwise.
     */
    public boolean getDefaultIsAllAccounts() {

        boolean isAllAccounts = sharedPrefs.getBoolean(PREFS_DEFAULT_IS_ALL_ACCOUNTS, false);

        if (isAllAccounts) setDefaultAccountId(-1);

        return isAllAccounts;
    }


    /**
     *
     * @param isAllAccounts
     */
    public void setDefaultIsAllAccounts(boolean isAllAccounts) {

        if (isAllAccounts) setDefaultAccountId(-1);

        sharedPrefs.edit().putBoolean(PREFS_DEFAULT_IS_ALL_ACCOUNTS, isAllAccounts).apply();
    }


    /**
     * Gets the hash of the chosen PIN for securing the app.
     * The hash is stored as a base 64 encoded String but is converted to a byte array before being returned.
     *
     * @return The byte array hash of the PIN
     */
    public byte[] getPinHash() {
        String hashString = sharedPrefs.getString(PREFS_PIN_HASH, "");
        return Base64.decode(hashString, Base64.DEFAULT);
    }


    /**
     * Stores the hash of the chosen PIN in shared preferences.
     * Stores the byte array hash as a base 64 encoded String.
     *
     * @param hash The byte array hash of the chosen PIN
     */
    public void setPinHash(byte[] hash) {
        String hashString = Base64.encodeToString(hash, Base64.DEFAULT);
        sharedPrefs.edit().putString(PREFS_PIN_HASH, hashString).apply();
    }


    /**
     * Gets the hash of the temporary chosen PIN for setting a new PIN.
     * The hash is stored as a base 64 encoded String but is converted to a byte array before being returned.
     *
     * @return The byte array hash of the PIN
     */
    public byte[] getTempPinHash() {
        String hashString = sharedPrefs.getString(PREFS_TEMP_PIN_HASH, "");
        return Base64.decode(hashString, Base64.DEFAULT);
    }


    /**
     * Stores the hash of the temporary chosen PIN for setting a new PIN in shared preferences.
     * Stores the byte array hash as a base 64 encoded String.
     *
     * @param hash The byte array hash of the chosen PIN
     */
    public void setTempPinHash(byte[] hash) {
        String hashString = Base64.encodeToString(hash, Base64.DEFAULT);
        sharedPrefs.edit().putString(PREFS_TEMP_PIN_HASH, hashString).apply();
    }


    /**
     * Resets the pin hash in shared preferences to an empty String.
     */
    public void resetPinHash() {
        sharedPrefs.edit().putString(PREFS_PIN_HASH, "").apply();
    }


    /**
     * Gets whether the application has not previously been run.
     * If it returns true, the value is also updated to false in shared preferences.
     *
     * @return True if the application has not previously been launched, such as on app first install, or false otherwise.
     */
    public boolean getIsFirstRun() {
        if (sharedPrefs.getBoolean(PREFS_FIRST_RUN, true)) {
            setIsFirstRun(false);
            return true;
        }
        return false;
    }


    /**
     * Stores whether the application has not previously been run in shared preferences.
     *
     * @param isFirstRun Boolean value
     */
    public void setIsFirstRun(boolean isFirstRun) {
        sharedPrefs.edit().putBoolean(PREFS_FIRST_RUN, isFirstRun).apply();
    }


    /**
     * Gets the stored unique salt to be used in hashing the PIN.
     * The salt is stored as a base 64 encoded String but is converted to a byte array before being returned.
     *
     * @return The byte array unique salt
     */
    public byte[] getSalt() {
        String saltString = sharedPrefs.getString(PREFS_SALT, "");
        return Base64.decode(saltString, Base64.DEFAULT);
    }


    /**
     * Gets whether a unique salt has previously been generated.
     *
     * @return True if there is already a salt stored in shared preferences, and false otherwise.
     */
    public boolean getIsSaltGenerated() {
        String saltString = sharedPrefs.getString(PREFS_SALT, "");
        return !saltString.equals("");
    }


    /**
     * Stores the unique salt for hashing the PIN in shared preferences.
     * Stores the byte array salt as a base 64 encoded String.
     *
     * @param salt The byte array unique salt
     */
    public void setSalt(byte[] salt) {
        String saltString = Base64.encodeToString(salt, Base64.DEFAULT);
        sharedPrefs.edit().putString(PREFS_SALT, saltString).apply();
    }


    /**
     * Gets whether password protection is enabled.
     *
     * @return True if the user has enabled password protection, and false otherwise
     */
    public boolean getPasswordEnabled() {
        return sharedPrefs.getBoolean(PREFS_PASSWORD_ENABLED, false);
    }


    /**
     * Stores whether password protection is enabled in shared preferences.
     *
     * @param enabled Boolean value true if the user wants to enable password protection, and false otherwise
     */
    public void setPasswordEnabled(boolean enabled) {
        sharedPrefs.edit().putBoolean(PREFS_PASSWORD_ENABLED, enabled).apply();
    }


    /**
     * Gets the encrypted database encryption key from shared preferences and decrypts it using KeyStore.
     *
     * @return The decrypted database encryption key as a char array, which is more secure than a String in terms of mutability
     */
    public char[] getEncryptKey() {
        String encryptedKey = sharedPrefs.getString(PREFS_ENCRYPT_KEY, "");
        KeyStoreManager.init(context);
        return KeyStoreManager.decryptData(encryptedKey, KEYSTORE_ENCRYPT_ALIAS).toCharArray();
    }


    /**
     * Generates a random 10 character long database encryption key using KeyStore and encrypts it before storing it in shared preferences.
     */
    public void generateEncryptKeyAndStore() {
        // Only generate and store a new encryption key if one does not already exist in SharedPreferences.
        if (sharedPrefs.getString(PREFS_ENCRYPT_KEY, "").equals("")) {
            KeyStoreManager.init(context);
            char[] randomKey = KeyStoreManager.getNewRandomPhrase(10).toCharArray();
            String encryptedKey = KeyStoreManager.encryptData(Arrays.toString(randomKey), KEYSTORE_ENCRYPT_ALIAS);
            randomKey = null;
            sharedPrefs.edit().putString(PREFS_ENCRYPT_KEY, encryptedKey).apply();
        }
        else {
            Timber.d("Encryption key already generated.  No action performed.");
        }
    }


    /**
     * Takes an unencrypted database encryption key as an argument then encrypts it using KeyStore before storing it in shared preferences.
     *
     * @param key The unencrypted database encryption key as a char array
     */
    public void setEncryptKeyAndStore(char[] key) {
        KeyStoreManager.init(context);
        String encryptedKey = KeyStoreManager.encryptData(Arrays.toString(key), KEYSTORE_ENCRYPT_ALIAS);
        key = null;
        sharedPrefs.edit().putString(PREFS_ENCRYPT_KEY, encryptedKey).apply();
    }


    /**
     * Gets whether fingerprint authentication is enabled.
     *
     * @return True if fingerprint authentication is enabled, and false otherwise.
     */
    public boolean getFingerprintEnabled() {
        return sharedPrefs.getBoolean(PREFS_FINGERPRINT_ENABLED, false);
    }

    /**
     * Sets whether fingerprint authentication is enabled.
     *
     * @param enabled Whether or not fingerprint authentication is enabled.
     */
    public void setFingerprintEnabled(boolean enabled) {
        sharedPrefs.edit().putBoolean(PREFS_FINGERPRINT_ENABLED, enabled).apply();
    }


    /**
     * Gets the chosen background for the app.
     *
     * @return the object referring to the chosen background.
     */
    public Background getActivityBackground() {
        int backgroundId = sharedPrefs.getInt(PREFS_ACTIVITY_BACKGROUND, Background.DARK_GREY.getResourceId());

        if (backgroundId == Background.LIGHT_BLUE.getResourceId())
            setActivityTheme(THEME_LIGHT);
        else
            setActivityTheme(THEME_DARK);

        return  Background.getById(backgroundId);
    }


    /**
     * Stores the resource ID of the chosen background for the app in shared preferences.
     *
     * @param background The chosen background object.
     */
    public void setActivityBackground(Background background) {
        sharedPrefs.edit().putInt(PREFS_ACTIVITY_BACKGROUND, background.getResourceId()).apply();

        if (background == Background.LIGHT_BLUE)
            setActivityTheme(THEME_LIGHT);
        else
            setActivityTheme(THEME_DARK);
    }


    /**
     * Gets the chosen theme of the app, either dark or light.
     *
     * @return 1 for dark, 0 for light.
     */
    public int getActivityTheme() {
        return sharedPrefs.getInt(PREFS_ACTIVITY_THEME, THEME_DARK);
    }


    /**
     * Stores the integer value of the chosen theme for the app in shared preferences.
     *
     * @param theme 1 for dark, 0 for light.
     */
    public void setActivityTheme(int theme) {
        sharedPrefs.edit().putInt(PREFS_ACTIVITY_THEME, theme).apply();
    }
}
