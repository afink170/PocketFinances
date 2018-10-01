package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import java.util.Calendar;


@Entity (tableName = "RecurringExpense")
public class RecurringExpense extends Expense {


    @ColumnInfo (name="first_expense_id")
    private int firstExpenseId;


    public RecurringExpense(int accountId, String title, ExpenseCategory category, double amount, Calendar date, int depositOrDeduct,
                        boolean isRecurring, RecurrenceRate recurrenceRate, int firstExpenseId) {
        super (accountId, title, category, amount, date, depositOrDeduct, isRecurring, recurrenceRate);
        this.firstExpenseId = firstExpenseId;
    }

    public int getFirstExpenseId() {
        return firstExpenseId;
    }

    public void setFirstExpenseId(int firstExpenseId) { this.firstExpenseId = firstExpenseId; }

}
