package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();

    private final LiveData<List<Expense>> expensesList;

    private FinancesDatabase financesDatabase;


    public ExpensesViewModel(Application application) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        expensesList = financesDatabase.getExpenseDao().getAllExpenses();
    }

    public ExpensesViewModel(Application application, boolean getRecurring, @Nullable Calendar currentTime) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        if (getRecurring) {

            if (currentTime != null) {
                Log.v(TAG, "Querying the database for parent recurring expenses with nextOcurrence before current time.");
                expensesList = financesDatabase.getExpenseDao().getAllRecurringExpensesBeforeDate(currentTime.getTimeInMillis());
            }
            else {
                Log.v(TAG, "Querying the database for parent recurring expenses.");
                expensesList = financesDatabase.getExpenseDao().getAllRecurringExpenses();
            }
        }
        else {
            Log.v(TAG, "Querying the database for all expenses.");
            expensesList = financesDatabase.getExpenseDao().getAllExpenses();
        }
    }


    public ExpensesViewModel(Application application, int expenseId) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        expensesList = financesDatabase.getExpenseDao().getExpense(expenseId);
    }


    public ExpensesViewModel(Application application, @Nullable Integer accountId, @Nullable ExpenseCategory category) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        if (category == null && accountId != null) {
            Log.v(TAG, "Querying the database for all expenses with accountId="+accountId+".");
            expensesList = financesDatabase.getExpenseDao().getExpensesOnAccount(accountId);
        }
        else if (accountId == null && category != null) {
            Log.v(TAG, "Querying the database for all expenses with category="+category.getText()+".");
            expensesList = financesDatabase.getExpenseDao().getExpensesOnCategoryValue(category.getValue());
        }
        else if (accountId != null) {
            Log.v(TAG, "Querying the database for all expenses with category="+category.getText()+" and accountId="+accountId+".");
            expensesList = financesDatabase.getExpenseDao().getExpensesOnCategoryAndAccount(category.getValue(), accountId);
        }
        else {
            Log.v(TAG, "Querying the database for all expenses.");
            expensesList = financesDatabase.getExpenseDao().getAllExpenses();
        }
    }


    public LiveData<List<Expense>> getExpensesList() {
        return expensesList;
    }




    public void deleteItem(Expense... expenses) {
        new deleteAsyncTask(financesDatabase).execute(expenses);
    }

    public void insertItem(Expense... expenses) {
        new insertAsyncTask(financesDatabase).execute(expenses);
    }

    public void updateItem(Expense... expenses) {
        new updateAsyncTask(financesDatabase).execute(expenses);
    }


    private static class deleteAsyncTask extends AsyncTask<Expense, Void, Void> {

        private FinancesDatabase db;

        deleteAsyncTask(FinancesDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Expense... expenses) {
            db.getExpenseDao().delete(expenses);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Expense, Void, Void> {
        private FinancesDatabase db;

        insertAsyncTask(FinancesDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            db.getExpenseDao().insert(expenses);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Expense, Void, Void> {
        private FinancesDatabase db;

        updateAsyncTask(FinancesDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Expense... expenses) {
            db.getExpenseDao().update(expenses);
            return null;
        }
    }
}
