package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {

    private final LiveData<List<Expense>> expensesList;

    private FinancesDatabase financesDatabase;


    public ExpensesViewModel(Application application) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        expensesList = financesDatabase.getExpenseDao().getAllExpenses();
    }


    public ExpensesViewModel(Application application, int expenseId) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        expensesList = financesDatabase.getExpenseDao().getExpense(expenseId);
    }


    public ExpensesViewModel(Application application, @Nullable Integer accountId, @Nullable ExpenseCategory category) {
        super(application);

        financesDatabase = FinancesDatabase.getDatabase(this.getApplication());

        if (category == null && accountId != null)
            expensesList = financesDatabase.getExpenseDao().getExpensesOnAccount(accountId);
        else if (accountId == null && category != null)
            expensesList = financesDatabase.getExpenseDao().getExpensesOnCategoryValue(category.getValue());
        else if (accountId != null)
            expensesList = financesDatabase.getExpenseDao().getExpensesOnCategoryAndAccount(category.getValue(), accountId);
        else
            expensesList = financesDatabase.getExpenseDao().getAllExpenses();
    }


    public LiveData<List<Expense>> getExpensesList() {
        return expensesList;
    }


    public void deleteItem(Expense expense) {
        new deleteAsyncTask(financesDatabase).execute(expense);
    }

    public void insertItem(Expense expense) {
        new insertAsyncTask(financesDatabase).execute(expense);
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
}
