package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

// Class defining the logic for the Home Fragment.
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    // Declare UI elements
    TextView textView1, textView2, textView3, titleTextView, balanceTextView, budgetTextView, btnCaptionTextView;
    ImageButton addExpenseBtn, settingsBtn;
    RecyclerView notificationsRecyclerView;

    int defaultAccountId;
    NotificationsRecyclerViewAdapter notificationsRecyclerViewAdapter;

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
            notificationsRecyclerView = view.findViewById(R.id.fragment_home_notifications_recyclerview);
        } catch(Exception e) {
            Log.e(TAG, "Unable to initialize UI elements of HomeFragment.", e);
        }
        Log.d(TAG, "HomeFragment's UI elements successfully initialized.");

        budgetTextView.setText("");

        sharedPreferences = new CustomSharedPreferences(getContext());
        defaultAccountId = sharedPreferences.getDefaultAccountId();

        try {

            Log.d(TAG, "Attempting to observe default account in database.");
            observeAccountBalance();

            Log.d(TAG, "Attempting to observe expenses in database for filling the notifications list.");
            observeNotifications();

            // Set listeners for UI elements, such as OnClick listeners for buttons
            Log.v(TAG, "Attempting to set UI event listeners.");
            setListeners();

        } catch(Exception e) {
            Log.e(TAG, "Failed to set listeners and observe the database instance.", e);
        }


        Log.d(TAG, "View successfully created.");
        return view;
    }



    private void setListeners() {
        addExpenseBtn.setOnClickListener(view -> {
            // Launch add new expense
            NewExpenseDialog newExpenseDialog = new NewExpenseDialog(getActivity(), defaultAccountId);
            newExpenseDialog.show();
        });

        settingsBtn.setOnClickListener(view -> {
            // Launch settings activity
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> observeAccountBalance());
    }

    public void observeAccountBalance() {

        BankAccountsViewModel accountsViewModel = new BankAccountsViewModel((getActivity().getApplication()));

        if (sharedPreferences.getDefaultIsAllAccounts() || defaultAccountId == -1) {
            Log.d(TAG, "Getting the sum of the balances of all accounts.");

            accountsViewModel.getBankAccountsList().observe(this, bankAccountList -> {
                double totalBalance = 0.0;

                try {
                    for (BankAccount account : bankAccountList) {
                        totalBalance += account.getAccountBalance();
                    }
                }
                catch (NullPointerException e) {
                    Log.e(TAG, "Failed to get bank accounts list from database.", e);
                }

                if (totalBalance < 0.0) {
                    balanceTextView.setText(String.format(Locale.US, "-$%.2f", Math.abs(totalBalance)));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
                else if (totalBalance == 0.0) {
                    balanceTextView.setText(String.format(Locale.US, "$%.2f", totalBalance));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                }
                else {
                    balanceTextView.setText(String.format(Locale.US, "$%.2f", totalBalance));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            });

        }
        else {
            Log.d(TAG, "Getting the balance of the default account (ID: " + defaultAccountId + ").");

            accountsViewModel.getBankAccount(defaultAccountId).observe(this, bankAccount -> {
                if (bankAccount == null) {
                    Log.e(TAG, "Failed to get balance of default account.");

                    balanceTextView.setText(String.format(Locale.US, "$%.2f", 0.0));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorWhite));

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
            });
        }
    }

    public void observeNotifications() {
        // Initialize RecyclerViewAdapter with empty list
        notificationsRecyclerViewAdapter = new NotificationsRecyclerViewAdapter(new ArrayList<Expense>(), getContext());

        // Set the LayoutManager to be the LinearLayoutManager
        // This makes the list display linearly
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set the RecyclerViewAdapter for the NotificationsRecyclerView
        notificationsRecyclerView.setAdapter(notificationsRecyclerViewAdapter);

        // Define the view model to be used
        ExpensesViewModel expensesViewModel = ViewModelProviders.of(this,
                new ExpensesViewModelFactory(this.getActivity().getApplication(),
                        true, null, false)).get(ExpensesViewModel.class);

        // Set the RecyclerView to observe the Expenses table in the DB
        expensesViewModel.getExpensesList().observe(this, expenseList -> {

            if (expenseList != null)
                Collections.sort(expenseList,
                        (e1, e2) -> e1.getNextOccurrence().compareTo(e2.getNextOccurrence()));

            notificationsRecyclerViewAdapter.addItems(expenseList);

        });
    }
}
