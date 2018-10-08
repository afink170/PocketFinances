package edu.usm.cs.csc414.pocketfinances;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.Arrays;

public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivity";

    // Declare Ui elements
    TextView btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView input0, input1, input2, input3;
    ImageButton fingerprintBtn, backBtn;
    GridLayout gridLayout;

    // Stores user input
    char[] input = {' ', ' ', ' ', ' '};



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize UI elements
        btn0 = findViewById(R.id.activity_password_button0);
        btn1 = findViewById(R.id.activity_password_button1);
        btn2 = findViewById(R.id.activity_password_button2);
        btn3 = findViewById(R.id.activity_password_button3);
        btn4 = findViewById(R.id.activity_password_button4);
        btn5 = findViewById(R.id.activity_password_button5);
        btn6 = findViewById(R.id.activity_password_button6);
        btn7 = findViewById(R.id.activity_password_button7);
        btn8 = findViewById(R.id.activity_password_button8);
        btn9 = findViewById(R.id.activity_password_button9);
        input0 = findViewById(R.id.activity_password_text0);
        input1 = findViewById(R.id.activity_password_text1);
        input2 = findViewById(R.id.activity_password_text2);
        input3 = findViewById(R.id.activity_password_text3);
        fingerprintBtn = findViewById(R.id.activity_password_button_fingerprint);
        backBtn = findViewById(R.id.activity_password_button_back);
        gridLayout = findViewById(R.id.activity_password_gridlayout);

        setUiListeners();
        setNavMenuPadding();

        input0.setText("");
        input1.setText("");
        input2.setText("");
        input3.setText("");
    }


    private void authenticatePin(char[] pin) {
        // number of bytes of salt
        final int numBytes = 20;

        // number of hashing iterations.
        // The more iterations, the more secure, but worse performance
        final int iterations = 100;

        // get salt and password hash from shared prefs
        final byte[] salt = new CustomSharedPreferences(getApplicationContext()).getSalt();
        final byte[] savedHash = new CustomSharedPreferences(getApplicationContext()).getPinHash();

        try {
            // hash user input password
            PKCS5S2ParametersGenerator kdf = new PKCS5S2ParametersGenerator();
            kdf.init(new String(pin).getBytes(), salt, iterations);
            byte[] checkHash = ((KeyParameter) kdf.generateDerivedMacParameters(8 * numBytes)).getKey();

            // check whether it matches saved password hash
            if (Arrays.equals(checkHash, savedHash)) {
                // if correct password, launch main activity
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();

            }
            else {
                // if not correct, clear input and try again
                Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();

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
    }


    private void setNavMenuPadding() {
        Log.v(TAG, "Checking for soft keys.");
        try {
            int softKeyBarHeight = getSoftButtonsBarHeight();
            Log.d(TAG, "Soft key bar height: " + softKeyBarHeight);

            gridLayout.setPadding(0,0,0, softKeyBarHeight);
            /*
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams = (GridLayout.LayoutParams) gridLayout.getLayoutParams();
            layoutParams.bottomMargin += softKeyBarHeight;
            gridLayout.setLayoutParams(layoutParams);
            */

        } catch(Exception e) {
            Log.e(TAG, "Error in checking presence of soft keys and adapting UI accordingly.", e);
        }
    }


    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        return realHeight - usableHeight;
    }
}
