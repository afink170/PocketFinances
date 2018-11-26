package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

// Class defining the logic for the Home Fragment.
public class HomeFragment extends Fragment {

    // Declare UI elements
    TextView textView1, textView2, textView3, titleTextView, balanceTextView, budgetTextView, btnCaptionTextView;
    ImageButton addExpenseBtn, settingsBtn;
    RecyclerView notificationsRecyclerView;

    int defaultAccountId;
    volatile boolean accountsExist = true;

    NotificationsRecyclerViewAdapter notificationsRecyclerViewAdapter;

    CustomSharedPreferences sharedPreferences;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment from fragment_home XML
        Timber.d("Attempting to create HomeFragment");
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
            Timber.e(e, "Unable to initialize UI elements of HomeFragment.");
        }

        budgetTextView.setText("");

        sharedPreferences = new CustomSharedPreferences(getContext());
        defaultAccountId = sharedPreferences.getDefaultAccountId();

        setThemeColors();

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> observeAccountBalance());

        try {

            Timber.d("Attempting to observe default account in database.");
            observeAccountBalance();

            Timber.d("Attempting to observe expenses in database for filling the notifications list.");
            observeNotifications();

            Timber.d("Attempting to observe total spending from expenses in the database.");
            observeSpending();

            // Set listeners for UI elements, such as OnClick listeners for buttons
            Timber.v("Attempting to set UI event listeners.");
            setListeners();

        } catch(Exception e) {
            Timber.e(e, "Failed to set listeners and observe the database instance.");
        }

        return view;
    }



    private void setListeners() {
        addExpenseBtn.setOnClickListener(view -> {
            if (accountsExist) {
                // Launch add new expense
                NewExpenseDialog newExpenseDialog = new NewExpenseDialog(getActivity(), defaultAccountId);
                newExpenseDialog.show();
            }
            else {
                showToastMessage("You must add an account first!");
            }
        });

        settingsBtn.setOnClickListener(view -> {
            // Launch settings activity
        });
    }


    private void setThemeColors() {
        int theme = sharedPreferences.getActivityTheme();

        if (theme == CustomSharedPreferences.THEME_DARK) {
            titleTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            addExpenseBtn.setImageResource(R.drawable.ic_add_light);
            btnCaptionTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            settingsBtn.setImageResource(R.drawable.ic_settings_light);
        }
        else if (theme == CustomSharedPreferences.THEME_LIGHT) {
            titleTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));
            addExpenseBtn.setImageResource(R.drawable.ic_add_dark);
            btnCaptionTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));
            settingsBtn.setImageResource(R.drawable.ic_settings_dark);
        }
    }

    private void observeAccountBalance() {

        BankAccountsViewModel accountsViewModel = new BankAccountsViewModel((getActivity().getApplication()));

        if (sharedPreferences.getDefaultIsAllAccounts() || defaultAccountId == -1) {
            Timber.d("Getting the sum of the balances of all accounts.");

            accountsViewModel.getBankAccountsList().observe(this, bankAccountList -> {
                double totalBalance = 0.0;

                try {
                    if (bankAccountList.isEmpty()) {
                        accountsExist = false;
                        setListeners();
                    }
                    else {
                        for (BankAccount account : bankAccountList) {
                            totalBalance += account.getAccountBalance();
                        }
                    }
                }
                catch (NullPointerException e) {
                    Timber.e(e, "Failed to get bank accounts list from database.");
                }

                if (totalBalance < 0.0) {
                    balanceTextView.setText(String.format(Locale.US, "-$%.2f", Math.abs(totalBalance)));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
                else if (totalBalance == 0.0) {
                    balanceTextView.setText(String.format(Locale.US, "$%.2f", totalBalance));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                }
                else {
                    balanceTextView.setText(String.format(Locale.US, "$%.2f", totalBalance));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            });

        }
        else {
            Timber.d("Getting the balance of the default account (ID: %d).", defaultAccountId);

            accountsViewModel.getBankAccount(defaultAccountId).observe(this, bankAccount -> {
                if (bankAccount == null && defaultAccountId != -1) {
                    Timber.e("Failed to get balance of default account (ID: %d).  Bank account is null.", defaultAccountId);

                    balanceTextView.setText(String.format(Locale.US, "$%.2f", 0.0));
                    balanceTextView.setTextColor(getResources().getColor(R.color.colorDarkGrey));

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

    private void observeNotifications() {
        // Initialize RecyclerViewAdapter with empty list
        notificationsRecyclerViewAdapter = new NotificationsRecyclerViewAdapter(new ArrayList<>(), getContext());

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

            if (expenseList != null) {
                Collections.sort(expenseList,
                        (e1, e2) -> e1.getNextOccurrence().compareTo(e2.getNextOccurrence()));

                List<Expense> trimmedExpenseList = new ArrayList<>();

                // Only display the first 10 notifications.
                for (int i = 0; i < expenseList.size() && i < 10; i++)
                    trimmedExpenseList.add(expenseList.get(i));

                notificationsRecyclerViewAdapter.addItems(trimmedExpenseList);
            }

        });
    }


    private void observeSpending() {
        ExpensesViewModel expensesViewModel = new ExpensesViewModel(getActivity().getApplication());

        expensesViewModel.getExpensesList().observe(this, expenseList -> {

            double spending = 0.0;
            Calendar currentTime = Calendar.getInstance();

            try {
                for (Expense expense : expenseList) {
                    // Check if expense is a deduction, that it is not a parent recurring expense, and that is occurred this month.
                    if (expense.getDepositOrDeduct() == Expense.DEDUCT &&
                            (!expense.getIsRecurring() || !expense.getIsFirstOccurrence()) &&
                            currentTime.get(Calendar.MONTH) == expense.getDate().get(Calendar.MONTH) &&
                            currentTime.get(Calendar.YEAR) == expense.getDate().get(Calendar.YEAR))
                        spending += expense.getAmount();
                }
            }
            catch (NullPointerException e) {
                Timber.e(e, "Failed to get total spending!");
            }

            budgetTextView.setText(ExpenseTypeConverters.amountToString(spending));
        });
    }

    private void showToastMessage(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
