package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Calendar;
import java.util.Date;


@Entity (tableName = "Subscription")
public class Subscription extends Expense {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name ="expense_id")
    private int expenseId; //foreign key

    @ColumnInfo (name="first_expense_id")
    private int firstExpenseId;


    public Subscription(int accountId, String title, ExpenseCategory category, double amount, Calendar date, int depositOrDeduct,
                        boolean isRecurring, RecurrenceRate recurrenceRate, int expenseId, int firstExpenseId) {
        super (accountId, title, category, amount, date, depositOrDeduct, isRecurring, recurrenceRate);
        this.expenseId = expenseId;
        this.firstExpenseId = firstExpenseId;
    }

    public void setExpenseId(int expenseId) { this.expenseId = expenseId; }

    public int getExpenseId() {
        return expenseId;
    }

    public int getFirstExpenseId() {
        return firstExpenseId;
    }

    public void setFirstExpenseId(int firstExpenseId) { this.firstExpenseId = firstExpenseId; }

}
