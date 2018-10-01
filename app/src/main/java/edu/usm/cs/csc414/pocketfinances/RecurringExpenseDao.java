package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecurringExpenseDao {
    @Insert
    void insert(RecurringExpense... RecurringExpense);

    @Update
    void update(RecurringExpense... RecurringExpense);

    @Delete
    void delete(RecurringExpense... RecurringExpense);

    @Query("SELECT * FROM RecurringExpense ORDER BY date DESC")
    LiveData<List<RecurringExpense>> getAllRecurringExpenses();


}
