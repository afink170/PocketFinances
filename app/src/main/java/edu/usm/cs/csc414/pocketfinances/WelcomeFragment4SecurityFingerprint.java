package edu.usm.cs.csc414.pocketfinances;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

@SuppressWarnings("deprecation")
public class WelcomeFragment4SecurityFingerprint extends Fragment {
    private static final int REQUEST_USE_FINGERPRINT_CODE = 0;

    private Switch enableFingerprintSwitch;
    private CustomSharedPreferences sharedPrefs;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_add, container, false);

        enableFingerprintSwitch = view.findViewById(R.id.fragment_fingerprint_enter_switch);

        sharedPrefs = new CustomSharedPreferences(getContext());

        enableFingerprintSwitch.setChecked(false);

        enableFingerprintSwitch.setOnCheckedChangeListener((compoundButton, b) -> {

            if (!b) {
                sharedPrefs.setFingerprintEnabled(false);
                return;
            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.USE_FINGERPRINT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.USE_FINGERPRINT}, REQUEST_USE_FINGERPRINT_CODE);
            }
            else
                sharedPrefs.setFingerprintEnabled(true);
        });

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USE_FINGERPRINT_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                // Permissions were granted.  Good to go.
                sharedPrefs.setFingerprintEnabled(enableFingerprintSwitch.isChecked());
            else
                enableFingerprintSwitch.setChecked(false);

        }
    }
}
