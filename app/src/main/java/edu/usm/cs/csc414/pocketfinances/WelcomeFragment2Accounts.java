package edu.usm.cs.csc414.pocketfinances;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class WelcomeFragment2Accounts extends Fragment {

    private static final String TAG = "WelcomeFragment2";

    private ImageButton addAccountButton;
    private BankAccountsViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Attempting to create WelcomeFragment2.");
        View view = inflater.inflate(R.layout.fragment_welcome_2_accounts, container, false);

        // Initialize the ViewModel
        viewModel = ViewModelProviders.of(this).get(BankAccountsViewModel.class);

        addAccountButton = view.findViewById(R.id.fragment_welcome_2_newaccount_button);

        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder newAccountDialog = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertDialogView = inflater.inflate(R.layout.dialog_bankaccount_new, null);

                final EditText accountName = alertDialogView.findViewById(R.id.dialog_bankaccount_new_accountname);
                final EditText bankName = alertDialogView.findViewById(R.id.dialog_bankaccount_new_bankname);
                final EditText accountType = alertDialogView.findViewById(R.id.dialog_bankaccount_new_accounttype);
                final EditText accountBalance = alertDialogView.findViewById(R.id.dialog_bankaccount_new_balance);

                newAccountDialog.setTitle("New Bank Account");
                newAccountDialog.setCancelable(true);
                newAccountDialog.setView(alertDialogView);

                newAccountDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String accountNameText = accountName.getText().toString();
                            String bankNameText = bankName.getText().toString();
                            String accountTypeText = accountType.getText().toString();
                            String accountBalanceText = accountBalance.getText().toString();

                            if (accountNameText.equals("") || bankNameText.equals("")
                                    || accountTypeText.equals("") || accountBalanceText.equals("")) {
                                showToastMessage("Error: All fields must be filled.");
                            }
                            else {
                                BankAccount newAccount = new BankAccount(bankNameText, accountNameText,
                                        accountTypeText, Double.parseDouble(accountBalanceText));

                                new CustomSharedPreferences(getContext()).setDefaultAccountId(newAccount.getAccountId());

                                viewModel.insertItem(newAccount);

                                dialogInterface.cancel();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to get user input from AlertDialog.", e);
                            showToastMessage("Error: Failed to get user input.");
                            dialogInterface.cancel();
                        }
                    }
                });

                newAccountDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                newAccountDialog.show();
            }
        });


        return view;
    }


    private void showToastMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), message,
                Toast.LENGTH_LONG).show();
    }

}
