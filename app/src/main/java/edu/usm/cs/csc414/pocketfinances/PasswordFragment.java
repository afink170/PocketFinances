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

import timber.log.Timber;

public class PasswordFragment extends Fragment implements View.OnClickListener {

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
        Timber.v("Attempting to create PasswordFragment.");

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

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        fingerprintBtn.setOnClickListener(this);

        input0.setText("");
        input1.setText("");
        input2.setText("");
        input3.setText("");

        return view;
    }


    private void authenticatePin(char[] pin) {

        Activity parentActivity = getActivity();

        // get salt and password hash from shared prefs
        final byte[] salt = sharedPrefs.getSalt();
        final byte[] savedHash = sharedPrefs.getPinHash();

        try {
            byte[] checkHash = HashingHandler.getHash(salt, new String(pin).getBytes());

            // check whether it matches saved password hash
            if (Arrays.equals(checkHash, savedHash)) {
                // if correct password, launch main activity
                Intent splashActivityIntent = new Intent(parentActivity, SplashActivity.class);
                startActivity(splashActivityIntent);
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
            Timber.e(e, "Failed to authenticate password.");
        }
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.fragment_password_button0:
                handleNumberClick('0');
                break;
            case R.id.fragment_password_button1:
                handleNumberClick('1');
                break;
            case R.id.fragment_password_button2:
                handleNumberClick('2');
                break;
            case R.id.fragment_password_button3:
                handleNumberClick('3');
                break;
            case R.id.fragment_password_button4:
                handleNumberClick('4');
                break;
            case R.id.fragment_password_button5:
                handleNumberClick('5');
                break;
            case R.id.fragment_password_button6:
                handleNumberClick('6');
                break;
            case R.id.fragment_password_button7:
                handleNumberClick('7');
                break;
            case R.id.fragment_password_button8:
                handleNumberClick('8');
                break;
            case R.id.fragment_password_button9:
                handleNumberClick('9');
                break;
            case R.id.fragment_password_button_back:
                handleBackButtonClick();
                break;
            case R.id.fragment_password_button_fingerprint:
                handleFingerprintButtonClicked();
                break;
            default:
                break;
        }
    }

    private void handleNumberClick(char number) {
        if (input[0] == ' ') {
            input[0] = number;
            input0.setText("*");
        }
        else if (input[1] == ' ') {
            input[1] = number;
            input1.setText("*");
        }
        else if (input[2] == ' ') {
            input[2] = number;
            input2.setText("*");
        }
        else if (input[3] == ' ') {
            input[3] = number;
            input3.setText("*");
            authenticatePin(input);
        }
    }

    private void handleBackButtonClick() {
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

    private void handleFingerprintButtonClicked() {
        if (sharedPrefs.getFingerprintEnabled()) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.activity_password_framelayout, new FingerprintFragment())
                    .commit();
        }
    }
}
