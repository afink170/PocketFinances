package edu.usm.cs.csc414.pocketfinances;

public class BankAccount {

    private int accountId;
    private String bankName;
    private String accountName;
    private String accountType;
    private String accountBalance;


    public BankAccount(int accountId, String bankName, String accountName, String accountType, String accountBalance) {
        this.accountId = accountId;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }


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

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }
}
