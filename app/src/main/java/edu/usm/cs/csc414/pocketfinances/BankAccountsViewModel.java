package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class BankAccountsViewModel extends AndroidViewModel {

    private final LiveData<List<BankAccount>> bankAccountsList;

    private FinancesDatabase financesDatabase;


    public BankAccountsViewModel(Application application) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        bankAccountsList = financesDatabase.getBankAccountDao().getAllBankAccounts();
    }


    public LiveData<List<BankAccount>> getBankAccountsList() {
        return bankAccountsList;
    }


    public void deleteItem(BankAccount bankAccount) {
        new deleteAsyncTask(financesDatabase).execute(bankAccount);
    }

    public void insertItem(BankAccount bankAccount) {
        new insertAsyncTask(financesDatabase).execute(bankAccount);
    }


    private static class deleteAsyncTask extends AsyncTask<BankAccount, Void, Void> {

        private FinancesDatabase db;

        deleteAsyncTask(FinancesDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final BankAccount... accounts) {
            db.getBankAccountDao().delete(accounts);
            return null;
        }

    }


    private static class insertAsyncTask extends AsyncTask<BankAccount, Void, Void> {
        private FinancesDatabase db;

        insertAsyncTask(FinancesDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(BankAccount... accounts) {
            db.getBankAccountDao().insert(accounts);
            return null;
        }
    }

}
