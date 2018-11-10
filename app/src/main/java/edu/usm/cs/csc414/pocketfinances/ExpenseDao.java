package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense... expense);

    @Update
    void update(Expense... expense);

    @Delete
    void delete(Expense... expense);

    @Query("SELECT * FROM Expense WHERE account_id=:accountId AND ((NOT is_recurring) OR (NOT is_first_occurrence)) ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesOnAccount(int accountId);

    @Query("SELECT * FROM Expense WHERE category=:categoryValue AND ((NOT is_recurring) OR (NOT is_first_occurrence)) ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesOnCategoryValue(int categoryValue);

    @Query("SELECT * FROM Expense WHERE category=:categoryValue AND account_id=:accountId AND ((NOT is_recurring) OR (NOT is_first_occurrence)) ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesOnCategoryAndAccount(int categoryValue, int accountId);

    @Query("SELECT * FROM Expense WHERE expense_id=:expenseId")
    LiveData<List<Expense>> getExpense(int expenseId);

    @Query("SELECT * FROM Expense WHERE ((NOT is_recurring) OR (NOT is_first_occurrence)) ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM Expense WHERE is_recurring AND is_first_occurrence")
    LiveData<List<Expense>> getAllRecurringExpenses();

    @Query("SELECT * FROM Expense WHERE is_recurring AND is_first_occurrence AND next_occurrence < :now")
    LiveData<List<Expense>> getAllRecurringExpensesBeforeDate(long now);
}
