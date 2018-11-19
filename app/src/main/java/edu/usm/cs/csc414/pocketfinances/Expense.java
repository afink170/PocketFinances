package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Calendar;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Expense",
        indices = @Index(value = "account_id"),
        foreignKeys = @ForeignKey(
        entity = BankAccount.class,
        parentColumns = "account_id",
        childColumns = "account_id",
        onDelete = CASCADE))

public class Expense {

    public static final int DEPOSIT = 1;
    public static final int DEDUCT = -1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="expense_id")
    private int expenseId;

    @ColumnInfo(name="account_id")
    private int accountId; //Foreign Key

    @ColumnInfo(name="title")
    private String title;

    @TypeConverters(CategoryConverter.class)
    @ColumnInfo(name="category")
    private ExpenseCategory category;

    @ColumnInfo(name="amount")
    private double amount;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name="date")
    private Calendar date;

    @ColumnInfo(name="deposit_or_deduct")
    private int depositOrDeduct;

    @ColumnInfo(name="is_recurring")
    private boolean isRecurring;

    @TypeConverters(RecurrenceRateConverter.class)
    @ColumnInfo(name="recurrence_rate")
    private RecurrenceRate recurrenceRate;

    @ColumnInfo(name="is_first_occurrence")
    private boolean isFirstOccurrence;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name="next_occurrence")
    private Calendar nextOccurrence;


    public Expense(int accountId, String title, ExpenseCategory category, double amount, Calendar date, int depositOrDeduct, boolean isRecurring, RecurrenceRate recurrenceRate) {
        this.accountId = accountId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.depositOrDeduct = depositOrDeduct;
        this.isRecurring = isRecurring;
        this.recurrenceRate = recurrenceRate;
        this.isFirstOccurrence = true;
        this.nextOccurrence = date;

    }

    @Ignore
    public Expense(int accountId, String title, ExpenseCategory category, double amount, Calendar date, int depositOrDeduct, boolean isRecurring, RecurrenceRate recurrenceRate, boolean isFirstOccurrence, Calendar nextOccurrence) {
        this.accountId = accountId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.depositOrDeduct = depositOrDeduct;
        this.isRecurring = isRecurring;
        this.recurrenceRate = recurrenceRate;
        this.isFirstOccurrence = isFirstOccurrence;
        this.nextOccurrence = nextOccurrence;

    }

    @Ignore
    public Expense(Expense expense) {
        this.accountId = expense.getAccountId();
        this.title = expense.getTitle();
        this.category = expense.getCategory();
        this.amount = expense.getAmount();
        this.date = expense.getDate();
        this.depositOrDeduct = expense.getDepositOrDeduct();
        this.isRecurring = expense.getIsRecurring();
        this.recurrenceRate = expense.getRecurrenceRate();
        this.isFirstOccurrence = expense.getIsFirstOccurrence();
        this.nextOccurrence = expense.getNextOccurrence();
    }


    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }



    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getDepositOrDeduct() {
        return depositOrDeduct;
    }

    public void setDepositOrDeduct(int depositOrDeduct) {
        this.depositOrDeduct = depositOrDeduct;
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public RecurrenceRate getRecurrenceRate() {
        return recurrenceRate;
    }

    public void setRecurrenceRate(RecurrenceRate recurrenceRate) {
        this.recurrenceRate = recurrenceRate;
    }

    public boolean getIsFirstOccurrence() {
        return isFirstOccurrence;
    }

    public void setIsFirstOccurrence(boolean firstOccurrence) {
        isFirstOccurrence = firstOccurrence;
    }

    public Calendar getNextOccurrence() {
        return nextOccurrence;
    }

    public void setNextOccurrence(Calendar nextOccurrence) {
        this.nextOccurrence = nextOccurrence;
    }


    public String toString() {
        return "Expense ID: " + this.getExpenseId() + "\r\n"
                + "Account ID: " + this.getAccountId() + "\r\n"
                + "Title: " + this.getTitle() + "\r\n"
                + "Amount: $" + this.getAmount() + "\r\n"
                + "Category: " + this.getCategory().getText() + "\r\n"
                + "Date: " + ExpenseTypeConverters.dateToString(this.getDate()) + "\r\n"
                + "Deposit or Deduct: " + (this.getDepositOrDeduct() == 1 ? "Deposit" : "Deduct") + "\r\n"
                + "Is Recurring: " + this.getIsRecurring() + "\r\n"
                + "Recurrence Rate: " + this.getRecurrenceRate().getText() + "\r\n"
                + "Is First Occurrence: " + this.getIsFirstOccurrence() + "\r\n"
                + "Next Occurrence: " + ExpenseTypeConverters.dateToString(this.getDate()) + "\r\n"
                + "\r\n";
    }
}
