package com.example.project3.model;

public class AccountType {
    private String accountTypeID;
    private String accountName;
    private long amountMoney;

    public AccountType() {
    }

    public AccountType(String accountTypeID, String accountName, long amountMoney) {
        this.accountTypeID = accountTypeID;
        this.accountName = accountName;
        this.amountMoney = amountMoney;
    }

    public String getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(String accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(long amountMoney) {
        this.amountMoney = amountMoney;
    }
}
