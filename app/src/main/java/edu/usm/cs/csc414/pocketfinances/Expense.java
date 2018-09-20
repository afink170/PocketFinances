package edu.usm.cs.csc414.pocketfinances;

import java.util.Date;

public class Expense {

    // Different categories of expenses
    public static final String CATEGORY_SHOPPING = "Shopping";
    public static final String CATEGORY_DINING = "Dining";
    public static final String CATEGORY_AUTO = "Auto";
    public static final String CATEGORY_GROCERIES = "Groceries";
    public static final String CATEGORY_SAVINGS = "Savings";
    public static final String CATEGORY_TRAVEL = "Travel";
    public static final String CATEGORY_UTILITIES = "Utilities";
    public static final String CATEGORY_HOUSEHOLD = "Household";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_SALARY = "Salary";
    public static final String CATEGORY_DEPOSIT = "Deposit";
    public static final String CATEGORY_CREDIT_CARD_PAYMENT = "Credit Card Payment";
    public static final String CATEGORY_GIFTS = "Gifts";
    public static final String CATEGORY_HEALTH_FITNESS = "Health & Fitness";
    public static final String CATEGORY_RENT = "Rent";
    public static final String CATEGORY_TAX= "Tax";
    public static final String CATEGORY_OTHER = "Other";

    // Different options for recurring payments
    public static final String RECURRENCE_DAILY = "daily";
    public static final String RECURRENCE_WEEKLY = "weekly";
    public static final String RECURRENCE_BIWEEKLY = "biweekly";
    public static final String RECURRENCE_MONTHLY = "monthly";
    public static final String RECURRENCE_3MONTHS = "3months";
    public static final String RECURRENCE_6MONTHS = "6months";
    public static final String RECURRENCE_YEARLY = "yearly";

    public static final int DEPOSIT = 1;
    public static final int DEDUCT = -1;


    private int accountId; //Foreign Key
    private int expenseId; // Primary Key
    private String title;
    private String category;
    private double amount;
    private Date date;
    private int depositOrDeduct;
    private boolean isRecurring;
    private String recurrenceRate;




    public Expense(int accountId, int expenseId, String title, String category, double amount, Date date, int depositOrDeduct, boolean isRecurring, String recurrenceRate) {
        this.accountId = accountId;
        this.expenseId = expenseId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.depositOrDeduct = depositOrDeduct;
        this.isRecurring = isRecurring;
        this.recurrenceRate = recurrenceRate;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDepositOrDeduct() {
        return depositOrDeduct;
    }

    public void setDepositOrDeduct(int depositOrDeduct) {
        this.depositOrDeduct = depositOrDeduct;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getRecurrenceRate() {
        return recurrenceRate;
    }

    public void setRecurrenceRate(String recurrenceRate) {
        this.recurrenceRate = recurrenceRate;
    }
}
