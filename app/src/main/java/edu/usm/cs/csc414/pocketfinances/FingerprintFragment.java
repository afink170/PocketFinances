package edu.usm.cs.csc414.pocketfinances;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

@SuppressWarnings("ALL")
public class FingerprintFragment extends Fragment {

    private static final String TAG = "FingerprintFragment";
    private static final String KEY_NAME = "fingerprint_auth_key";

    private CustomSharedPreferences sharedPrefs;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private FingerprintHandler handler;

    private Button usePasswordButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        usePasswordButton = view.findViewById(R.id.fragment_fingerprint_use_password_button);
        usePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.disableErrorMessages();
                exitToPasswordFragment();
            }
        });

        // Check build SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Initialize KeyguardManager
            keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);

            // Initialize FingerprintManager
            fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);

            // Check if fingerprint scanner hardware is present on device
            if (!fingerprintManager.isHardwareDetected()) {
                Log.e(TAG, "No fingerprint scanner detected!");
                showToastMessage("No fingerprint scanner detected!");
                exitToPasswordFragment();
                return view;
            }

            // Check if access to fingerprint scanner has been granted for this app
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Fingerprint scanner access not granted!");
                showToastMessage("Fingerprint scanner access not granted!");
                exitToPasswordFragment();
                return view;
            }

            // Check if user has any enrolled fingerprints
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Log.e(TAG, "No enrolled fingerprints found!");
                showToastMessage("No enrolled fingerprints found!");
                exitToPasswordFragment();
                return view;
            }

            // Check if the lock screen is secured
            if (!keyguardManager.isKeyguardSecure()) {
                Log.e(TAG, "Lock screen is not secured!");
                showToastMessage("Lock screen is not secured!");
                exitToPasswordFragment();
                return view;
            }

            // Generate fingerprint key
            try {
                generateKey();
            } catch (Exception e) {
                Log.e(TAG, "Failed to perform fingerprint authentication.", e);
            }

            if (initCipher()) {
                //If the cipher is initialized successfully, then create a CryptoObject instance
                cryptoObject = new FingerprintManager.CryptoObject(cipher);

                // Initialize the FingerprintHandler, which will start the authentication process and process the authentication events
                handler = new FingerprintHandler(getContext(), getActivity());

                // Begin authentication
                handler.startAuth(fingerprintManager, cryptoObject);
            }
        }

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        // If handler has been initialized and app is sent to background, cancel authentication.
        // This is necessary because if fingerprint auth is not cancelled, other apps can't use the scanner.
        if (handler != null)
            handler.cancelAuth();

        // Go ahead and return to password fragment
        exitToPasswordFragment();
    }


    private void exitToPasswordFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
        fragmentTransaction.replace(R.id.activity_password_framelayout, new PasswordFragment())
                .commit();
    }

    private void showToastMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    private void generateKey() throws FingerprintException {
        try {

            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Get instance of KeyGenerator
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            // Initialize an empty KeyStore
            keyStore.load(null);

            // Initialize the KeyGenerator
            keyGenerator.init(new
                    //Specify the operation(s) this key can be used for
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            // Generate the key
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException e) {
            Log.e(TAG, "Failed to generate fingerprint authentication key.");
            throw new FingerprintException(e);
        }
    }

    // Create a new method that we’ll use to initialize our cipher.
    public boolean initCipher() {

        try {
            // Obtain a cipher instance and configure it with the properties required for fingerprint authentication.
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Log.e(TAG, "Failed to get Cipher instance");
            throw new RuntimeException(e);
        }

        try {
            // Initialize empty KeyStore
            keyStore.load(null);

            // Get key from KeyStore
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // Return true if the cipher has been initialized successfully
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            Log.e(TAG, "Cipher initialization failed!");
            // Return false if cipher initialization failed
            return false;
        } catch (KeyStoreException
                | CertificateException
                | UnrecoverableKeyException
                | IOException
                | NoSuchAlgorithmException
                | InvalidKeyException e) {
            Log.e(TAG, "Cipher initialization failed!");
            throw new RuntimeException(e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
