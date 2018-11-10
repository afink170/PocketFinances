package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

public class ExpensesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;
    private Integer accountId;
    private ExpenseCategory expenseCategory;
    private Boolean onlyRecurringExpenses;
    private Calendar currentTime;


    public ExpensesViewModelFactory(Application application, @Nullable Integer accountId, @Nullable ExpenseCategory expenseCategory) {
        this.application = application;
        this.accountId = accountId;
        this.expenseCategory = expenseCategory;
    }

    public ExpensesViewModelFactory(Application application, boolean getRecurringExpensesOnly, @Nullable Calendar currentTime) {
        this.application = application;
        this.onlyRecurringExpenses = getRecurringExpensesOnly;
        this.currentTime = currentTime;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if(onlyRecurringExpenses != null)
            return (T) new ExpensesViewModel(application, onlyRecurringExpenses, currentTime);

        return (T) new ExpensesViewModel(application, accountId, expenseCategory);
    }

}
