package edu.usm.cs.csc414.pocketfinances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.Arrays;


public class WelcomeFragment4SecurityConfirm extends Fragment implements View.OnClickListener {

    private static final String TAG = "WelcomeFragment4";

    CustomSharedPreferences sharedPrefs;

    // Declare Ui elements
    TextView btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView input0, input1, input2, input3;
    ImageButton backBtn;

    // Stores user input
    char[] input = {' ', ' ', ' ', ' '};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_confirm, container, false);

        // Initialize UI elements
        btn0 = view.findViewById(R.id.fragment_password_confirm_button0);
        btn1 = view.findViewById(R.id.fragment_password_confirm_button1);
        btn2 = view.findViewById(R.id.fragment_password_confirm_button2);
        btn3 = view.findViewById(R.id.fragment_password_confirm_button3);
        btn4 = view.findViewById(R.id.fragment_password_confirm_button4);
        btn5 = view.findViewById(R.id.fragment_password_confirm_button5);
        btn6 = view.findViewById(R.id.fragment_password_confirm_button6);
        btn7 = view.findViewById(R.id.fragment_password_confirm_button7);
        btn8 = view.findViewById(R.id.fragment_password_confirm_button8);
        btn9 = view.findViewById(R.id.fragment_password_confirm_button9);
        input0 = view.findViewById(R.id.fragment_password_confirm_text0);
        input1 = view.findViewById(R.id.fragment_password_confirm_text1);
        input2 = view.findViewById(R.id.fragment_password_confirm_text2);
        input3 = view.findViewById(R.id.fragment_password_confirm_text3);
        backBtn = view.findViewById(R.id.fragment_password_confirm_button_back);

        sharedPrefs = new CustomSharedPreferences(getContext());

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

        input0.setText("");
        input1.setText("");
        input2.setText("");
        input3.setText("");

        return view;
    }


    private void processPin(char[] pin) {

        // number of bytes of salt
        final int numBytes = 20;

        // number of hashing iterations.
        // The more iterations, the more secure, but worse performance
        final int iterations = 100;

        // get salt and password hash from shared prefs
        final byte[] salt = sharedPrefs.getSalt();

        try {
            // hash user input password
            PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator();
            kdf.init(new String(pin).getBytes(), salt, iterations);
            byte[] userHash = ((KeyParameter) kdf.generateDerivedMacParameters(8 * numBytes)).getKey();
            pin = null;

            final byte[] firstInputHash = sharedPrefs.getTempPinHash();

            if(Arrays.equals(firstInputHash, userHash)) {

                sharedPrefs.setPasswordEnabled(true);
                sharedPrefs.setPinHash(firstInputHash);

                ((WelcomeActivity)getActivity()).jumpToFingerprint();
            }
            else {
                sharedPrefs.resetPinHash();
                showToastMessage("Passwords do not match!");
                WelcomeFragment4SecurityEnter retryFragment = new WelcomeFragment4SecurityEnter();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.slide_right_left_in, R.animator.slide_right_left_out);
                fragmentTransaction.replace(R.id.activity_welcome_framelayout, retryFragment)
                        .commit();
            }

        }catch (Exception e) {
            Log.e(TAG, "Failed to authenticate password.", e);
        }

    }


    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.fragment_password_confirm_button0:
                handleNumberClick('0');
                break;
            case R.id.fragment_password_confirm_button1:
                handleNumberClick('1');
                break;
            case R.id.fragment_password_confirm_button2:
                handleNumberClick('2');
                break;
            case R.id.fragment_password_confirm_button3:
                handleNumberClick('3');
                break;
            case R.id.fragment_password_confirm_button4:
                handleNumberClick('4');
                break;
            case R.id.fragment_password_confirm_button5:
                handleNumberClick('5');
                break;
            case R.id.fragment_password_confirm_button6:
                handleNumberClick('6');
                break;
            case R.id.fragment_password_confirm_button7:
                handleNumberClick('7');
                break;
            case R.id.fragment_password_confirm_button8:
                handleNumberClick('8');
                break;
            case R.id.fragment_password_confirm_button9:
                handleNumberClick('9');
                break;
            case R.id.fragment_password_confirm_button_back:
                handleBackButtonClick();
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
            processPin(input);
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


    private void showToastMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
