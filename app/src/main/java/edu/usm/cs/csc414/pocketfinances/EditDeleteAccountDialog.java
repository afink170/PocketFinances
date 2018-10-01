package edu.usm.cs.csc414.pocketfinances;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditDeleteAccountDialog extends Dialog implements View.OnClickListener {

    private Activity activity;
    private TextView editAccountTextView, deleteAccountTextView, setDefaultAccountTextView;
    private BankAccount account;
    private BankAccountsViewModel viewModel;


    public EditDeleteAccountDialog(@NonNull Activity activity, BankAccountsViewModel viewModel, BankAccount account) {
        super(activity);
        this.activity = activity;
        this.account = account;
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bankaccount_editdelete);
        editAccountTextView = findViewById(R.id.dialog_bankaccount_editdelete_edit_textview);
        deleteAccountTextView = findViewById(R.id.dialog_bankaccount_editdelete_delete_textview);
        setDefaultAccountTextView = findViewById(R.id.dialog_bankaccount_editdelete_setdefault_textview);

        editAccountTextView.setOnClickListener(this);
        deleteAccountTextView.setOnClickListener(this);
        setDefaultAccountTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_bankaccount_editdelete_edit_textview:
                editAccount();
                dismiss();
                break;
            case R.id.dialog_bankaccount_editdelete_delete_textview:
                deleteAccount();
                dismiss();
                break;
            case R.id.dialog_bankaccount_editdelete_setdefault_textview:
                setAsDefaultAccount();
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


    private void setAsDefaultAccount() {
        final String SHARED_PREFS = "shared_prefs";
        final String PREFS_DEFAULT_ACCOUNT = "default_account";

        SharedPreferences sharedPreferences = sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(PREFS_DEFAULT_ACCOUNT, account.getAccountId()).apply();
    }


    private void deleteAccount() {
        //showToastMessage("Delete Account button pressed!");

        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.dialog_bankaccount_confirmdelete, null);

        TextView confirmText = alertDialogView.findViewById(R.id.dialog_bankaccount_confirmdelete_textview);
        confirmText.setText("Are you sure you wish to delete \"" + account.getAccountName() + "?\"");

        confirmDeleteDialog.setCancelable(true);
        confirmDeleteDialog.setView(alertDialogView);

        confirmDeleteDialog.setPositiveButton("Delete", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showToastMessage("Account deleted!");
                viewModel.deleteItem(account);
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
        //showToastMessage("Edit Account button pressed!");

        final AlertDialog.Builder editDialog = new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.dialog_bankaccount_editaccount, null);

        alertDialogView.setMinimumWidth(200);

        final EditText accountName = alertDialogView.findViewById(R.id.dialog_bankaccount_editaccount_accountname);
        final EditText bankName = alertDialogView.findViewById(R.id.dialog_bankaccount_editaccount_bankname);
        final EditText accountType = alertDialogView.findViewById(R.id.dialog_bankaccount_editaccount_accounttype);

        accountName.setText(account.getAccountName());
        bankName.setText(account.getBankName());
        accountType.setText(account.getAccountType());

        editDialog.setTitle(account.getAccountName());
        editDialog.setCancelable(true);
        editDialog.setView(alertDialogView);

        editDialog.setPositiveButton("Save", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String accountNameText = accountName.getText().toString();
                String bankNameText = bankName.getText().toString();
                String accountTypeText = accountType.getText().toString();

                boolean flag = false;

                if (!accountNameText.equals(account.getAccountName())) {
                    account.setAccountName(accountNameText);
                    flag = true;
                }
                if (!bankNameText.equals(account.getBankName())) {
                    account.setBankName(bankNameText);
                    flag = true;
                }
                if (!accountTypeText.equals(account.getAccountType())) {
                    account.setAccountType(accountTypeText);
                    flag = true;
                }

                if (flag)
                    viewModel.updateItem(account);

                showToastMessage("Account updated!");
                dialogInterface.dismiss();
            }
        });

        editDialog.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //showToastMessage("Edit account cancelled!");
                dialogInterface.dismiss();
            }
        });
        editDialog.show();
    }

}
