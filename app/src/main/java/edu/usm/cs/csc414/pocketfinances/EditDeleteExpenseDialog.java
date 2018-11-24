package edu.usm.cs.csc414.pocketfinances;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import timber.log.Timber;

public class EditDeleteExpenseDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TextView editExpenseTextView, deleteExpenseTextView;
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
        editExpenseTextView = findViewById(R.id.dialog_expense_editdelete_edit_textview);
        deleteExpenseTextView = findViewById(R.id.dialog_expense_editdelete_delete_textview);

        editExpenseTextView.setOnClickListener(this);
        deleteExpenseTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_expense_editdelete_edit_textview:
                editExpense();
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


    private void deleteExpense() {

        Timber.v("Begin deleteExpense transaction.");

        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.dialog_expense_confirmdelete, null);

        TextView confirmText = alertDialogView.findViewById(R.id.dialog_expense_confirmdelete_textview);
        confirmText.setText(String.format("Are you sure you wish to delete \"%s?\"", expense.getTitle()));

        confirmDeleteDialog.setCancelable(true);
        confirmDeleteDialog.setView(alertDialogView);

        confirmDeleteDialog.setPositiveButton("Delete", (dialogInterface, i) -> {
                //showToastMessage("Expense deleted!");
                Timber.v("deleteExpense confirmed!");
                expensesViewModel.deleteItem(expense);
                if (!(expense.getIsFirstOccurrence() && expense.getIsRecurring()))
                    new AsyncUpdateBalance(getContext()).execute(new UpdateAccountInfo(expense.getAccountId(), expense.getDepositOrDeduct(), expense.getAmount()));
                dialogInterface.dismiss();
            });

        confirmDeleteDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                Timber.v("deleteExpense canceled!");
                dialogInterface.dismiss();
            });
        confirmDeleteDialog.show();
    }

    private void editExpense() {
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
