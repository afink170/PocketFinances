package edu.usm.cs.csc414.pocketfinances;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "BankAccount")
public class BankAccount {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="account_id")
    private int accountId;

    @ColumnInfo(name="bank_name")
    private String bankName;

    @ColumnInfo(name="account_name")
    private String accountName;

    @ColumnInfo(name="account_type")
    private String accountType;

    @ColumnInfo(name="account_balance")
    private double accountBalance;


    public BankAccount(String bankName, String accountName, String accountType, double accountBalance) {
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getAccountId() {
        return accountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
