package edu.usm.cs.csc414.pocketfinances;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class EditDeleteExpenseDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TextView editAccountTextView, deleteAccountTextView;
    private Expense expense;
    private ExpensesViewModel expensesViewModel;

    public EditDeleteExpenseDialog(@NonNull Activity activity, ExpensesViewModel expensesViewModel, Expense expense) {
        super(activity);
        this.activity = activity;
        this.expense = expense;
        this.expensesViewModel = expensesViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_expense_editdelete);
        editAccountTextView = findViewById(R.id.dialog_expense_editdelete_edit_textview);
        deleteAccountTextView = findViewById(R.id.dialog_expense_editdelete_delete_textview);

        editAccountTextView.setOnClickListener(this);
        deleteAccountTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_expense_editdelete_edit_textview:
                editAccount();
                dismiss();
                break;
            case R.id.dialog_expense_editdelete_delete_textview:
                deleteExpense();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    private void showToastMessage(String message) {
        Toast.makeText(activity.getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }


    private void deleteExpense() {

        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.dialog_bankaccount_confirmdelete, null);

        TextView confirmText = alertDialogView.findViewById(R.id.dialog_bankaccount_confirmdelete_textview);
        confirmText.setText("Are you sure you wish to delete \"" + expense.getTitle() + "?\"");

        confirmDeleteDialog.setCancelable(true);
        confirmDeleteDialog.setView(alertDialogView);

        confirmDeleteDialog.setPositiveButton("Delete", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //showToastMessage("Expense deleted!");
                expensesViewModel.deleteItem(expense);
                new AsyncUpdateBalance(getContext()).execute(new UpdateAccountInfo(expense.getAccountId(), expense.getDepositOrDeduct(), expense.getAmount()));
                dialogInterface.dismiss();
            }
        });

        confirmDeleteDialog.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.show();
    }

    private void editAccount() {
        EditExpenseDialog editExpenseDialog = new EditExpenseDialog(activity, expense);
        editExpenseDialog.show();
    }

    private class UpdateAccountInfo {
        public int accountId;
        public int depositOrDeduction;
        public double amount;

        public UpdateAccountInfo(int accountId, int depositOrDeduction, double amount) {
            this.accountId = accountId;
            this.depositOrDeduction = depositOrDeduction;
            this.amount = amount;
        }
    }

    private static class AsyncUpdateBalance extends AsyncTask<UpdateAccountInfo, Void, Void> {
        FinancesDatabase db;


        public AsyncUpdateBalance(Context context) {
            db = FinancesDatabase.getDatabase(context);
        }

        @Override
        protected Void doInBackground(UpdateAccountInfo... updateAccountInfos) {
            int accountId = updateAccountInfos[0].accountId;
            int depositOrDeduction = updateAccountInfos[0].depositOrDeduction;
            double amount = updateAccountInfos[0].amount;

            amount *= -1.0 * depositOrDeduction;

            db.getBankAccountDao().updateBalance(accountId, amount);

            return null;
        }
    }
}
