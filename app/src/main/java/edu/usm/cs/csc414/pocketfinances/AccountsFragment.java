package edu.usm.cs.csc414.pocketfinances;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AccountsFragment extends Fragment {

    // Declare UI elements
    TextView titleText;
    ImageButton addAccountBtn;
    RecyclerView recyclerView;
    AccountsRecyclerViewAdapter recyclerViewAdapter;
    BankAccountsViewModel viewModel;
    AlertDialog.Builder newAccountDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Attempting to create AccountsFragment.");
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        // Initialize UI elements for fragment
        titleText = view.findViewById(R.id.fragment_accounts_title_textview);
        addAccountBtn = view.findViewById(R.id.fragment_accounts_add_btn);


        // Initialize the RecyclerView for holding the list of accounts
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_accounts_recyclerview);

        // Create the RecyclerViewAdapter for the RecyclerView
        recyclerViewAdapter = new AccountsRecyclerViewAdapter(new ArrayList<>());

        // Set the LayoutManager to be the LinearLayoutManager
        // This makes the list display linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Attach the RecyclerViewAdapter to the RecyclerView
        recyclerView.setAdapter(recyclerViewAdapter);


        // Initialize the ViewModel
        viewModel = ViewModelProviders.of(this).get(BankAccountsViewModel.class);

        // Set the ViewModel to observe the live BankAccount data list
        //      so that the UI will update whenever the data or config changes
        viewModel.getBankAccountsList().observe(getActivity(), bankAccountList ->
                recyclerViewAdapter.addItems(bankAccountList));


        // set the listeners for any UI elements that need them
        setListeners();


        return view;
    }




    // Method for setting all the listeners
    private void setListeners() {
        addAccountBtn.setOnClickListener(view -> {

            newAccountDialog = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View alertDialogView = inflater.inflate(R.layout.dialog_bankaccount_new, null);

            final EditText accountName = alertDialogView.findViewById(R.id.dialog_bankaccount_new_accountname);
            final EditText bankName = alertDialogView.findViewById(R.id.dialog_bankaccount_new_bankname);
            final EditText accountType = alertDialogView.findViewById(R.id.dialog_bankaccount_new_accounttype);
            final EditText accountBalance = alertDialogView.findViewById(R.id.dialog_bankaccount_new_balance);

            newAccountDialog.setTitle("New Bank Account");
            newAccountDialog.setCancelable(true);
            newAccountDialog.setView(alertDialogView);

            newAccountDialog.setPositiveButton("Save", (dialogInterface, i) -> {
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

                        viewModel.insertItem(newAccount);

                        dialogInterface.cancel();
                    }
                } catch (Exception e) {
                    Timber.e(e, "Failed to get user input from AlertDialog.");
                    showToastMessage("Error: Failed to get user input.");
                    dialogInterface.cancel();
                }
            });

            newAccountDialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

            newAccountDialog.show();
        });


        recyclerViewAdapter.setOnClickListener(view -> {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            BankAccount clickedAccount = viewModel.getBankAccountsList().getValue().get(itemPosition);

            Bundle bundle = new Bundle();
            bundle.putInt("activeAccountId", clickedAccount.getAccountId());

            ExpensesFragment expensesFragment = new ExpensesFragment();
            expensesFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.slide_left_right_in, R.animator.slide_left_right_out);
            fragmentTransaction.replace(R.id.activity_main_framelayout, expensesFragment)
                    .commit();

        });

        recyclerViewAdapter.setOnLongClickListener(view -> {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            BankAccount clickedAccount = viewModel.getBankAccountsList().getValue().get(itemPosition);

            EditDeleteAccountDialog dialog = new EditDeleteAccountDialog(getActivity(), viewModel, clickedAccount);
            dialog.show();

            return true;
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(getActivity().getBaseContext(), message,
                Toast.LENGTH_LONG).show();
    }



}
