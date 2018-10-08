package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

// Class defining the logic for the Home Fragment.
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    // Declare UI elements
    TextView textView1, textView2, textView3, titleTextView, balanceTextView, budgetTextView, btnCaptionTextView;
    ImageButton addExpenseBtn, settingsBtn;

    int defaultAccountId;

    CustomSharedPreferences sharedPreferences;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment from fragment_home XML
        Log.d(TAG, "Attempting to create HomeFragment");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {

            // Initialize UI elements
            textView1 = view.findViewById(R.id.fragment_home_textview1);
            textView2 = view.findViewById(R.id.fragment_home_textview2);
            textView3 = view.findViewById(R.id.fragment_home_textview3);
            titleTextView = view.findViewById(R.id.fragment_home_title_textview);
            balanceTextView = view.findViewById(R.id.fragment_home_balance_textview);
            budgetTextView = view.findViewById(R.id.fragment_home_budget_textview);
            addExpenseBtn = view.findViewById(R.id.fragment_home_addexpense_btn);
            settingsBtn = view.findViewById(R.id.fragment_home_settings_btn);
            btnCaptionTextView = view.findViewById(R.id.fragment_home_addexpense_caption);
        } catch(Exception e) {
            Log.e(TAG, "Unable to initialize UI elements of HomeFragment.", e);
        }
        Log.d(TAG, "HomeFragment's UI elements successfully initialized.");

        budgetTextView.setText("");

        sharedPreferences = new CustomSharedPreferences(getContext());
        defaultAccountId = sharedPreferences.getDefaultAccountId();


        if (defaultAccountId != -1) {
            observeAccountBalance();
        }
        else {
            balanceTextView.setText("$0.00");
        }

        // Set listeners for UI elements, such as OnClick listeners for buttons
        setListeners();

        return view;
    }



    private void setListeners() {
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch add new expense
                NewExpenseDialog newExpenseDialog = new NewExpenseDialog(getActivity(), defaultAccountId);
                newExpenseDialog.show();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch settings activity
            }
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                observeAccountBalance();
            }
        });
    }

    public void observeAccountBalance() {
        BankAccountsViewModel accountsViewModel =  new BankAccountsViewModel(getActivity().getApplication());
                accountsViewModel.getBankAccount(defaultAccountId).observe(this, new Observer<BankAccount>() {
            @Override
            public void onChanged(@Nullable BankAccount bankAccount) {
                if (bankAccount == null) {
                    Log.e(TAG, "Failed to get balance of default account.");
                    return;
                }

                if (bankAccount.getAccountBalance() < 0.0) {
                    balanceTextView.setText(String.format(Locale.US, "-$%.2f", Math.abs(bankAccount.getAccountBalance())));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
                else {
                    balanceTextView.setText(String.format(Locale.US, "$%.2f", bankAccount.getAccountBalance()));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            }

        });
    }
}
