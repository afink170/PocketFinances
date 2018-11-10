package edu.usm.cs.csc414.pocketfinances;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
@SuppressWarnings("ALL")
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // Must use CancellationSignal whenever fingerprint scanner is no longer needed.  Otherwise, other apps will not be able to use it.
    private CancellationSignal cancellationSignal;
    private Context context;
    private FragmentActivity activity;

    private int failCount = 0;
    private final int maxFails = 3;
    private boolean errorMessagesEnabled = true;


    public FingerprintHandler(Context mContext, FragmentActivity mActivity) {
        context = mContext;
        activity = mActivity;
    }


    public void cancelAuth() {
        cancellationSignal.cancel();
    }


    // Method that is responsible for starting the fingerprint authentication process
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Begin authentication
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    // Method for manually disabling error message.
    public void disableErrorMessages() {
        errorMessagesEnabled = false;
    }


    // is called when a fatal error has occurred.
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (errorMessagesEnabled)
            showToastMessage("Authentication error\n" + errString);
    }


    // is called when the fingerprint doesn’t match with any of the fingerprints registered on the device
    @Override
    public void onAuthenticationFailed() {

        if (errorMessagesEnabled)
            showToastMessage("Authentication failed");

        failCount++;

        if (failCount >= maxFails) {
            cancellationSignal.cancel();

            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
            fragmentTransaction.replace(R.id.activity_password_framelayout, new PasswordFragment())
                    .commit();
        }
    }


    // is called when a non-fatal error has occurred. This method provides additional information about the error
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (errorMessagesEnabled)
            showToastMessage("Authentication help\n" + helpString);
    }


    // is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        cancellationSignal.cancel();

        // Launch MainActivity
        Intent intent = new Intent(activity, MainActivity.class);
        context.startActivity(intent);
        activity.finish();
    }




    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(context, message,
                Toast.LENGTH_SHORT);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);

        toast.show();
    }

}
