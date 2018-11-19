package edu.usm.cs.csc414.pocketfinances;

public class ExpenseItem {

    private Expense expense;
    private int index;

    public ExpenseItem(Expense expense, int index) {
        this.expense = expense;
        this.index = index;
    }

    public Expense getExpense() {
        return expense;
    }

    public int getIndex() {
        return index;
    }
}
