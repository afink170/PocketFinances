package edu.usm.cs.csc414.pocketfinances;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BankAccountDao {

    @Insert
    void insert(BankAccount... bankAccount);

    @Update
    void update(BankAccount... bankAccount);

    @Delete
    void delete(BankAccount... bankAccount);

    @Query("SELECT * FROM BankAccount ORDER BY account_id")
    LiveData<List<BankAccount>> getAllBankAccounts();

    @Query("SELECT * FROM BankAccount WHERE account_id=:accountId")
    LiveData<BankAccount> getBankAccount(int accountId);

    @Query("UPDATE BankAccount SET account_balance = account_balance + :balance WHERE account_id = :accountId")
    void updateBalance(int accountId, double balance);
}
