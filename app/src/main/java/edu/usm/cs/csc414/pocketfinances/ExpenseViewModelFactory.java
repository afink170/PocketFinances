package edu.usm.cs.csc414.pocketfinances;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.Nullable;

public class ExpenseViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;
    private Integer accountId;
    private ExpenseCategory expenseCategory;


    public ExpenseViewModelFactory(Application application, @Nullable Integer accountId, @Nullable ExpenseCategory expenseCategory) {
        this.application = application;
        this.accountId = accountId;
        this.expenseCategory = expenseCategory;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ExpensesViewModel(application, accountId, expenseCategory);
    }

}
