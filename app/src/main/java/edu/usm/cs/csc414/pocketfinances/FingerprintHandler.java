package edu.usm.cs.csc414.pocketfinances;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
@SuppressWarnings("ALL")
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private static final String TAG = "FingerprintHandler";

    // Must use CancellationSignal whenever fingerprint scanner is no longer needed.  Otherwise, other apps will not be able to use it.
    private CancellationSignal cancellationSignal;
    private Context context;
    private FragmentActivity activity;
    private View view;

    private int failCount = 0;
    private final int maxFails = 3;

    private TextView messageTextView;
    private ImageView fingerprintImageView;


    public FingerprintHandler(Context context, FragmentActivity activity, View view) {
        this.context = context;
        this.activity = activity;
        this.view = view;

        messageTextView = view.findViewById(R.id.fragment_fingerprint_messagetext);
        fingerprintImageView = view.findViewById(R.id.fragment_fingerprint_image);
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



    // is called when a fatal error has occurred.
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Log.e(TAG, (String)errString);
    }


    // is called when the fingerprint doesn’t match with any of the fingerprints registered on the device
    @Override
    public void onAuthenticationFailed() {

        fingerprintImageView.setImageDrawable(activity.getDrawable(R.drawable.ic_fingerprint_red));
        messageTextView.setTextColor(activity.getColor(R.color.colorRed));
        messageTextView.setText("Fingerprint authentication failed!");

        waitThenResetUI(2);

        Log.e(TAG, "Fingerprint authentication failed!");

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
        Log.e(TAG, "Authentication help:  " + helpString);
    }


    // is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        cancellationSignal.cancel();

        Log.i(TAG, "Fingerprint recognized!");

        fingerprintImageView.setImageDrawable(activity.getDrawable(R.drawable.ic_fingerprint_green));
        messageTextView.setTextColor(activity.getColor(R.color.colorGreen));
        messageTextView.setText("Fingerprint recognized!");

        // Launch MainActivity
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }



    private void waitThenResetUI(double seconds) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fingerprintImageView.setImageDrawable(activity.getDrawable(R.drawable.ic_fingerprint_white));
                messageTextView.setTextColor(activity.getColor(R.color.colorWhite));
                messageTextView.setText(activity.getText(R.string.fingerprintauth_message_default));
            }
        }, (int)(seconds * 1000));
    }


    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(context, message,
                Toast.LENGTH_SHORT);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);

        toast.show();
    }

}
