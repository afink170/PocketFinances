package edu.usm.cs.csc414.pocketfinances;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.Arrays;

public class PasswordFragment extends Fragment {

    private static final String TAG = "PasswordFragment";

    private CustomSharedPreferences sharedPrefs;

    // Declare Ui elements
    TextView btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView input0, input1, input2, input3;
    ImageButton fingerprintBtn, backBtn;
    GridLayout gridLayout;

    // Stores user input
    char[] input = {' ', ' ', ' ', ' '};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        sharedPrefs = new CustomSharedPreferences(getContext());

        // Initialize UI elements
        btn0 = view.findViewById(R.id.fragment_password_button0);
        btn1 = view.findViewById(R.id.fragment_password_button1);
        btn2 = view.findViewById(R.id.fragment_password_button2);
        btn3 = view.findViewById(R.id.fragment_password_button3);
        btn4 = view.findViewById(R.id.fragment_password_button4);
        btn5 = view.findViewById(R.id.fragment_password_button5);
        btn6 = view.findViewById(R.id.fragment_password_button6);
        btn7 = view.findViewById(R.id.fragment_password_button7);
        btn8 = view.findViewById(R.id.fragment_password_button8);
        btn9 = view.findViewById(R.id.fragment_password_button9);
        input0 = view.findViewById(R.id.fragment_password_text0);
        input1 = view.findViewById(R.id.fragment_password_text1);
        input2 = view.findViewById(R.id.fragment_password_text2);
        input3 = view.findViewById(R.id.fragment_password_text3);
        fingerprintBtn = view.findViewById(R.id.fragment_password_button_fingerprint);
        backBtn = view.findViewById(R.id.fragment_password_button_back);
        gridLayout = view.findViewById(R.id.fragment_password_gridlayout);

        if (!sharedPrefs.getFingerprintEnabled())
            fingerprintBtn.setVisibility(View.GONE);
        else
            fingerprintBtn.setVisibility(View.VISIBLE);

        setUiListeners();

        input0.setText("");
        input1.setText("");
        input2.setText("");
        input3.setText("");

        return view;
    }


    private void authenticatePin(char[] pin) {

        Activity parentActivity = getActivity();

        // number of bytes of salt
        final int numBytes = 20;

        // number of hashing iterations.
        final int iterations = 100;

        // get salt and password hash from shared prefs
        final byte[] salt = sharedPrefs.getSalt();
        final byte[] savedHash = sharedPrefs.getPinHash();

        try {
            // hash user input password
            PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator();
            kdf.init(new String(pin).getBytes(), salt, iterations);
            byte[] checkHash = ((KeyParameter) kdf.generateDerivedMacParameters(8 * numBytes)).getKey();

            // check whether it matches saved password hash
            if (Arrays.equals(checkHash, savedHash)) {
                // if correct password, launch main activity
                Intent mainActivityIntent = new Intent(parentActivity, MainActivity.class);
                startActivity(mainActivityIntent);
                parentActivity.finish();

            }
            else {
                // if not correct, clear input and try again
                Toast.makeText(parentActivity, "Incorrect password!", Toast.LENGTH_SHORT).show();

                input[0] = ' ';
                input[1] = ' ';
                input[2] = ' ';
                input[3] = ' ';

                input0.setText("");
                input1.setText("");
                input2.setText("");
                input3.setText("");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to authenticate password.", e);
        }
    }


    private void setUiListeners() {
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '0';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '0';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '0';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '0';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '1';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '1';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '1';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '1';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '2';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '2';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '2';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '2';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '3';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '3';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '3';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '3';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '4';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '4';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '4';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '4';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '5';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '5';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '5';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '5';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '6';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '6';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '6';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '6';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '7';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '7';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '7';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '7';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '8';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '8';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '8';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '8';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input[0] == ' ') {
                    input[0] = '9';
                    input0.setText("*");
                }
                else if (input[1] == ' ') {
                    input[1] = '9';
                    input1.setText("*");
                }
                else if (input[2] == ' ') {
                    input[2] = '9';
                    input2.setText("*");
                }
                else if (input[3] == ' ') {
                    input[3] = '9';
                    input3.setText("*");
                    authenticatePin(input);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! (input[2] == ' ')) {
                    input[2] = ' ';
                    input2.setText("");
                }
                else if (! (input[1] == ' ')) {
                    input[1] = ' ';
                    input1.setText("");
                }
                else if (! (input[0] == ' ')) {
                    input[0] = ' ';
                    input0.setText("");
                }
            }
        });

        fingerprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPrefs.getFingerprintEnabled()) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    //fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
                    fragmentTransaction.replace(R.id.activity_password_framelayout, new FingerprintFragment())
                            .commit();
                }
            }
        });
    }
}
