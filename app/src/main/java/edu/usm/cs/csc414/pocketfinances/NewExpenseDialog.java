package edu.usm.cs.csc414.pocketfinances;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewExpenseDialog extends Dialog {

    private Activity activity;
    private int accountId;
    private Calendar date;
    private List<BankAccount> bankAccounts;

    // UI elements
    private Spinner categorySpinner, recurrenceSpinner, bankAccountSpinner;
    private EditText expenseTitle, expenseAmount, expenseDate;
    private CheckBox isRecurringCheckBox;
    private TextView isDeposit, isDeduction, cancelBtn, saveBtn;
    private int depositOrDeduction = Expense.DEDUCT;


    public NewExpenseDialog(@NonNull Activity activity, int accountId) {
        super(activity);
        this.activity = activity;
        this.accountId = accountId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_expense_new);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        categorySpinner = findViewById(R.id.dialog_expense_new_expensecategory_spinner);
        recurrenceSpinner = findViewById(R.id.dialog_expense_new_expenserecurrence_spinner);
        bankAccountSpinner = findViewById(R.id.dialog_expense_new_bankaccount_spinner);
        expenseTitle = findViewById(R.id.dialog_expense_new_expensetitle);
        expenseAmount = findViewById(R.id.dialog_expense_new_expenseamount);
        expenseDate = findViewById(R.id.dialog_expense_new_expensedate);
        isRecurringCheckBox = findViewById(R.id.dialog_expense_new_expenseisrecurring);
        isDeposit = findViewById(R.id.dialog_expense_new_toggledeposit);
        isDeduction = findViewById(R.id.dialog_expense_new_toggleexpense);
        cancelBtn = findViewById(R.id.dialog_expense_new_cancel);
        saveBtn = findViewById(R.id.dialog_expense_new_save);

        date = Calendar.getInstance();

        observeAccounts();
        setSpinnerAdapters();
        setListeners();
        setInitialStates();
    }



    private void setSpinnerAdapters() {
        // get array of expense categories
        ExpenseCategory[] categoryArray = ExpenseCategory.values();
        ArrayList<String> categoryArrayList = new ArrayList<>();

        for (ExpenseCategory category : categoryArray) {
            categoryArrayList.add(category.getText());
        }

        // set array adapter for expense categories
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categoryArrayList);



        // get array of expense recurrence rates
        RecurrenceRate[] recurrenceRateArray = RecurrenceRate.values();
        ArrayList<String> recurrenceRateArrayList = new ArrayList<>();

        for (RecurrenceRate rate : recurrenceRateArray) {
            recurrenceRateArrayList.add(rate.getText());
        }

        // set array adapter for expense recurrence rates
        ArrayAdapter<String> recurrenceRateArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, recurrenceRateArrayList);


        categorySpinner.setAdapter(categoryArrayAdapter);
        recurrenceSpinner.setAdapter(recurrenceRateArrayAdapter);
    }



    private void setInitialStates() {
        isRecurringCheckBox.setChecked(false);

        recurrenceSpinner.setSelection(0);
        recurrenceSpinner.setEnabled(isRecurringCheckBox.isChecked());

        categorySpinner.setSelection(ExpenseCategory.values().length - 1);

        if (bankAccounts != null) {
            bankAccountSpinner.setSelection(0);
            for (int  i = 0; i < bankAccounts.size(); i++) {
                if (accountId == bankAccounts.get(i).getAccountId()) {
                    bankAccountSpinner.setSelection(i);
                }

            }
        }


        expenseDate.setText(String.format(Locale.US, "%02d/%02d/%4d",
                date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.YEAR)));

        setDepositDeductionColors();
    }


    private void setListeners() {

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tryToSave()) dismiss();
            }
        });

        isRecurringCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                recurrenceSpinner.setEnabled(b);
            }
        });

        isDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depositOrDeduction = Expense.DEPOSIT;
                setDepositDeductionColors();
            }
        });

        isDeduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depositOrDeduction = Expense.DEDUCT;
                setDepositDeductionColors();
            }
        });

        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        expenseDate.setText(String.format(Locale.US, "%2d/%2d/%4d",
                                date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.YEAR)));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        expenseAmount.addTextChangedListener(new MoneyTextWatcher(expenseAmount));
    }



    private void setDepositDeductionColors() {
        if (depositOrDeduction == Expense.DEPOSIT) {
            isDeposit.setBackgroundColor(activity.getResources().getColor(R.color.colorGreen));
            isDeduction.setBackgroundColor(activity.getResources().getColor(R.color.colorDisabledButton));
        }
        else if (depositOrDeduction == Expense.DEDUCT) {
            isDeposit.setBackgroundColor(activity.getResources().getColor(R.color.colorDisabledButton));
            isDeduction.setBackgroundColor(activity.getResources().getColor(R.color.colorRed));
        }
    }


    private void showToastMessage(String message) {
        Toast.makeText(activity.getBaseContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    private class UpdateAccountInfo {

        public int accountId;
        public double amount;

        public UpdateAccountInfo(int accountId, double amount) {
            this.accountId = accountId;
            this.amount = amount;
        }
    }



    private static class AsyncInsertExpense extends AsyncTask<Expense, Void, Void> {
        FinancesDatabase db;

        public AsyncInsertExpense(Context context) {
            db = FinancesDatabase.getDatabase(context);
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            db.getExpenseDao().insert(expenses);

            return null;
        }
    }

    private static class AsyncUpdateBalance extends AsyncTask<NewExpenseDialog.UpdateAccountInfo, Void, Void> {
        FinancesDatabase db;

        public AsyncUpdateBalance(Context context) {
            db = FinancesDatabase.getDatabase(context);
        }

        @Override
        protected Void doInBackground(NewExpenseDialog.UpdateAccountInfo... updateAccountInfos) {
            db.getBankAccountDao().updateBalance(updateAccountInfos[0].accountId, updateAccountInfos[0].amount);

            return null;
        }
    }


    private void observeAccounts() {
        BankAccountsViewModel accountsViewModel =  new BankAccountsViewModel(activity.getApplication());
        accountsViewModel.getBankAccountsList().observe((LifecycleOwner) activity, new Observer<List<BankAccount>>() {
            @Override
            public void onChanged(@Nullable List<BankAccount> bankAccountsList) {
                bankAccounts = bankAccountsList;

                ArrayList<String> bankAccountArrayList = new ArrayList<>();

                if (bankAccounts != null) {
                    for (BankAccount account : bankAccounts) {
                        bankAccountArrayList.add(account.getAccountName());
                    }
                }
                else {
                    showToastMessage("You must add a bank account first!");
                    cancel();
                }

                ArrayAdapter<String> bankAccountArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, bankAccountArrayList);
                bankAccountSpinner.setAdapter(bankAccountArrayAdapter);

                for (int  i = 0; i < bankAccounts.size(); i++) {
                    if (accountId == bankAccounts.get(i).getAccountId()) {
                        bankAccountSpinner.setSelection(i);
                    }

                }
            }

        });
    }


    private boolean tryToSave() {
        if (expenseAmount.getText().toString().isEmpty()) {
            showToastMessage("Error: Expense title and amount fields must be filled!");
            return false;
        }

        String expenseTitleText = expenseTitle.getText().toString();
        double expenseAmountText = Double.parseDouble(expenseAmount.getText().toString().substring(1).replace(",", ""));
        boolean isRecurring = isRecurringCheckBox.isChecked();
        RecurrenceRate recurrenceRate = RecurrenceRate.getRateFromText(recurrenceSpinner.getSelectedItem().toString());
        ExpenseCategory category = ExpenseCategory.getCategoryFromText(categorySpinner.getSelectedItem().toString());
        int activeAccount = -1;
        for (int j = 0; j < bankAccounts.size(); j++) {
            if (bankAccountSpinner.getSelectedItem() == bankAccounts.get(j).getAccountName())
                activeAccount = bankAccounts.get(j).getAccountId();
        }

        double balanceChangeAmount = expenseAmountText * depositOrDeduction;

        if (expenseTitleText.isEmpty() || expenseAmountText <= 0.0) {
            showToastMessage("Error: Expense title and amount fields must be filled!");
            return false;
        } else if (activeAccount == -1) {
            showToastMessage("Error: No bank account chosen for the expense!");
            return false;
        } else {
            Expense newExpense = new Expense(activeAccount, expenseTitleText, category,
                    expenseAmountText, date, depositOrDeduction, isRecurring, recurrenceRate);

            new AsyncInsertExpense(getContext()).execute(newExpense);
            new AsyncUpdateBalance(getContext()).execute(new UpdateAccountInfo(activeAccount, balanceChangeAmount));

            return true;
        }
    }
}
